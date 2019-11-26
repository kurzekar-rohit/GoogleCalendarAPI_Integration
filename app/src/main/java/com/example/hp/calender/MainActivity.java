package com.example.hp.calender;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    int RC_SIGN_IN = 0;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    private TextView mTextView;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signInButton = findViewById(R.id.btn_sign_in);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //  .requestIdToken(getString(R.string.server_client_id))
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestServerAuthCode(getString(R.string.server_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        mTextView = (TextView) findViewById(R.id.text);
//         new CallAPI().execute();


    }

//    public class CallAPI extends AsyncTask<String, String, String> {
//        String authcodeN;
//        public CallAPI(String authcode) {
//            authcodeN = authcode;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            System.out.println("hhhh");
//        }
//
//        @Override
//        protected String doInBackground (String... strings){
//            HttpClient client = new DefaultHttpClient();
//
//            HttpPost httpPost = new HttpPost("https://accounts.google.com/o/oauth2/auth");
//            try {
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
//                nameValuePairs.add(new BasicNameValuePair("authCode", authcodeN));
//                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                HttpResponse response = client.execute(httpPost);
//                int statusCode = response.getStatusLine().getStatusCode();
//                final String responseBody = EntityUtils.toString(response.getEntity());
//
//            }
//            catch (ClientProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            System.out.println("string data --- "+s);
//        }
//    }

//    private String sendPostRequest(final String authcode) {
//
//        new CallAPI(authcode).execute("");
//        return null;
//
//    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {

        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String authCode = account.getServerAuthCode();
            String emailId = account.getEmail();
//            String profile = String.valueOf(account.getPhotoUrl());
            Uri profile = account.getPhotoUrl();
            mTextView.setText(getString(R.string.id_token_fmt, authCode));
//            sendPostRequest(authCode);
//          String idToken = account.getIdToken();
//          mTextView.setText(getString(R.string.id_token_fmt,idToken));

//            String CLIENT_SECRET_FILE = "/src/main/res/client_secret.json";
//            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new FileReader(CLIENT_SECRET_FILE));
//            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), "https://oauth2.googleapis.com/token", clientSecrets.getDetails().getClientId(), clientSecrets.getDetails().getClientSecret(), authCode, null)
//                    .execute();
//            String accessToken = tokenResponse.getAccessToken();
//            GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
////            Drive drive =
////                    new Drive.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
////                            .setApplicationName("Auth Code Exchange Demo")
////                            .build();
////
////            File file = drive.files().get("appfolder").execute();
//            GoogleIdToken idToken = tokenResponse.parseIdToken();
//            GoogleIdToken.Payload payload = idToken.getPayload();
//            String userId = payload.getSubject();  // Use this value as a key to identify a user.
//            String email = payload.getEmail();
//            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
//            String name = (String) payload.get("name");
//            String pictureUrl = (String) payload.get("picture");
//            String locale = (String) payload.get("locale");
//            String familyName = (String) payload.get("family_name");
//            String givenName = (String) payload.get("given_name");


            SharedPreferences sp = getSharedPreferences("Email", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("email", emailId);
//            editor.putString("email", email);
            editor.putString("profile_url", String.valueOf(profile));
            editor.apply();


            startActivity(new Intent(MainActivity.this, DetailActivity.class));
        } catch (ApiException e) {
            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
        }
//        catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onStart() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            startActivity(new Intent(MainActivity.this, DetailActivity.class));
        }
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
