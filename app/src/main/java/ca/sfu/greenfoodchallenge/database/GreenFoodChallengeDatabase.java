package ca.sfu.greenfoodchallenge.database;


//this is the only class where the firebase imports should appear
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import ca.sfu.greenfoodchallenge.meal.Day;
import ca.sfu.greenfoodchallenge.meal.DefaultProteins;
import ca.sfu.greenfoodchallenge.meal.Meal;
import ca.sfu.greenfoodchallenge.meal.MealCalendar;
import ca.sfu.greenfoodchallenge.meal.MealComponent;
import ca.sfu.greenfoodchallenge.meal.Protein;
/**
 * Abstraction layer on top of the firebase database.
 * This class should not be instantiated.
 *
 * To compute the total CO2 use User.totalPledgeCarbon from a list
 */
public class  GreenFoodChallengeDatabase {


    // BEGIN public api ----------------------------------------------------------------------------

    public static User getCurrentUser() {
        return currentUser;
    }


    public static void updateCurrentUser(){
        updateCurrentUserQuery(currentUser);
    }


    public static List<User> getAllUsers() {
        return allUsers;
    }


    public static List<User> getAllUsersWithPledge() {
        List<User>  usersWithPledge = new ArrayList<User>();

        for(User user : allUsers) {
            if(user.getCo2PledgeAmount()> 0){
                user = formatNames(user);
                usersWithPledge.add(user);
            }
        }
        return usersWithPledge;
    }


    //A list of municipalities van be found in the MunicipalityClass
    public static List<User> getUsersByMunicipality(String municipality) {
        List<User>  usersInMunicipality = new ArrayList<User>();

        for(User user: allUsers) {
            if(Municipality.filter(user.getMunicipality()).equals(municipality)){
                usersInMunicipality.add(user);
            }
        }

        return usersInMunicipality;
    }


    public static List<User> getUsersWithPledgeByMunicipality(String municipality) {
        List<User>  usersInMunicipality = new ArrayList<User>();

        for(User user: getAllUsersWithPledge()) {
            if(user.getMunicipality().equals(municipality)){
                usersInMunicipality.add(user);
            }
        }

        return usersInMunicipality;
    }



    //run this during the start up of the application
    public static void initialize(String currentUserEmailKey) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        GreenFoodChallengeDatabase.currentUserEmailKey = encodeUserEmail(currentUserEmailKey);
        refreshCurrentUserQuery();
        allUsersQuery();

        currentUserDataIsLoadedLatch = new CountDownLatch(1);
        allUserDataIsLoadedLatch = new CountDownLatch(1);
    }


    public static void waitForData()
    {
        try {
            allUserDataIsLoadedLatch.await();
        } catch (InterruptedException e) {
            Log.e("GFC","Interrupted while waiting for data");
            e.printStackTrace();
            waitForData();
        }
        try {
            currentUserDataIsLoadedLatch.await();
        } catch (InterruptedException e) {
            Log.e("GFC","Interrupted while waiting for data");
            e.printStackTrace();
            waitForData();
        }

    }




    //END public api -------------------------------------------------------------------------------



    //\todo{clean activities and make private}
    private static final String CHILD_NAME_USERS = "users";
    private static final String CHILD_NAME_EMAIL = "email";
    private static final String CHILD_NAME_FIRST_NAME = "firstName";
    private static final String CHILD_NAME_LAST_NAME_INITIAL = "lastNameInitial";
    private static final String CHILD_NAME_MUNICIPALITY = "municipality";
    private static final String CHILD_NAME_PHOTO_URL = "photoUrl";
    private static final String CHILD_NAME_USER_NAME = "userName";
    private static final String CHILD_NAME_CO2_AMOUNT = "co2PledgeAmount";
    private static final String CHILD_NAME_MEAL_CALENDAR = "mealCalendar";
    private static final String CHILD_NAME_DAY = "Day";
    private static final String CHILD_NAME_MEAL = "meal";
    private static final String CHILD_NAME_MEAL_NAME = "mealName";
    private static final String CHILD_NAME_RESTAURANT = "restaurant";
    private static final String CHILD_NAME_LOCATION = "location";
    private static final String CHILD_NAME_DESCRIPTION = "description";
    private static final String CHILD_NAME_IMAGE_URL = "imageUrl";
    private static final String CHILD_NAME_COMPONENTS = "components";
    private static final String CHILD_NAME_KILOGRAMS = "kilograms";
    private static final String CHILD_NAME_SHARED = "shared";

    private final static String DEFAULT_NAME = "Anonymous";



    private static DatabaseReference mDatabase;
    private static String currentUserEmailKey;
    private static User currentUser;
    private static List<User> allUsers;

    // Since we can't do anything with the app if the current user is not loaded, use a latch
    // to keep the tread waiting until the data is fetched
    private static CountDownLatch currentUserDataIsLoadedLatch;
    private static CountDownLatch allUserDataIsLoadedLatch;

    //BEGIN Queries --------------------------------------------------------------------------------


    private static void updateCurrentUserQuery(User user) {
        DatabaseReference userRef = getCurrentUserRef();

        userRef.child(CHILD_NAME_EMAIL).setValue(user.getEmail());
        userRef.child(CHILD_NAME_USER_NAME).setValue(user.getUserName());
        userRef.child(CHILD_NAME_FIRST_NAME).setValue(user.getFirstName());
        userRef.child(CHILD_NAME_LAST_NAME_INITIAL).setValue(user.getLastNameInitial());
        userRef.child(CHILD_NAME_CO2_AMOUNT).setValue(user.getCo2PledgeAmount());
        userRef.child(CHILD_NAME_MUNICIPALITY).setValue(user.getMunicipality());
        userRef.child(CHILD_NAME_PHOTO_URL).setValue(user.getPhotoUrl());

        updateMealCalendar(userRef.child(CHILD_NAME_MEAL_CALENDAR),user.getMealCalendar());
    }


    //Updates the current user with the info from the database
    private static void refreshCurrentUserQuery(){
        Log.d("GFC",currentUserEmailKey);
        getCurrentUserRef().addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("GFC","Query executed");
                        currentUser = dataSnapshot2User(dataSnapshot);
                        currentUserDataIsLoadedLatch.countDown();
                    }
                    @Override
                    public void onCancelled(DatabaseError e) {
                        Log.e("GFC","Query was cancelled");
                    }
                }
        );


    }

    private static void allUsersQuery(){
        allUsers = new ArrayList<>();
        mDatabase.child(CHILD_NAME_USERS)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                allUsers = new ArrayList<>();
                                for (DataSnapshot child:dataSnapshot.getChildren()) {
                                    allUsers.add(dataSnapshot2User(child));
                                }
                                allUserDataIsLoadedLatch.countDown();
                            }

                            @Override
                            public void onCancelled(DatabaseError e) {
                            }
                        }
                );

    }

    private static void updateMealCalendar(DatabaseReference mealCalendarRef, MealCalendar mealCalendar){
        if(mealCalendar != null) {
            for(Map.Entry<String,Day> entry : mealCalendar.getDefinedDays().entrySet()){
                updateDay(mealCalendarRef.child(entry.getKey()),entry.getValue());
            }
        }
    }

    private static void updateDay(DatabaseReference dayRef, Day day) {
        for(Map.Entry<String,Meal> entry : day.getMealList().entrySet()){

            updateMeal(dayRef.child(entry.getKey()),entry.getValue());
        }
    }


    private static void updateMeal(DatabaseReference mealRef, Meal meal) {

        Log.d("GFC","Saving Meal");
        mealRef.child(CHILD_NAME_RESTAURANT).setValue(meal.getRestaurant());
        mealRef.child(CHILD_NAME_LOCATION).setValue(meal.getLocation());
        mealRef.child(CHILD_NAME_PHOTO_URL).setValue(meal.getImageUrl());
        mealRef.child(CHILD_NAME_DESCRIPTION).setValue(meal.getDescription());
        mealRef.child(CHILD_NAME_MUNICIPALITY).setValue(meal.getMunicipality());
        mealRef.child(CHILD_NAME_SHARED).setValue(meal.isShared());

        //update the components
        DatabaseReference componentsRef = mealRef.child(CHILD_NAME_COMPONENTS);
        for(MealComponent component : meal.getComponentList()) {
            Log.d("GFC","Saving component");
            componentsRef.child(component.getComponentProtein().getName())
                    .setValue(component.getServingSize());
        }
    }
    

    //END queries-----------------------------------------------------------------------------------









    //BEGIN firebase to java conversion ------------------------------------------------------------


    private static User dataSnapshot2User(DataSnapshot dataSnapshot) {
        User user = new User();

        user.setEmail(
                dataSnapshot.child(CHILD_NAME_EMAIL).getValue(String.class)
        );
        user.setUserName(
                dataSnapshot.child(CHILD_NAME_USER_NAME).getValue(String.class)
        );
        user.setFirstName(
                dataSnapshot.child(CHILD_NAME_FIRST_NAME).getValue(String.class)
        );
        user.setLastNameInitial(
                dataSnapshot.child(CHILD_NAME_LAST_NAME_INITIAL).getValue(String.class)
        );
        user.setCo2PledgeAmount(
                null2zero(dataSnapshot.child(CHILD_NAME_CO2_AMOUNT).getValue(Double.class))
        );
        user.setMunicipality(
                dataSnapshot.child(CHILD_NAME_MUNICIPALITY).getValue(String.class)
        );
        user.setPhotoUrl(
                dataSnapshot.child(CHILD_NAME_PHOTO_URL).getValue(String.class)
        );
        user.setMealCalendar(
                dataSnapshot2MealCalendar(dataSnapshot.child(CHILD_NAME_MEAL_CALENDAR))
        );

        return user;
    }


    private static MealCalendar dataSnapshot2MealCalendar(DataSnapshot mealCalendarRef){
        MealCalendar mealCalendar = new MealCalendar();

        for(DataSnapshot dayRef: mealCalendarRef.getChildren())
        {
            mealCalendar.updateDay(dayRef.getKey(),dataSnapshot2Day(dayRef));
        }

        return mealCalendar;
    }



    private  static Day dataSnapshot2Day(DataSnapshot dayRef){
        Day day = new Day();

        for(DataSnapshot mealRef: dayRef.getChildren())
        {
            day.addMeal(dataSnapshot2Meal(mealRef));
        }

        return day;
    }


    private  static Meal dataSnapshot2Meal(DataSnapshot mealRef) {
        Meal meal = new Meal(
                mealRef.getKey(),
                null2zero(mealRef.child(CHILD_NAME_MUNICIPALITY).getValue(Integer.class)),
                mealRef.child(CHILD_NAME_RESTAURANT).getValue(String.class),
                mealRef.child(CHILD_NAME_LOCATION).getValue(String.class),
                mealRef.child(CHILD_NAME_PHOTO_URL).getValue(String.class),
                mealRef.child(CHILD_NAME_DESCRIPTION).getValue(String.class)
        );

        if(mealRef.child(CHILD_NAME_SHARED).getValue(boolean.class) != null) {
            meal.setShared(mealRef.child(CHILD_NAME_SHARED).getValue(boolean.class));
        } else {
            meal.setShared(false);
        }


        for(DataSnapshot componentRef: mealRef.child(CHILD_NAME_COMPONENTS).getChildren()) {
            String proteinName = componentRef.getKey();
            Double servingSize = null2zero(componentRef.getValue(Double.class));

            Protein protein = DefaultProteins.byName(proteinName);

            meal.addComponent(new MealComponent(protein, servingSize));
        }

        return meal;
    }

    //TODO the meal conversions should be working


    //END firebase to java conversion ------------------------------------------------------------








    //BEGIN Helpers---------------------------------------------------------------------------------


    private static DatabaseReference getCurrentUserRef() {
        return getUserRef(currentUserEmailKey);
    }


    private static DatabaseReference getUserRef(String emailKey) {
        return mDatabase.child(CHILD_NAME_USERS).child(emailKey);
    }



    // Since Firebase does not allow symbols as '.' in the key, I enconded the email like this:
    // name@email.com -> name@email,com
    public static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    public static String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }

    //Set null or none user names and last names to anonymous instead
    private static User formatNames(User user) {
        String name = user.getFirstName();

        name = name.equals("none") ? DEFAULT_NAME : name;
        user.setFirstName(name);

        name = user.getLastNameInitial();
        name = name.equals("n") || name.equals("none") ? "" : name;
        user.setLastNameInitial(name);

        return user;
    }


    private static Double null2zero(Double d) {
        if(d==null){
            return new Double(0);
        }
        return d;
    }

    private static Integer null2zero(Integer d) {
        if(d == null) {
            return new Integer(0);
        }
        return d;
    }

    public static void checkUserData(String unmodifiedEmailAddress, String loginUserName) {
        DatabaseReference usersRef = mDatabase.child(CHILD_NAME_USERS);

        usersRef.orderByChild(CHILD_NAME_EMAIL)
                .equalTo(unmodifiedEmailAddress)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // If Data exists
                    refreshCurrentUserQuery();
                } else {
                    // Create a new user's database
                    createUser(unmodifiedEmailAddress, loginUserName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }



    private static void createUser(String unmodifiedUserEmailAddress, String mUsername) {
        // Access to database and set information
        // Set modified email address as a key
        currentUserEmailKey = encodeUserEmail(unmodifiedUserEmailAddress);


        User newUser = new User();
        newUser.setEmail(unmodifiedUserEmailAddress);
        newUser.setUserName(mUsername);


        updateCurrentUserQuery(newUser);

        refreshCurrentUserQuery();
    }

    public static String getcurrentUserEmailKey(){
        return currentUserEmailKey;
    }

    //END Helpers

}
