package ca.sfu.greenfoodchallenge.meal;

import java.util.HashMap;
import java.util.Map;

/*Imports the users current average consumption and carbon emissions over the period ( a week )
and converts to yearly. Offers recommendations on how to improve diet and shows how much CO2e could be saved,
also shows equivalence in easy to understand way.
 */
public class Recommendation {

    public static final int MEAT_EATER_PLAN = 0;
    public static final int BALANCED_PLAN = 1;
    public static final int VEGETARIAN_PLAN = 2;


    public static final int WEEKS_IN_A_YEAR = 52;
    public static final int VANCOUVER_POPULATION = 2530746;
    public static final double VANCOUVER_TOTAL_YEARLY_CARBON = 4700;//kg

    private Period period;
    private Map<String,Double> suggestedPercentages;

    public Recommendation(Period inputPeriod,int plan) {

        period = inputPeriod;
        this.setSuggestedPercentages(plan);
    }

    public double currentYearlyCarbon() {
        return period.totalCarbon() * WEEKS_IN_A_YEAR;
    }


    public double totalYearlyServing(){
        return period.totalServing() * WEEKS_IN_A_YEAR;
    }


    public Map<String, Double> getCurrentPercentages() {
        return period.proteinPercentagesByName();
    }



    public Double[] getSuggestedPercentagesInArray() {
        Double array[] = new Double [7];

        for (int i = 0; i < 7; i++) {
            array[i] = suggestedPercentages.get(DefaultProteins.NAMES[i]);
        }

        return array;
    }

    public Double getPercentageDiff(String proteinName){
        double ret = new Double(0);
        if (getCurrentPercentages().containsKey(proteinName)){
             ret =  nullToZero(getCurrentPercentages().get(proteinName) - this.suggestedPercentages.get(proteinName));
        }

        return ret;
    }

    private void setSuggestedPercentages(int planSwitch) {
        suggestedPercentages = new HashMap<>();

        Double modifier;//used
        switch (planSwitch) {

            case MEAT_EATER_PLAN:
                //SUGGESTED PERCENTAGES FOR "MEAT EATER" PROFILE
                //REDUCES MEAT CONSUMPTION BY 10% AND INCREASES EGGS/BEANS/VEG BY RELATIVE AMOUNT
                //REDUCE MEAT PROTEINS

                modifier = new Double(0.90);
                suggestedPercentages.put(
                        DefaultProteins.NAME_BEEF,
                        nullToZero(getCurrentPercentages().get(DefaultProteins.NAME_BEEF)) * modifier
                );
                suggestedPercentages.put(
                        DefaultProteins.NAME_PORK,
                        nullToZero(getCurrentPercentages().get(DefaultProteins.NAME_PORK)) * modifier
                );
                suggestedPercentages.put(
                        DefaultProteins.NAME_CHICKEN,
                        nullToZero(getCurrentPercentages().get(DefaultProteins.NAME_CHICKEN)) * modifier
                );
                suggestedPercentages.put(
                        DefaultProteins.NAME_FISH,
                        nullToZero(getCurrentPercentages().get(DefaultProteins.NAME_FISH)) * modifier
                );
                //INCREASE VEG + EGGS + BEANS
                suggestedPercentages.put(
                        DefaultProteins.NAME_EGGS,
                        nullToZero(getCurrentPercentages().get(DefaultProteins.NAME_EGGS))
                                + nullToZero(getPercentageDiff(DefaultProteins.NAME_BEEF))
                );
                suggestedPercentages.put(
                        DefaultProteins.NAME_BEANS,
                        nullToZero(getCurrentPercentages().get(DefaultProteins.NAME_BEANS))
                                + nullToZero(getPercentageDiff(DefaultProteins.NAME_PORK))
                );
                suggestedPercentages.put(
                        DefaultProteins.NAME_VEGGIES,
                        nullToZero(getCurrentPercentages().get(DefaultProteins.NAME_VEGGIES))
                                + nullToZero(getPercentageDiff(DefaultProteins.NAME_CHICKEN))
                                + nullToZero(getPercentageDiff(DefaultProteins.NAME_FISH))
                        );
                break;
            case BALANCED_PLAN:
                //SUGGESTED PERCENTAGES FOR "BALANCED" PROFILE
                //REDUCES MEAT CONSUMPTION BY 25% AND INCREASES EGGS/BEANS/VEG BY RELATIVE AMOUNT
                //REDUCE MEAT PROTEINS

                modifier = new Double(0.75);
                suggestedPercentages.put(
                        DefaultProteins.NAME_BEEF,
                        nullToZero(getCurrentPercentages().get(DefaultProteins.NAME_BEEF)) * modifier
                );
                suggestedPercentages.put(
                        DefaultProteins.NAME_PORK,
                        nullToZero(getCurrentPercentages().get(DefaultProteins.NAME_PORK)) * modifier
                );
                suggestedPercentages.put(
                        DefaultProteins.NAME_CHICKEN,
                        nullToZero(getCurrentPercentages().get(DefaultProteins.NAME_CHICKEN)) * modifier
                );
                suggestedPercentages.put(
                        DefaultProteins.NAME_FISH,
                        nullToZero(getCurrentPercentages().get(DefaultProteins.NAME_FISH)) * modifier
                );

                //INCREASE VEG + EGGS + BEANS
                suggestedPercentages.put(
                        DefaultProteins.NAME_EGGS,
                        nullToZero(getCurrentPercentages().get(DefaultProteins.NAME_EGGS))
                                + nullToZero(getPercentageDiff(DefaultProteins.NAME_BEEF))
                );
                suggestedPercentages.put(
                        DefaultProteins.NAME_BEANS,
                        nullToZero(getCurrentPercentages().get(DefaultProteins.NAME_BEANS))
                                + nullToZero(getPercentageDiff(DefaultProteins.NAME_PORK))
                );
                suggestedPercentages.put(
                        DefaultProteins.NAME_VEGGIES,
                        nullToZero(getCurrentPercentages().get(DefaultProteins.NAME_VEGGIES) )
                                + nullToZero(getPercentageDiff(DefaultProteins.NAME_CHICKEN))
                                + nullToZero(getPercentageDiff(DefaultProteins.NAME_FISH))
                );
                break;
            case VEGETARIAN_PLAN:
                //SUGGESTED PERCENTAGES FOR "VEGETARIAN" PROFILE
                //REDUCES MEAT CONSUMPTION BY 100% AND INCREASES EGGS/BEANS/VEG BY RELATIVE AMOUNT
                //REDUCE MEAT PROTEINS
                modifier = new Double(0.0);
                suggestedPercentages.put(
                        DefaultProteins.NAME_BEEF,
                        nullToZero(getCurrentPercentages().get(DefaultProteins.NAME_BEEF)) * modifier
                );
                suggestedPercentages.put(
                        DefaultProteins.NAME_PORK,
                        nullToZero(getCurrentPercentages().get(DefaultProteins.NAME_PORK)) * modifier
                );
                suggestedPercentages.put(
                        DefaultProteins.NAME_CHICKEN,
                        nullToZero(getCurrentPercentages().get(DefaultProteins.NAME_CHICKEN)) * modifier
                );
                suggestedPercentages.put(
                        DefaultProteins.NAME_FISH,
                        nullToZero(getCurrentPercentages().get(DefaultProteins.NAME_FISH)) * modifier
                );
                //INCREASE VEG + EGGS + BEANS
                suggestedPercentages.put(
                        DefaultProteins.NAME_EGGS,
                        nullToZero(suggestedPercentages.get(DefaultProteins.NAME_EGGS))
                                + nullToZero(getPercentageDiff(DefaultProteins.NAME_BEEF))
                );

                suggestedPercentages.put(
                        DefaultProteins.NAME_BEANS,
                        nullToZero(suggestedPercentages.get(DefaultProteins.NAME_BEANS))
                                + nullToZero(getPercentageDiff(DefaultProteins.NAME_PORK))
                );

                suggestedPercentages.put(
                        DefaultProteins.NAME_VEGGIES,
                        nullToZero(suggestedPercentages.get(DefaultProteins.NAME_VEGGIES))
                                + nullToZero(getPercentageDiff(DefaultProteins.NAME_CHICKEN))
                                + nullToZero(getPercentageDiff(DefaultProteins.NAME_FISH)));
                break;
        }
    }

    public Map<String,Double> getSuggestedPercentages(){
        return  suggestedPercentages;
    }


    public Double suggestedYearlyCarbon() {
        Double carbonPerServing = new Double(0);
        for( Map.Entry<String,Double> entry : getSuggestedPercentages().entrySet()){
            Protein protein = DefaultProteins.byName(entry.getKey());
            Double fractionOfDiet = entry.getValue()/100; //convert from percentage

            carbonPerServing += fractionOfDiet * protein.getCarbonPerKg();
        }

        return carbonPerServing * totalYearlyServing();
    }


    public double yearlyCarbonDifference() {
        return currentYearlyCarbon() - suggestedYearlyCarbon();
    }

    public double yearlyCarbonSavedByVancouver(){
        return (yearlyCarbonDifference() * VANCOUVER_POPULATION) /1000; //convert to tonnes
    }



    public static final int EQUIVALENCES_COUNT = 4;
    public static final int KILOMETERS_DRIVEN_EQUIVALENCE = 0;
    public static final int POUNDS_OF_COAL_EQUIVALENCE = 1;
    public static final int HOMES_POWERED_FOR_A_DAY_EQUIVALENCE = 2;
    public static final int TREES_PLANTED_OVER_TEN_YEARS_EQUIVALENCE = 3;



    //Carbon saved by user in a year
    public Double[] getEquivalences() {
        Double constant;

        Double[] equivalences = new Double[EQUIVALENCES_COUNT];

        //Kilometers Driven
        constant = new Double(0.254);
        equivalences[KILOMETERS_DRIVEN_EQUIVALENCE] = yearlyCarbonDifference() / constant;  //kg CO2/km

        //Pounds of coal burned
        constant = new Double(0.914);
        equivalences[POUNDS_OF_COAL_EQUIVALENCE] = yearlyCarbonDifference() / constant; //kg CO2/lb

        //Homes powered for a day
        constant = new Double(6672.0/365.0);
        equivalences[HOMES_POWERED_FOR_A_DAY_EQUIVALENCE] = yearlyCarbonDifference() / constant; //kg CO2/ home for a day

        //Trees planted over 10 yrs
        constant = new Double(39.0);
        equivalences[TREES_PLANTED_OVER_TEN_YEARS_EQUIVALENCE] = yearlyCarbonDifference() / constant; //kg CO2/tree

        return equivalences;
    }





    //Carbon savings if everyone in Vancouver made the same improvement
    public double getEquivalencesVancouver(int i){
        return getEquivalences()[i] * VANCOUVER_POPULATION;
    }





    private static Double nullToZero(Double n){
        if(n==null)
            return new Double(0);
        return n;
    }


}
