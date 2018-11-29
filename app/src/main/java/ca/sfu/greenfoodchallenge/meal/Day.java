package ca.sfu.greenfoodchallenge.meal;

import java.util.Map;
import java.util.TreeMap;

/*The Day class contains all Meal objects from a single day and calculates total CO2e per day's meals.
 */
public class Day {
    private Map<String,Meal> mealList;

    // empty day
    public Day() {
        this.mealList = new TreeMap<>();
    }

    public Day(Day other){
        mealList = new TreeMap<>();
        for (Map.Entry mealEntry : other.mealList.entrySet()){
            mealList.put((String)mealEntry.getKey(), new Meal((Meal) mealEntry.getValue()));
        }
    }

    public void addMeal(Meal newMeal) {
        mealList.put(newMeal.getMealName()!=null?newMeal.getMealName():"meal", newMeal);
    }

    /*Updates an existing meal, or creates a new meal if the meal name is not found
     */
    public void setMeal(Meal newMeal) {
        addMeal(newMeal);
    }

    public Meal getMeal(String mealName) {
        Meal returnMeal;

        if(mealList.get(mealName) != null) {
            returnMeal = mealList.get(mealName);
        } else {
            returnMeal = new Meal("");
        }

        return new Meal(returnMeal);
    }

    public void removeMeal(String mealName) {
        Meal returnMeal;

        if(mealList.get(mealName) != null) {
            mealList.remove(mealName);
        }
    }

    public Map<String, Meal> getMealList() {
        return mealList;
    }

    public double totalCarbon() {
        double totalCarbon = 0;
        double mealCarbon;

        //iterate over meals to get total carbon
        for(Map.Entry<String, Meal> entry : mealList.entrySet()) {
            mealCarbon = entry.getValue().getCarbon();
            totalCarbon = totalCarbon + mealCarbon;
        }

        return totalCarbon;
    }

    //for debug
    public String show()
    {
        String out = "";

        //dummy code waiting for the UI
        for(Map.Entry<String,Meal> mealEntry : this.getMealList().entrySet()){
            out += mealEntry.getKey() + ":\n";
            for (MealComponent component :mealEntry.getValue().getComponentList())
            {
                out += "\t";
                out += component.getComponentProtein().getName();
                out += " " + component.getServingSize() + "\n";
            }
        }

        return out;
    }




    public Map<String, Double> proteinAmountsByName() {
        Map<String, Double> totalProteinAmountsByName = new TreeMap<>();

        //iterate over every meal & make sure to add protein keys
        for (String s : mealList.keySet()) {
            Map<String, Double> mealProteinAmountsByName = mealList.get(s).proteinAmountsByName();


            for (String proteinName : mealProteinAmountsByName.keySet()) {
                Double serving = mealProteinAmountsByName.get(proteinName);

                //Update serving sizes for existing proteins
                if (totalProteinAmountsByName.containsKey(proteinName)) {
                    serving += totalProteinAmountsByName.get(proteinName);
                    totalProteinAmountsByName.put(proteinName, serving);
                } else {
                    totalProteinAmountsByName.put(proteinName, serving);
                }
            }
        }
        return totalProteinAmountsByName;
    }


    /*
     * total grams of food consumed
     */
    public double totalServing()
    {
        double totalServing = 0;
        for (Meal meal: mealList.values()) {
            totalServing += meal.totalServing();
        }
        return totalServing;
    }


    public int getMealCount() {
        return mealList.size();
    }

}