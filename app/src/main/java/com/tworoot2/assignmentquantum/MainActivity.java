package com.tworoot2.assignmentquantum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    TextView name, email;
    Button logout;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().hide();
        setTitle("Home Page");

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        logout = findViewById(R.id.logout);

        UID = user.getUid();

        if (user != null){

            if (user.getDisplayName() != null ){
                name.setText(user.getDisplayName());

            }
            else {

                reference.child(UID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel =  snapshot.getValue(UserModel.class);
                        name.setText(userModel.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this, "Error while loading name", Toast.LENGTH_SHORT).show();
                    }
                });
                


            }
            email.setText(user.getEmail());
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                LoginManager.getInstance().logOut();
                openLogin();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (user == null){
            openLogin();
        }
    }

    private void openLogin() {

        Intent i = new Intent(MainActivity.this, LoginSignUp.class);
        startActivity(i);
        finish();
    }
}