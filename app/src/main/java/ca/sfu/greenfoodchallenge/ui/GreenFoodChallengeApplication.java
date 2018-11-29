package ca.sfu.greenfoodchallenge.ui;

import android.app.Application;
import android.content.SharedPreferences;
import android.text.format.DateFormat;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.GregorianCalendar;

import ca.sfu.greenfoodchallenge.meal.DummyContent;
import ca.sfu.greenfoodchallenge.meal.MealCalendar;
import io.fabric.sdk.android.Fabric;


/*
 * Instantiated when the app is open this class is a place to keep global information about the
 * application.
 */
public class GreenFoodChallengeApplication extends Application{


    // Runs on application boot
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());


    }






}
