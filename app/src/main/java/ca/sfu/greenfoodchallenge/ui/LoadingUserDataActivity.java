package ca.sfu.greenfoodchallenge.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ca.sfu.greenfoodchallenge.database.GreenFoodChallengeDatabase;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoadingUserDataActivity extends AppCompatActivity {


    //Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loading_user_data);

        collectUserInfoAtLoginPage();


        Thread dataLoaderThread = new Thread(){
            @Override
            public void run(){
                GreenFoodChallengeDatabase.waitForData();
                startMainActivity();
            }

        };

        dataLoaderThread.start();

    }



    public void startMainActivity()
    {
        Intent intent = new Intent(getBaseContext(),SecondMainActivity.class);
        startActivity(intent);
        finish();
    }




    public void collectUserInfoAtLoginPage() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        String emailAddress = mFirebaseUser.getEmail();
        String loginUserName = mFirebaseUser.getDisplayName();
//        System.out.println("Login name and user are : " + emailAddress + loginUserName);
//        Log.w("Login :",  emailAddress +"     /     "+ loginUserName);


        //We use UNMODIFIED email address to check same data.
        GreenFoodChallengeDatabase.initialize(emailAddress);

        GreenFoodChallengeDatabase.checkUserData(emailAddress, loginUserName);

    }


}
