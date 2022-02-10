package com.tworoot2.assignmentquantum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.tworoot2.assignmentquantum.slider.SliderAdapter;

import javax.crypto.Mac;

public class MainActivity extends AppCompatActivity {

    TextView name, email;
    ImageView hamburger;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navView;
    Button logout;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
    String UID;
    SliderAdapter sliderAdapter;
    SliderView sliderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        setTitle("Home Page");

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        logout = findViewById(R.id.logout);
        hamburger = (ImageView) findViewById(R.id.hamburger);


        myDrawerLayout();


        String[] images = new String[]{
                "https://assetscdn1.paytm.com/images/catalog/view_item/865487/1643716361063.jpg?imwidth=480&impolicy=hq",
                "https://sslimages.shoppersstop.com/sys-master/root/h6f/ha9/26934087450654/men-newrevised28012022necasualwear_web.jpg",
                "https://assets.myntassets.com/f_webp,w_980,c_limit,fl_progressive,dpr_2.0/assets/images/2022/2/9/864ae453-537b-4ac8-a161-786b7950e3701644389121675-Prebuzz-banner_1.gif",
                "https://assetscdn1.paytm.com/images/catalog/view_item/940221/1643716500478.jpg?imwidth=1600&impolicy=hq"};


        String[] description = new String[]{" ", " ", " ", " "};

        sliderView = (SliderView) findViewById(R.id.imageSlider);
        sliderAdapter = new SliderAdapter(images, description);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.DROP);
        sliderView.startAutoCycle();

        UID = user.getUid();

        if (user != null) {

            if (user.getDisplayName() != null) {
                name.setText(user.getDisplayName());

            } else {

                reference.child(UID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
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
                logOut();
            }
        });

    }

    private void logOut() {

        auth.signOut();
        LoginManager.getInstance().logOut();
        openLogin();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (user == null) {
            openLogin();
        }
    }

    private void openLogin() {

        Intent i = new Intent(MainActivity.this, LoginSignUp.class);
        startActivity(i);
        finishAffinity();
    }


    private void myDrawerLayout() {

        navView = (NavigationView) findViewById(R.id.navigation);

        drawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int itemId = menuItem.getItemId();
                switch (itemId) {
                    case R.id.nav_home: {
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        return true;
                    }

                    case R.id.nav_privacy: {
                        Toast.makeText(MainActivity.this, "Privacy policy", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        return true;
                    }
                    case R.id.nav_contact: {
                        Toast.makeText(MainActivity.this, "Contact us", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        return true;
                    }


                    case R.id.nav_rate: {
                        Toast.makeText(MainActivity.this, "Rate your work by 5 star", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        return true;
                    }

                    case R.id.logout: {

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                        alertDialog.setTitle("Log out");
                        alertDialog.setMessage("Are you sure you want to log out ?");
                        alertDialog.setIcon(R.drawable.logout);
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "Logout successfully", Toast.LENGTH_SHORT).show();
                                logOut();
                            }
                        });

                        alertDialog.setNegativeButton("No", null);
                        alertDialog.show();


                        return true;
                    }

                }
                return false;
            }
        });

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }


}