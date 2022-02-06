package com.tworoot2.assignmentquantum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class LoginSignUp extends AppCompatActivity {
    TextView signInUI, signUpUI, signIN, signUP;
    LinearLayout signInUILayout, signInUPLayout;
    private static final String EMAIL = "email";
    CallbackManager callbackManager;
    LoginButton facebook_login_button;
    private static final String TAG = "FB Auth";
    FirebaseAuth auth = FirebaseAuth.getInstance();
    SignInButton googleSignIn;
    GoogleSignInClient mGoogleSignInClient;
    CheckBox checkTC;
    EditText upName, upEmail, upCC, upPhone, upPassword, inEmail, inPassword;
    ProgressDialog progressDialog;
    FirebaseDatabase database;
    TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);
        getSupportActionBar().hide();

        inEmail = findViewById(R.id.inEmail);
        inPassword = findViewById(R.id.inPassword);

        upName = findViewById(R.id.upName);
        upEmail = findViewById(R.id.upEmail);
        upCC = findViewById(R.id.upCC);
        upPhone = findViewById(R.id.upPhone);
        upPassword = findViewById(R.id.upPassword);
        signUP = findViewById(R.id.signUP);
        checkTC = findViewById(R.id.checkTC);

        signIN = findViewById(R.id.signIN);
        signUpUI = findViewById(R.id.signUpUI);
        signInUI = findViewById(R.id.signInUI);
        signInUPLayout = findViewById(R.id.signInUPLayout);
        signInUILayout = findViewById(R.id.signInUILayout);
        googleSignIn = findViewById(R.id.googleSignIn);
        forgotPassword = findViewById(R.id.forgotPassword);

        database = FirebaseDatabase.getInstance();

        callbackManager = CallbackManager.Factory.create();

        signInUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUI.setBackgroundResource(R.drawable.button_ui);
                signUpUI.setBackgroundResource(R.color.white);
                signUpUI.setTextColor(getResources().getColor(R.color.black));
                signInUI.setTextColor(getResources().getColor(R.color.white));
                signInUILayout.setVisibility(View.VISIBLE);
                signInUPLayout.setVisibility(View.GONE);
                signIN.setVisibility(View.VISIBLE);
                signUP.setVisibility(View.GONE);

            }
        });
        signUpUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUI.setBackgroundResource(R.drawable.button_ui);
                signInUI.setBackgroundResource(R.color.white);
                signInUI.setTextColor(getResources().getColor(R.color.black));
                signUpUI.setTextColor(getResources().getColor(R.color.white));
                signInUILayout.setVisibility(View.GONE);
                signInUPLayout.setVisibility(View.VISIBLE);
                signIN.setVisibility(View.GONE);
                signUP.setVisibility(View.VISIBLE);
            }
        });

        signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (upName.getText().toString().isEmpty() || upEmail.getText().toString().isEmpty() ||
                        upPhone.getText().toString().isEmpty() || upCC.getText().toString().isEmpty() || upPassword.getText().toString().isEmpty()) {

                    if (upName.getText().toString().isEmpty()) {
                        upName.setError("Enter your name");
                    }
                    if (upEmail.getText().toString().isEmpty()) {
                        upEmail.setError("Enter your email");
                    }
                    if (upPhone.getText().toString().isEmpty()) {
                        upPhone.setError("Enter your phone number");
                    }
                    if (upCC.getText().toString().isEmpty()) {
                        upCC.setError("Enter Country Code");
                    }
                    if (upPassword.getText().toString().isEmpty()) {
                        upPassword.setError("Enter password");
                    }

                } else {

                    signUPHere();

                }
            }
        });

        signIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inEmail.getText().toString().isEmpty() || inPassword.getText().toString().isEmpty()) {

                    if (inEmail.getText().toString().isEmpty()) {
                        inEmail.setError("Enter your email");
                    }
                    if (inPassword.getText().toString().isEmpty()) {
                        inPassword.setError("Enter your password");
                    }
                } else {
                    signINHere();
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inEmail.getText().toString().isEmpty()) {

                    if (inEmail.getText().toString().isEmpty()) {
                        inEmail.setError("Enter your email to reset");
                    }

                }
                else {
                    forgotPasswordMethod(inEmail.getText().toString());
                }
            }
        });

        // facebook login

        facebook_login_button = (LoginButton)

                findViewById(R.id.facebook_login_button);
        facebook_login_button.setPermissions(Arrays.asList(EMAIL));

        facebook_login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "onError: " + exception.getMessage());
            }
        });

        // facebook login end

        // google login start

        googleSignin();


        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();

            }
        });
        // google login end

    }

    private void forgotPasswordMethod(String email) {

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginSignUp.this, "Check your email - Reset link sent successfully", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(LoginSignUp.this, "Something went wrong check your email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 101);
    }

    private void googleSignin() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(LoginSignUp.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            openProfile();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginSignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            openProfile();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginSignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void openProfile() {

        Intent i = new Intent(LoginSignUp.this, MainActivity.class);
        startActivity(i);
        finish();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            openProfile();
        }
    }

    public void signUPHere() {

        progressDialog = new ProgressDialog(LoginSignUp.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Registering....");
        progressDialog.show();

        String email1 = upEmail.getText().toString();
        String password1 = upPassword.getText().toString();
        String name1 = upName.getText().toString();
        String phone1 = upCC.getText().toString() + upPhone.getText().toString();

        auth.createUserWithEmailAndPassword(email1, password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCustomToken:success");
                            Toast.makeText(LoginSignUp.this, "Registered successfully",
                                    Toast.LENGTH_SHORT).show();


                            // make profile

                            FirebaseUser user = auth.getCurrentUser();
                            String uid = user.getUid();
                            String name3 = name1;
                            String email3 = user.getEmail();
                            String phone3 = phone1;
                            String password3 = password1;

                            UserModel userModel = new UserModel(uid, name3, email3, phone3, password3);

                            database.getReference()
                                    .child("Users")
                                    .child(uid)
                                    .setValue(userModel)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            openProfile();
                                            progressDialog.dismiss();
                                            upEmail.setText("");
                                            upName.setText("");
                                            upPhone.setText("");
                                            upPassword.setText("");
                                        }
                                    });

                            //

                        } else {
                            Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                            Toast.makeText(LoginSignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    }
                });

    }

    public void signINHere() {
        progressDialog = new ProgressDialog(LoginSignUp.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();

        String email2 = inEmail.getText().toString();
        String password2 = inPassword.getText().toString();

        auth.signInWithEmailAndPassword(email2, password2)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCustomToken:success");
                            Toast.makeText(LoginSignUp.this, "Login successfully",
                                    Toast.LENGTH_SHORT).show();
                            inEmail.setText("");
                            inPassword.setText("");

                            progressDialog.dismiss();
                            openProfile();
                        } else {
                            Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                            Toast.makeText(LoginSignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    }
                });

    }

}