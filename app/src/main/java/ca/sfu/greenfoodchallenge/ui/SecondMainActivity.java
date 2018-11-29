package ca.sfu.greenfoodchallenge.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import ca.sfu.greenfoodchallenge.database.GreenFoodChallengeDatabase;
import ca.sfu.greenfoodchallenge.database.User;


/*SecondMainActivity controls the navigation drawer and initiates switching between activities.
 */
public class SecondMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    private NavigationView navigationView;
    private ImageView navIcon;
    private TextView navUser;
    private TextView navEmail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                   new MealSummaryFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_pledge);
        }
        loadHeaderViews(navigationView.getHeaderView(0));

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            //My diet
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MealSummaryFragment()).commit();
                updateHeaderFields();
                break;
            case R.id.nav_calendar:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CalendarFragment()).commit();
                break;
            case R.id.nav_recommendation:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RecommendationFragment()).commit();
                break;


            //Social
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SecondProfileFragment()).commit();
                break;

            case R.id.nav_share:    ///Firebase instance variables
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Share()).commit();
                break;

            case R.id.nav_pledge:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SecondPledgeFragment()).commit();
                break;

            case R.id.nav_my_pledge:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PledgeFragment()).commit();
                break;

            case R.id.nav_restaurant:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RestaurantMealsFragment()).commit();
                    break;

            //About
            case R.id.nav_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AboutFragment()).commit();
                break;

            case R.id.nav_signout:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SecondShareFragment()).commit();

                //Go back to the login page
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(SecondMainActivity.this, "User Signed Out", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SecondMainActivity.this, LoginActivity.class));

                            }
                        });
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Share fragment = (Share) getSupportFragmentManager().findFragmentByTag("Share");
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void updateHeaderFields() {

        User user = GreenFoodChallengeDatabase.getCurrentUser();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Uri uri = Uri.parse(user.getPhotoUrl());
            navIcon.setImageURI(uri);
            if(uri == null) {
                navIcon.setImageResource(R.mipmap.ic_app_round);
            }
        } catch (Exception e) {
            e.getMessage();
            navIcon.setImageResource(R.mipmap.ic_app_round);
        }

        String userName = user.getFirstName() + " " + user.getLastNameInitial();
        userName = (userName.equals("none none")|| userName.equals("none n")) ? getText(R.string.anonymous).toString() : userName;

        navUser.setText(userName);
        String userEmail = user.getEmail();

        userEmail = userEmail.equals("none") ? "" : userEmail;
        navEmail.setText(userEmail);
    }


    public void loadHeaderViews(View hView) {
        navIcon = (ImageView) hView.findViewById(R.id.nav_icon);
        navUser= (TextView) hView.findViewById(R.id.nav_name);
        navEmail = (TextView) hView.findViewById(R.id.nav_email);
    }


}

