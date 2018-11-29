package ca.sfu.greenfoodchallenge.ui;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import ca.sfu.greenfoodchallenge.meal.Day;
import ca.sfu.greenfoodchallenge.meal.DummyContent;
import ca.sfu.greenfoodchallenge.meal.Meal;
import ca.sfu.greenfoodchallenge.meal.MealComponent;
import ca.sfu.greenfoodchallenge.meal.Protein;

import static org.junit.Assert.assertEquals;


/*DayTest tests the Day class which models all the meals consumed in a day.
 */
public class DayTest {

    Day testDay = new Day();



    @Test
    public void setMeal() {
        Meal testMeal = new Meal("yummyTest1");
        testMeal.addComponent(new MealComponent(new Protein("Beef",1,""), 300));
        testDay.setMeal(testMeal);
        assertEquals(3, testDay.getMealCount());
    }



    @Test
    public void getMeal() {
        Meal testMeal = new Meal("yummyTest1");
        testMeal.addComponent(new MealComponent(new Protein("Beef",1,""), 300));
        testDay.setMeal(testMeal);

        Meal returnedMeal = testDay.getMeal("yummyTest1");
        assertEquals("yummyTest1", returnedMeal.getMealName());
    }

    @Test
    public void getMealFail() {
       Meal testMeal = testDay.getMeal("yummyFail");
       assertEquals("", testMeal.getMealName());
    }


    @Test
    public void removeMealFail() {
        int mealCount = testDay.getMealCount();
        testDay.removeMeal("");
        assertEquals(mealCount, testDay.getMealCount());

    }

    @Test
    public void getCarbon() {

        assertEquals(54321.0, testDay.totalCarbon(), 0.0001);

        Meal testMeal = new Meal("yummyTest1");
        MealComponent testComponent = new MealComponent(new Protein("Chicken",6,""), 100000);
        testMeal.addComponent(testComponent);
        testDay.addMeal(testMeal);

        assertEquals(654321.0, testDay.totalCarbon(), 0.0001);


    }


    @Test
    public void removeMeal() {
        Day testDay = new Day();
        assertEquals(0, testDay.getMealCount());
        testDay.removeMeal("Lunch");
        assertEquals(0, testDay.getMealCount());
        testDay.addMeal(DummyContent.meal("Lunch"));
        assertEquals(1, testDay.getMealCount());
        testDay.removeMeal("Lunch");
        assertEquals(0, testDay.getMealCount());

    }



    //\todo{update the methods above to use this, and then move them below}
    @Before
    public void setUp(){
        Meal testMeal1 = new Meal("lunch");
        testMeal1.addComponent(new MealComponent(new Protein("Beef", 1,""), 1));
        testMeal1.addComponent(new MealComponent(new Protein("Chicken", 2,""), 10));
        testMeal1.addComponent(new MealComponent(new Protein("Beans", 3,""), 100));
        testDay.addMeal(testMeal1);
        Meal testMeal2 = new Meal("dinner");
        testMeal2.addComponent(new MealComponent(new Protein("Beef", 4,""), 1000));
        testMeal2.addComponent(new MealComponent(new Protein("Pork", 5,""), 10000));
        testDay.addMeal(testMeal2);
    }


    @Test
    public void addMeal() {
        assertEquals(2, testDay.getMealCount());

        testDay.addMeal(new Meal("yummyTest1"));
        assertEquals(3, testDay.getMealCount());
    }



    @Test
    public void proteinAmountsByName() {

        Map<String, Double> testEquivalence = testDay.proteinAmountsByName();
        assertEquals(new Double(1001), testEquivalence.get("Beef"),0.001);
        assertEquals(new Double(10), testEquivalence.get("Chicken"),0.001);
        assertEquals(new Double(10000), testEquivalence.get("Pork"),0.001);

    }


    @Test
    public void getMealList() {
        Map<String,Meal> mealList = testDay.getMealList();

        assertEquals(3,mealList.get("lunch").getComponentCount());
        assertEquals(2,mealList.get("dinner").getComponentCount());
    }


    @Test
    public void totalServing() {
        assertEquals(11111.0,testDay.totalServing(),0.001);
    }

    @Test
    public void getMealCount() {
        assertEquals(2,testDay.getMealCount());
    }
}