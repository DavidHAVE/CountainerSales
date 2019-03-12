package com.deva.android.countainersales.autentikasi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.deva.android.countainersales.admin.AdminActivity;
import com.deva.android.countainersales.helper.FirebaseHandler;
import com.deva.android.countainersales.ui.MainActivity;
import com.deva.android.countainersales.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.R.attr.name;
import static com.deva.android.countainersales.helper.StaticValue.ACCOUNT_PREFS;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailEditText, mPasswordEditText;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button mSignupButton, mLoginButton, mResetPasswordButton;

    private String email, password;

    //playlogin
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 0 ;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private com.google.android.gms.common.SignInButton mSigninButton;
    private GoogleApiClient mGoogleApiClient;

    // Firebase instance variables
    private FirebaseUser user;
    private String userId;
    private FirebaseHandler firebaseHandler;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mProfileDatabaseReference;

    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        user = FirebaseAuth.getInstance().getCurrentUser();
//        userId = user.getUid();
        firebaseHandler = new FirebaseHandler(this, "");

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();


        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS, MODE_PRIVATE);
//        String restoredText = prefs.getString("text", null);
//        if (restoredText != null) {
            email = prefs.getString("email", "No name defined");//"No name defined" is the default value.
            password = prefs.getString("password", "No password defined");
//            int idName = prefs.getInt("idName", 0); //0 is the default value.

//            }

            if (auth.getCurrentUser() != null) {
                if (email.equals("david@gmail.com") && password.equals("123456")) {
                    startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            }

            // set the view now
            setContentView(R.layout.activity_login);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

            mEmailEditText = (EditText) findViewById(R.id.emailEditText);
            mPasswordEditText = (EditText) findViewById(R.id.passwordEditText);
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            mLoginButton = (Button) findViewById(R.id.loginButton);
            mSignupButton = (Button) findViewById(R.id.signupButton);

            //Initialize Firebase Components
            mFirebaseDatabase = FirebaseDatabase.getInstance();

            mProfileDatabaseReference = mFirebaseDatabase.getReference().child("profile");

            //Get Firebase auth instance
            auth = FirebaseAuth.getInstance();


            mSignupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                }
            });


            mLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String email = mEmailEditText.getText().toString();
                    final String password = mPasswordEditText.getText().toString();


                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    SharedPreferences.Editor editor = getSharedPreferences(ACCOUNT_PREFS, MODE_PRIVATE).edit();
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.apply();

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
                                            mPasswordEditText.setError(getString(R.string.minimum_password));
                                        } else {
                                            Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                        }
                                    } else {

                                        if (email.equals("david@gmail.com") && password.equals("123456")) {
                                            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Log.i("LoginActivity", "user = " + auth.getCurrentUser());
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }
                            });
                }
            });


        }
    }
