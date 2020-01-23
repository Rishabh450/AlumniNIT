package com.meet2018_final.alumninitjsr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.meet2018_final.alumninitjsr.Fragment.Feed;
import com.meet2018_final.alumninitjsr.Support.Login;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth mAuth;String email,name;
    Uri photoUrl;
    TextView name1,email1;NavigationView navigationView;
    private Context context=MainActivity.this;
    ImageView profilePhoto;DrawerLayout drawer;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.e("ak47","user null");

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null)
                {
                    Log.e("ak47","user null");
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                }
                else
                {



                }

            }
        };
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            drawer = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.nav_view);

            name = user.getDisplayName();
            email = user.getEmail();
            photoUrl = user.getPhotoUrl();
            Toast.makeText(this,name+" is Logged in",Toast.LENGTH_LONG).show();
            View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
            name1=headerView.findViewById(R.id.name);
            name1.setText(name);
            email1=headerView.findViewById(R.id.email);
            email1.setText(email);
            profilePhoto=headerView.findViewById(R.id.profilePhoto);

            CircularProgressDrawable circularProgressDrawable = new  CircularProgressDrawable(MainActivity.this);
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(20f) ;
            circularProgressDrawable.start();
            Glide.with(context)
                    .load(photoUrl)
                    .placeholder(circularProgressDrawable)
                    .into(profilePhoto);

            navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) MainActivity.this);





            ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
            drawer.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            Feed feed=new Feed();
            fragmentTransaction.add(R.id.fragment_container,feed);
            fragmentTransaction.commit();

        }

        Log.e("ak47","user not null");


    }

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Log.e("ak47","on Start");
        super.onStart();
        Log.e("ak47","on Start after super");
        mAuth.addAuthStateListener(authStateListener);
        Log.e("ak47","on Start Ends");
    }
    private void signOut() {
        mAuth.signOut();
        mGoogleSignInClient.signOut();
    }
    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainactivitymenu, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.logout: signOut();
            break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
}
