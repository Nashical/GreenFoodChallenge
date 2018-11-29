package ca.sfu.greenfoodchallenge.meal;

import ca.sfu.greenfoodchallenge.database.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/*
 * Generate random content to test the UI.
 */
public class DummyContent {
    final static String DEFAULT_MEAL_NAME = "";
    final static String NAME_BREAKFAST = "Breakfast";
    final static String NAME_LUNCH = "Lunch";
    final static String NAME_DINNER = "Dinner";


    static MealCalendar mealCalendar(){
        MealCalendar c = new MealCalendar();

        return c;
    }

    public static MealCalendar defaultCalender(Date date) {
        Day d = new Day();
        Meal meal = new Meal(DEFAULT_MEAL_NAME);
        Protein[] proteins = DefaultProteins.array();

        for(int i = 0; i < proteins.length; i++) {
            meal.addComponent(new MealComponent(proteins[0], 0));
        }

        d.addMeal(meal);
        MealCalendar mc = new MealCalendar();
        mc.updateDay(date, d);

        return mc;

    }

    public static Day day() {
        Day d = new Day();
        d.addMeal(DummyContent.meal(NAME_BREAKFAST));
        d.addMeal(DummyContent.meal(NAME_LUNCH));
        d.addMeal(DummyContent.meal(NAME_DINNER));

        return d;
    }


    public static Meal meal(String name){
        Random randomGenerator = new Random();

        Meal meal = new Meal(name);

        for (int i = 0; i <  2 + randomGenerator.nextInt(3); i++) {
            meal.addComponent(DummyContent.mealComponent());
        }

        return meal;
    }

    static Meal nonRandomMeal(String name) {
        Meal meal = new Meal(name);
        Protein[] proteins = DefaultProteins.array();
        MealComponent component;

        for(int i = 0; i < proteins.length; i++) {
            component = new MealComponent(proteins[i], 10);
            meal.addComponent(component);
        }

        return meal;
    }


    public static Day nonRandomDay() {
        Day d = new Day();
        d.addMeal(DummyContent.nonRandomMeal(NAME_BREAKFAST));
        d.addMeal(DummyContent.nonRandomMeal(NAME_LUNCH));
        d.addMeal(DummyContent.nonRandomMeal(NAME_DINNER));

        return d;
    }

    static MealComponent mealComponent(){
        Random randomGenerator = new Random();

        Protein[] defaultProteins = DefaultProteins.array();

        MealComponent mc = new MealComponent(
                defaultProteins[randomGenerator.nextInt(defaultProteins.length)],
                0.2 + randomGenerator.nextDouble() * 0.2
        );


        return mc;
    }

    static User[] user(String[] inputArray, int num) {
        String[] municipalities = inputArray;
        User[] users = new User[num];
        String email;
        String userName;
        String firstName;
        String lastNameInitial;
        String municipality;
        String photoUrl;
        User user;

        for(int i = 0; i < 22; i++) {
            email = i + "@" + i + ".com";
            firstName = "Name" + i;
            lastNameInitial = "X";
            userName = firstName + "_" + lastNameInitial;
            municipality = municipalities[i + 1];
            photoUrl = "android.resource://ca.sfu.greenfoodchallenge.ui/R.mipmap.ic_launcher_round";
            user = new User(email, userName, municipality, i, photoUrl);
            user.setFirstName(firstName);
            user.setLastNameInitial(lastNameInitial);
            user.setCo2PledgeAmount(i);
            users[i] = user;
        }

        return users;
    }

    static User user() {

        User user = new User();
        user.setPhotoUrl("android.resource://ca.sfu.greenfoodchallenge.ui/R.mipmap.ic_app_round");
        user.setCo2PledgeAmount(45000);
        user.setFirstName("John");
        user.setLastNameInitial("D");
        user.setEmail("j.d@email.com");
        user.setUserName("John D");
        return user;
    }


    //Test cases with currently loaded images in GFC database
    public static List<Meal> mealList() {
        List<Meal> meals = new ArrayList<>();

        String[] urls = {"https://firebasestorage.googleapis.com/v0/b/greenfoodchallenge-53c54.appspot.com/o/uploads%2F1542597554380.jpg?alt=media&token=ff9502c0-acdf-4000-8342-2de5fa74bfcb",
                "https://firebasestorage.googleapis.com/v0/b/greenfoodchallenge-53c54.appspot.com/o/uploads%2F1542536548732.jpg?alt=media&token=5ad8f8fa-1d97-43d4-a273-fef248296260",
                "https://firebasestorage.googleapis.com/v0/b/greenfoodchallenge-53c54.appspot.com/o/uploads%2F1542597556747.jpg?alt=media&token=60da2785-2bd3-48f9-af30-1d03488817fb",
                "https://firebasestorage.googleapis.com/v0/b/greenfoodchallenge-53c54.appspot.com/o/uploads%2F1542597598253.jpg?alt=media&token=e2a68a5f-557c-4199-b8bf-6581b7fa1539",
                "https://firebasestorage.googleapis.com/v0/b/greenfoodchallenge-53c54.appspot.com/o/uploads%2F1542597598253.jpg?alt=media&token=e2a68a5f-557c-4199-b8bf-6581b7fa1539",
                "https://firebasestorage.googleapis.com/v0/b/greenfoodchallenge-53c54.appspot.com/o/uploads%2F1542758438813.jpg?alt=media&token=b0361f7b-a62b-4ea0-aa9e-cf8da2bdc3b5"};
        Random random = new Random();

        for(int i = 0; i < 3; i++) {
            Meal meal = new Meal("meal" + i);
            meal.addComponent(mealComponent());
            meal.setShared(true);
            meal.setMunicipality(random.nextInt(23));
            meal.setImageUrl(urls[random.nextInt(5)]);
            meals.add(meal);
        }

        return meals;
    }

    public static MealCalendar testingCalender(Date date) {
        Day d = new Day();

        for(Meal meal : mealList()) {
            d.addMeal(meal);
        }

        MealCalendar mc = new MealCalendar();
        mc.updateDay(date, d);

        return mc;

    }

    public static List<User> userList() {
        List<User> users = new ArrayList<>();

        for(int i = 0; i < 3; i++) {
            User user = user();
            user.setMealCalendar(testingCalender(new Date(2018, 10, 24 + i)));
            users.add(user);
        }

        return users;
    }
}
