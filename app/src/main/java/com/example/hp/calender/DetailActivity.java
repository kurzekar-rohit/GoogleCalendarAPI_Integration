package com.example.hp.calender;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.calender.adapter.EventsAdapter;
import com.example.hp.calender.api.ApiInterface;
import com.example.hp.calender.model.Example;
import com.example.hp.calender.model.Item;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = "Click";
    private final static String API_KEY = "AIzaSyCA_y6aK_bmfH3FNwB9qlE4ifh7FTXt9n4";
    private Context context;
    public static final String BASE_URL = "https://www.googleapis.com/calendar/v3/calendars/";
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView mTokenId;
    SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    List<Item> item;
    private Button mButton;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_GET_TOKEN = 9002;
    ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

//        mTokenId = findViewById(R.id.token);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(DetailActivity.this);
        if (acct != null) {
            // String personName = acct.getDisplayName();
//            String personEmail =acct.getEmail();
//            mTokenId.setText("Name:"+personEmail);
        }


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.simpleSwipeRefreshLayout);
        final FloatingActionButton fab = findViewById(R.id.fab);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                refresh();
            }
        });


        SharedPreferences sp = getSharedPreferences("Email", MODE_PRIVATE);
        String email = sp.getString("email", null);
        final String FINAL_URL = BASE_URL + email + "/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FINAL_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //  String profile = sp.getString("profile_url", null);
        image = (ImageView) findViewById(R.id.image);
//        Picasso.with(getApplicationContext())
//                .load(profile)
//                .resize(600, 200)
//                .into(image);

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY first", Toast.LENGTH_LONG).show();
            return;
        }
        ApiInterface api = retrofit.create(ApiInterface.class);
        Call<Example> call = api.getCalendarEvents(API_KEY);


        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if (!response.isSuccessful()) {
//                    mTextView.setText("Code: " + response.code());
                    return;
                }

                item = response.body().getItems();
                recyclerView.setAdapter(new EventsAdapter(item, R.layout.list_item_event, getApplicationContext()));

                Collections.sort(item, new Comparator<Item>() {
                    @Override
                    public int compare(Item o1, Item o2) {
                        int result;

                        if (o1.getStart().getDateTime() == null && o2.getStart().getDateTime() == null) {
                            String a = o1.getStart().getDate();
                            String b = o2.getStart().getDate();
                            result = a.compareTo(b);
                            return result;
                        } else if (o1.getStart().getDateTime() == null) {
                            String a = o1.getStart().getDate();
                            String b = o2.getStart().getDateTime();
                            result = a.compareTo(b);
                            return result;
                        } else if (o2.getStart().getDateTime() == null) {
                            String a = o1.getStart().getDateTime();
                            String b = o2.getStart().getDate();
                            result = a.compareTo(b);
                            return result;
                        }
                        return o1.getStart().getDateTime().compareTo(o2.getStart().getDateTime());
                    }


                });

                Log.d(TAG, "Number of event received: " + item.size());

            }


            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.e(TAG, t.toString());
//                mTextView.setText(t.getMessage());
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendarEvent = Calendar.getInstance();
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra("beginTime", calendarEvent.getTimeInMillis());
                intent.putExtra("endTime", calendarEvent.getTimeInMillis() + 60 * 60 * 1000);
                intent.putExtra("title", "");
                intent.putExtra("allDay", false);
                intent.putExtra("rule", "FREQ=YEARLY");
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(DetailActivity.this, "Successfully Signed out", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(DetailActivity.this, MainActivity.class));
                        finish();
                    }
                });
    }


    public void refresh() {

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SharedPreferences sp = getSharedPreferences("Email", MODE_PRIVATE);
        String email = sp.getString("email", null);
        final String FINAL_URL = BASE_URL + email + "/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FINAL_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY first", Toast.LENGTH_LONG).show();
            return;
        }
        ApiInterface api = retrofit.create(ApiInterface.class);
        Call<Example> call = api.getCalendarEvents(API_KEY);


        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if (!response.isSuccessful()) {
                    //  mTokenId.setText("Code: " + response.code());
                    return;
                }

                List<Item> item = response.body().getItems();
                recyclerView.setAdapter(new EventsAdapter(item, R.layout.list_item_event, getApplicationContext()));
                Collections.sort(item, new Comparator<Item>() {
                    @Override
                    public int compare(Item o1, Item o2) {
                        int result;
                        if (o1.getStart().getDateTime() == null && o2.getStart().getDateTime() == null) {
                            String a = o1.getStart().getDate();
                            String b = o2.getStart().getDate();
                            result = a.compareTo(b);
                            return result;
                        } else if (o1.getStart().getDateTime() == null) {
                            String a = o1.getStart().getDate();
                            String b = o2.getStart().getDateTime();
                            result = a.compareTo(b);
                            return result;
                        } else if (o2.getStart().getDateTime() == null) {
                            String a = o1.getStart().getDateTime();
                            String b = o2.getStart().getDate();
                            result = a.compareTo(b);
                            return result;
                        }
                        return o1.getStart().getDateTime().compareTo(o2.getStart().getDateTime());
                    }
                });
                Log.d(TAG, "Number of event received: " + item.size());

            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.e(TAG, t.toString());
//                mTextView.setText(t.getMessage());
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}
