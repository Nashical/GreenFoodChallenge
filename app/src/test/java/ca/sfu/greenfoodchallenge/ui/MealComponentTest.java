package ca.sfu.greenfoodchallenge.ui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.sfu.greenfoodchallenge.meal.MealComponent;
import ca.sfu.greenfoodchallenge.meal.Protein;

import static org.junit.Assert.*;

public class MealComponentTest {
    Protein testProtein;
    Protein testProtein2;
    MealComponent testComponent;

    @Before
    public void initialize()
    {
        testProtein = new Protein("Beef", 4.0, "testDescription");
        testProtein2 = new Protein("Chicken", 2.0, "testDescription2");
        testComponent = new MealComponent(testProtein, 40.0);
    }

    @Test
    public void getComponentProtein() {
        assertEquals(testProtein, testComponent.getComponentProtein());
    }

    @Test
    public void setComponentProtein() {
        testComponent.setComponentProtein(testProtein2);
        assertEquals(testProtein2, testComponent.getComponentProtein());
    }

    @Test
    public void getServingSize() {
        assertEquals(40.0, testComponent.getServingSize(), 1.0);
    }

    @Test
    public void setServingSize() {
        testComponent.setServingSize(30.0);
        assertEquals(30.0, testComponent.getServingSize(), 1.0);
    }

    @Test
    public void getCarbon() {
        assertEquals(160.0, testComponent.getCarbon(), 1.0);
    }
    @After
    public void teardown(){
        testProtein = null;
        testProtein2 = null;
        testComponent = null;
    }

}