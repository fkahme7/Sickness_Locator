package com.a500.sweng.sickness_locator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.a500.sweng.sickness_locator.common.AppPreferences;
import com.a500.sweng.sickness_locator.models.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.facebook.FacebookSdk;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    private CallbackManager mCallbackManager;
    private static final String TAG = "login_button";
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String password;
    String email;
    String name;
    String loginType;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());


        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference("users");
        // set the view now
        setContentView(R.layout.activity_login);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);

        //Get Firebase auth instance
       // auth = FirebaseAuth.getInstance();




        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginType = "Facebook";
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());

               // updateUI(user);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // [START_EXCLUDE]
                updateUI(null, loginType);
                // [END_EXCLUDE]
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // [START_EXCLUDE]
                updateUI(null, loginType);
                // [END_EXCLUDE]
            }
        });

       /* LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    email= object.getString("email");
                                    name = object.getString("name"); // 01/31/1980 format
                                    Log.v("LoginActivity Name", name);
                                    Log.v("LoginActivity Email", email);

                                    password = "temp123";

                                    auth.signInWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    // If sign in fails, display a message to the user. If sign in succeeds
                                                    // the auth state listener will be notified and logic to handle the
                                                    // signed in user can be handled in the listener.
                                                    progressBar.setVisibility(View.GONE);
                                                    if (!task.isSuccessful()) {
                                                        // there was an error
                                                        if (password.length() < 6) {
                                                            inputPassword.setError(getString(R.string.minimum_password));
                                                        } else {
                                                            signUpWithFB(email, password);
                                                        }
                                                    } else {
                                                        Log.v("Facebook SignIn", email);
                                                        Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }
                                            });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });*/





        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginType = "Application";
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {

                                    Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                updateUI(user, loginType);
                // [END_EXCLUDE]
            }
        };


    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            auth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }/*else{
                            Log.i("FBSignIn success", String.valueOf(task.isSuccessful()));
                            *//*Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                            startActivity(intent);
                            finish();*//*
                        }*/

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }

                });

    }

    private void updateUI(FirebaseUser user, String loginType) {
        //hideProgressDialog();
        if (user != null) {
            if(loginType.equals("Facebook")){
            password = "temp123";
            email = user.getEmail();
            name = user.getDisplayName();
            Log.i("FbUpdate ==>", email);
            Log.i("FbUpdate ==>", name);
            //mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(LoginActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            if (!task.isSuccessful()) {
                                //Toast.makeText(LoginActivity.this, "Authentication failed." + task.getException(),Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                                startActivity(intent);
                                finish();

                                /*auth.signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                // If sign in fails, display a message to the user. If sign in succeeds
                                                // the auth state listener will be notified and logic to handle the
                                                // signed in user can be handled in the listener.
                                                progressBar.setVisibility(View.GONE);
                                                if (!task.isSuccessful()) {
                                                    // there was an error
                                                    if (password.length() < 6) {
                                                        inputPassword.setError(getString(R.string.minimum_password));
                                                    } else {
                                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                                    }
                                                } else {

                                                    Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        });
*/


                            } else {
                                FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                                if (fUser != null) {
                                    //String name = inputName.getText().toString().trim();
                                    User user = new User();
                                    user.setEmail(email);
                                    user.setName(name);
                                    mDatabaseUsers.child(fUser.getUid()).setValue(user);
                                }
                                startActivity(new Intent(LoginActivity.this, MapsActivity.class));
                                finish();
                            }
                        }
                    });

            //findViewById(R.id.login_button).setVisibility(View.GONE);
            //findViewById(R.id.button_facebook_signout).setVisibility(View.VISIBLE);
        } else{
                startActivity(new Intent(LoginActivity.this, MapsActivity.class));
                finish();
            }
        }else {
            //mStatusTextView.setText(R.string.signed_out);
            //mDetailTextView.setText(null);

            findViewById(R.id.login_button).setVisibility(View.VISIBLE);
            //findViewById(R.id.button_facebook_signout).setVisibility(View.GONE);
        }
    }
}



