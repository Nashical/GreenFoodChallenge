package ca.sfu.greenfoodchallenge.meal;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/*Period class subsets MealCalendar so carbon emission calculations can be performed for a week.
 */
public class Period {

    public static final int DEFAULT_PERIOD_LENGTH = 7;



    private SortedMap<Date, Day> periodDays;


    public Period( MealCalendar newCalendar) {
        periodDays = newCalendar.getLastNDays(DEFAULT_PERIOD_LENGTH);

    }


    /*Return total carbon emission per kilogram for the given time period.
     */
    public double totalCarbon() {
        double totalCarbon = 0;

        for(Map.Entry<Date, Day> i : periodDays.entrySet()) {
            totalCarbon += i.getValue().totalCarbon();
        }

        return totalCarbon;
    }



    public Map<String, Double> proteinAmountsByName() {
        Map<String, Double> proteinEquivalence = new TreeMap<>();
        Map<String, Double> dailyAmountsByName;


        for(Date d : periodDays.keySet()) {
            Day day = periodDays.get(d);
            dailyAmountsByName = day.proteinAmountsByName();

            for(String proteinName : dailyAmountsByName.keySet()) {
                Double serving = dailyAmountsByName.get(proteinName);

                if(proteinEquivalence.containsKey(proteinName)) {
                    serving += proteinEquivalence.get(proteinName);
                    proteinEquivalence.put(proteinName, serving);
                } else {
                    proteinEquivalence.put(proteinName, serving);
                }
            }
        }

        return  proteinEquivalence;
    }





    //Returns the fraction of each protein as a percent.
    public Map<String, Double> proteinPercentagesByName() {

        Map<String, Double> proteinPercentageByName = new TreeMap<String,Double>();


        //Return each serving as a percent
        for(Map.Entry<String,Double> entry: proteinAmountsByName().entrySet()) {
            String proteinName = entry.getKey();
            Double amountOfProtein = entry.getValue();

            //Get percent of totalServing for the specific protein type

            proteinPercentageByName.put(proteinName, amountOfProtein / totalServing() * 100);
        }

        return proteinPercentageByName;
    }



     //total grams of food consumed
    public Double totalServing() {
        Double totalServing = new Double(0);
        for (Day day: periodDays.values()) {
            totalServing += day.totalServing();
        }

        return totalServing;
    }


    public int getPeriodSize() {
        return periodDays.size();
    }

    /*Returns a reverse chronologically ordered map
     */
    public SortedMap<Date, Day> getPeriodDays() {
        return periodDays;
    }

    /*Returns the latest date in the set.
     */
    public Date getPeriodEndDate() {
        return periodDays.lastKey();
    }

    /*Returns the earliest date in the set.
     */
    public Date getPeriodStartDate() {
        return periodDays.firstKey();
    }

    public void update(MealCalendar mealCalendar) {
        periodDays = mealCalendar.getLastNDays(getPeriodSize());
    }




}
