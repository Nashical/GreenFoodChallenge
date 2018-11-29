package ca.sfu.greenfoodchallenge.database;


import java.util.List;

import ca.sfu.greenfoodchallenge.meal.MealCalendar;

public class User {
    public static final String NONE = "none";
    public static final double DEFAULT_CO2_NUMBER = 0.0;
    public static final int EQUIVALENCES_COUNT = 4;
    public static final int KILOMETERS_DRIVEN_EQUIVALENCE = 0;
    public static final int POUNDS_OF_COAL_EQUIVALENCE = 1;
    public static final int HOMES_POWERED_FOR_A_DAY_EQUIVALENCE = 2;
    public static final int TREES_PLANTED_OVER_TEN_YEARS_EQUIVALENCE = 3;



    private String email;
    private String firstName; // In profile page, user can input first name.
    private String lastNameInitial; // In profile page, user can input last name's initial.
    private String userName;  // automatically get whole user's name when user sign up an account.
    private String municipality;
    private MealCalendar mealCalendar = null;

    private double co2PledgeAmount;
    private String photoUrl; // I don't know how we control profile pictures, so I just made a string variable.
                                // Feel free to fix it.
    public User() {
        email = NONE;
        firstName = NONE;
        lastNameInitial = NONE;
        userName = NONE;
        municipality = NONE;
        co2PledgeAmount = DEFAULT_CO2_NUMBER;
        photoUrl = NONE;
    }

    public User(String email, String userName, String municipality, double co2PledgeAmount,
                String photoUrl) {

        this.email = email;
        this.userName = userName;
        this.municipality = municipality;
        this.co2PledgeAmount = co2PledgeAmount;
        this.photoUrl = photoUrl;
    }


    public static double totalPledgeCarbon(List<User> users){
        double total = 0;
        for (User user : users) {
            total += user.getCo2PledgeAmount();
        }
        return total;
    }


    public static int totalPledges(List<User> users) {
        int total = users.size();
        return total;
    }

    public static double averagePledgeCarbon(List<User> users){
        double average = 0;
        double numUsers = (double) users.size();
        average = (totalPledgeCarbon(users) / numUsers);
        return average;
    }

    public static double[] pledgeEquivalence(double totalCarbon){
        double constant;
        double[] equivalences = new double[EQUIVALENCES_COUNT];


        //Kilometers Driven
        constant = 0.254;
        equivalences[KILOMETERS_DRIVEN_EQUIVALENCE] =  totalCarbon / constant;  //kg CO2/km

        //Pounds of coal burned
        constant = 0.914;
        equivalences[POUNDS_OF_COAL_EQUIVALENCE] = totalCarbon / constant; //kg CO2/lb

        //Homes powered for a day
        constant = 6672.0/365.0;
        equivalences[HOMES_POWERED_FOR_A_DAY_EQUIVALENCE] = totalCarbon / constant; //kg CO2/ home for a day

        //Trees planted over 10 yrs
        constant = 39.0;
        equivalences[TREES_PLANTED_OVER_TEN_YEARS_EQUIVALENCE] = totalCarbon / constant; //kg CO2/tree

        return equivalences;
    }






    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }


    public double getCo2PledgeAmount() {
        return co2PledgeAmount;
    }

    public void setCo2PledgeAmount(double co2PledgeAmount) {
        this.co2PledgeAmount = co2PledgeAmount;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastNameInitial() {
        return lastNameInitial;
    }

    public void setLastNameInitial(String lastNameInitial) {
        this.lastNameInitial = lastNameInitial;
    }

    public MealCalendar getMealCalendar() {
        return mealCalendar;
    }

    public void setMealCalendar(MealCalendar mealCalendar) {
        this.mealCalendar = mealCalendar;
    }
}
