package ca.sfu.greenfoodchallenge.ui;

import android.util.Log;

import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import ca.sfu.greenfoodchallenge.database.GreenFoodChallengeDatabase;
import ca.sfu.greenfoodchallenge.database.User;
import ca.sfu.greenfoodchallenge.meal.Day;

public class GreenFoodChallengeDatabaseTest {



    //@Before


    @Test
    public void userGettingAndSettingTests() {
        GreenFoodChallengeDatabase.initialize("invio,george@gmail,com");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        User g = GreenFoodChallengeDatabase.getCurrentUser();

        Log.d("ACCOUNT_TEST","=====before======");
        dumpUser(g);

        g.setPhotoUrl("abc123");
        GreenFoodChallengeDatabase.updateCurrentUser();


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d("ACCOUNT_TEST","=====After=====");
        dumpUser(g);



    }




    @Test
    public void globalUsersTests(){
        GreenFoodChallengeDatabase.initialize("invio,george@gmail,com");


        List<User> all =  GreenFoodChallengeDatabase.getUsersByMunicipality("Vancouver");


//        for (User user : all) {
//
//
//            if(user.getEmail().equals("invio.george@gmail.com")){
//                Map<Date,Day> period =  user.getMealCalendar().getLastNDays(7);
//                Log.d("GFC_TEST",period.keySet().iterator().next().toString());
//            }
//
//        }


        Map<Date,Day> period =  GreenFoodChallengeDatabase.getCurrentUser().getMealCalendar().getLastNDays(7);
        Log.d("GFC_TEST",period.keySet().iterator().next().toString());


    }







    @Test
    public void mealCalendarIntegration(){
        GreenFoodChallengeDatabase.initialize("invio,george@gmail,com");
        pause(200);



    }





    private  void dumpUser(User u){
        Log.d("ACCOUNT_TEST",""+u.getFirstName());
        Log.d("ACCOUNT_TEST",""+u.getLastNameInitial());
        Log.d("ACCOUNT_TEST",""+u.getCo2PledgeAmount());
        Log.d("ACCOUNT_TEST",""+u.getMunicipality());
        Log.d("ACCOUNT_TEST",""+u.getEmail());
        Log.d("ACCOUNT_TEST",""+u.getPhotoUrl());
    }



    private static void pause(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}