package ca.sfu.greenfoodchallenge.ui;

import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TreeMap;


import ca.sfu.greenfoodchallenge.meal.Day;
import ca.sfu.greenfoodchallenge.meal.DefaultProteins;
import ca.sfu.greenfoodchallenge.meal.DummyContent;
import ca.sfu.greenfoodchallenge.meal.Meal;
import ca.sfu.greenfoodchallenge.meal.MealCalendar;
import ca.sfu.greenfoodchallenge.meal.MealComponent;
import ca.sfu.greenfoodchallenge.meal.Period;
import ca.sfu.greenfoodchallenge.meal.Protein;
import ca.sfu.greenfoodchallenge.meal.Recommendation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RecommendationTest {

    Period testPeriod;
    MealCalendar testCalendar;
    Day testDay;
    Recommendation testRec;
    double totalWeekCarbon;

    @Before
    public void setUp() {

        testCalendar = new MealCalendar();
        Calendar calendar = GregorianCalendar.getInstance();

        for (int i = 0; i < 10; i++) {
            Date date = calendar.getTime();
            testDay = new Day();
            Meal testMeal = new Meal("meal of day " + i);
            testMeal.addComponent(
                    new MealComponent(
                            new Protein("p1", 0.5, ""),
                            Math.pow(2, i)
                    )
            );
            testDay.addMeal(testMeal);
            testCalendar.updateDay(date, testDay);
            calendar.add(Calendar.DATE, -1);
        }


        testPeriod = new Period(testCalendar);
        testRec = new Recommendation(testPeriod, Recommendation.BALANCED_PLAN);


        //The totalCarbon method returns a 7 days of total carbon
        totalWeekCarbon = testPeriod.totalCarbon();

    }

    @Test
    public void currentYearlyCarbon() {
        assertEquals(totalWeekCarbon * 52.0, testRec.currentYearlyCarbon(), 0.0001);
    }

    @Test
    public void totalYearlyServing() {
        assertEquals(testPeriod.totalServing() * 52.0, testRec.totalYearlyServing(), 0.0001);
    }



    @Test
    public void getCurrentPercentages() {
        Map<String, Double> testPercentages = new TreeMap<>();
        testPercentages.putAll(testRec.getCurrentPercentages());
        assertEquals(testPeriod.proteinPercentagesByName(), testPercentages);
    }


    @Test
    public void getSuggestedPercentagesInArray() {
        Map<String, Double> testPercentages = testRec.getSuggestedPercentages();
        Double array[] = testRec.getSuggestedPercentagesInArray();
        for (int i = 0; i < array.length; i++){
            assertEquals(testPercentages.get(DefaultProteins.NAMES[i]), array[i], 0.0001);
        }
    }


    @Test
    public void getPercentageDiff() {
        Map<String, Double> currPercentages = testRec.getCurrentPercentages();
        Map<String, Double> suggPercentages = testRec.getSuggestedPercentages();

        for(String i : currPercentages.keySet()) {
            if(currPercentages.containsKey(i) && suggPercentages.containsKey(i)) {
                Double diff = currPercentages.get(i) - suggPercentages.get(i);
                assertEquals(diff, testRec.getPercentageDiff(i));
            }
        }
    }


    @Test
    public void setSuggestedPercentages() {
        testRec = new Recommendation(testPeriod, Recommendation.VEGETARIAN_PLAN);
        Map<String, Double> suggPercentages = testRec.getSuggestedPercentages();
        Map<String, Double> currPercentages = testRec.getCurrentPercentages();
        Double[] array = testRec.getSuggestedPercentagesInArray();
        int i = 0;

        for(Double val : suggPercentages.values()) {
            assertEquals(array[i], val);
            i++;
        }
        i = 0;
        for(Double val : currPercentages.values()) {
            assertNotEquals(array[i], val);
            i++;
        }
    }

    //Tested in setSuggestedPercentages and getSuggestedPercentagesInArray
    @Test
    public void getSuggestedPercentages() {
    }

    @Test
    public void suggestedYearlyCarbon() {

    }

    @Test
    public void yearlyCarbonDifference() {
        Double curr = testRec.currentYearlyCarbon();
        Double sugg = testRec.suggestedYearlyCarbon();
        assertEquals(curr - sugg, testRec.yearlyCarbonDifference(), 0.001);

    }

    @Test
    public void yearlyCarbonSavedByVancouver() {

    }

    @Test
    public void getEquivalences() {

    }

    @Test
    public void getEquivalencesVancouver() {

    }
    
    @After
    public void tearDown(){
        testCalendar = null;
        testPeriod = null;
        testRec = null;
        testDay = null;
    }


}

