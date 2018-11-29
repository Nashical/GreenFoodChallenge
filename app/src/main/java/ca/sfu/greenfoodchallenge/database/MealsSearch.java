package ca.sfu.greenfoodchallenge.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import ca.sfu.greenfoodchallenge.meal.Day;
import ca.sfu.greenfoodchallenge.meal.Meal;
import ca.sfu.greenfoodchallenge.meal.Protein;

/*
 * A collections of tools to search through meals.
 */
public class MealsSearch {

    // From last to first, all shared meals
    List<Meal> allMeals;


    /*
     * Accepts a list of users such as the one returned by GreenFoodChallengeDatabase.getAllUsers()
     */
    public MealsSearch(List<User> users){


        SortedMap<String,ArrayList<Meal>> mealsByDay = new TreeMap<>();


        for (User user:users) {
            for(Map.Entry<String,Day> day: user.getMealCalendar().getDefinedDays().entrySet()){
                for(Meal meal: day.getValue().getMealList().values()){
                    if(meal.isShared()) {
                        if (!mealsByDay.containsKey(day.getKey())) {
                            mealsByDay.put(day.getKey(), new ArrayList<>());
                        }
                        mealsByDay.get(day.getKey()).add(meal);
                    }
                }
            }
        }

        allMeals = new ArrayList<>();

        for(Map.Entry<String,ArrayList<Meal>> day: mealsByDay.entrySet()){
            for(Meal meal: day.getValue()){
                allMeals.add(meal);
            }
        }


        Collections.reverse(allMeals);
    }

    public List<Meal> byNoFilter() {
        return allMeals;
    }

    public List<Meal> byMainProtein(Protein protein){
        ArrayList<Meal>matching = new ArrayList<>();

        for(Meal meal: allMeals){
            if(meal.getMainProtein()!= null &&
                    meal.getMainProtein().getName().equals(protein.getName())){
                matching.add(meal);
            }

        }

        return matching;
    }

    public List<Meal> byMainProtein(String proteinName){
        ArrayList<Meal>matching = new ArrayList<>();

        for(Meal meal: allMeals){
            if(meal.getMainProtein()!= null &&
                    meal.getMainProtein().getName().equals(proteinName)){
                matching.add(meal);
            }

        }

        return matching;
    }


    public List<Meal> byMunicipality(int municipality){
        ArrayList<Meal>matching = new ArrayList<>();

        for(Meal meal: allMeals){
            if(meal.getMunicipality() == municipality){
                matching.add(meal);
            }

        }
        return matching;
    }


    /*
     * Search through all textual parameters of a meal. (including municipality)
     */
    public List<Meal> fullText(String query) {
        ArrayList<Meal> matching = new ArrayList<>();

        for (Meal meal: allMeals){
            StringBuilder text = new StringBuilder();
            text.append(meal.getMealName());
            text.append(" ");
            text.append(Municipality.LIST[meal.getMunicipality()]);
            text.append(" ");
            text.append(meal.getRestaurant());
            text.append(" ");
            text.append(meal.getLocation());
            text.append(" ");
            text.append(meal.getDescription());

            if(text.indexOf(query)>=0)
            {
                matching.add(meal);
            }
        }

        return matching;
    }


    public int getSharedMealsCount(){
        return allMeals.size();
    }
}
