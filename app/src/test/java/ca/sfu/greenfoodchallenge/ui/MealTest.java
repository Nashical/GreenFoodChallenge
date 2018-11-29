package ca.sfu.greenfoodchallenge.ui;


import org.junit.Test;

import java.util.List;

import org.junit.Before;

import java.util.Map;


import ca.sfu.greenfoodchallenge.meal.Meal;
import ca.sfu.greenfoodchallenge.meal.MealComponent;
import ca.sfu.greenfoodchallenge.meal.Protein;

import static org.junit.Assert.*;

public class MealTest {

    Meal testMeal;


    @Before
    public void setUp() {


        MealComponent testComponent;
        Protein testProtein1;
        Protein testProtein2;
        Protein testProtein3;


        testProtein1 = new Protein("Chicken", 1, "");
        testProtein2 = new Protein("Beef", 2, "");
        testProtein3 = new Protein("Beans", 3, "");



        testMeal = new Meal("Meal1");
        testComponent = new MealComponent(testProtein1, 1);
        testMeal.addComponent(testComponent);
        testComponent = new MealComponent(testProtein2, 10);
        testMeal.addComponent(testComponent);
        testComponent = new MealComponent(testProtein3, 100);
        testMeal.addComponent(testComponent);
    }


    @Test
    public void getMealName() {
        assertEquals("Meal1",testMeal.getMealName());
    }

    @Test
    public void setMealName() {
        testMeal.setMealName("Breakfast");
        assertEquals("Breakfast",testMeal.getMealName());
    }

    @Test
    public void getCarbon(){

        assertEquals(321,testMeal.getCarbon(),0.00001);

    }

    @Test
    public void getComponentList() {

        List<MealComponent> componentList = testMeal.getComponentList();

    }

    @Test
    public void addComponent() {
        testMeal.addComponent(new MealComponent(
                new Protein("zeta",1,"description"),
                10
        ));

        List<MealComponent>  componentList = testMeal.getComponentList();

        assertEquals("zeta",componentList.get(3).getComponentProtein().getName());

    }

    @Test
    public void removeComponent() {

        testMeal.addComponent(new MealComponent(
                new Protein("zeta",0.2,"description"),
                5
        ));


        testMeal.removeComponent(2);


        List<MealComponent>  componentList = testMeal.getComponentList();


        assertEquals("Chicken",componentList.get(0).getComponentProtein().getName());
        assertEquals("Beef",componentList.get(1).getComponentProtein().getName());
        assertEquals("zeta",componentList.get(2).getComponentProtein().getName());


    }

    @Test
    public void getComponentCount() {
        assertEquals(3, testMeal.getComponentCount());

    }




    @Test
    public void getComponentByProteinName() {
        MealComponent component = testMeal.getComponentByProteinName("Beef");

        assertEquals("Beef",component.getComponentProtein().getName());
    }


    @Test
    public void getKilogramsByProteinName() {
        assertEquals(1.0,testMeal.getKilogramsByProteinName("Chicken"),0.0001);
    }


    @Test
    public void getGramsByProteinName() {
        assertEquals(1000.0,testMeal.getGramsByProteinName("Chicken"),0.0001);
    }

    @Test
    public void proteinAmountsByName() {

        Map<String,Double> map = testMeal.proteinAmountsByName();

        assertEquals((Double) 1.0,map.get("Chicken"),0.0001);
        assertEquals((Double)10.0,map.get("Beef"),0.0001);
        assertEquals((Double)100.0,map.get("Beans"),0.0001);
    }

    @Test
    public void totalServing() {
        assertEquals(111.0,testMeal.totalServing(),0.0001);
    }
}