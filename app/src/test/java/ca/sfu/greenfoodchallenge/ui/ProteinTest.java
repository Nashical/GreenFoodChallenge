package ca.sfu.greenfoodchallenge.ui;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ca.sfu.greenfoodchallenge.meal.Protein;

/*Tests the Protein class
 */
public class ProteinTest {

    Protein testProtein;
    @Before
    public void initializer() {
        testProtein = new Protein("Chicken", 6.9, "testy chick");
    }

    @Test
    public void getNameTest() {
        assertEquals("Chicken", testProtein.getName());
    }

    @Test
    public void setProteinNameTest() {
        testProtein.setProteinName("Beef");
        assertEquals("Beef", testProtein.getName());
    }

    @Test
    public void setProteinNameTestFails() {
        testProtein.setProteinName("");
        assertEquals("Chicken", testProtein.getName());
    }

    @Test
    public void getCarbonPerKgTest() {
        assertEquals(6.9, testProtein.getCarbonPerKg(), 0.001);
    }

    @Test
    public void setCarbonPerKgTest() {
        testProtein.setCarbonPerKg(7.0);
        assertEquals(7.0, testProtein.getCarbonPerKg(), 0.001);
    }

    @Test
    public void setCarbonPerKgTestFail() {
        testProtein.setCarbonPerKg(-1);
        assertEquals(10, testProtein.getCarbonPerKg(), 0.0001);
    }

    @Test
    public void getDescriptionTest() {
        assertEquals("testy chick", testProtein.getDescription());
    }

    @Test
    public void setDescriptionTest() {
        testProtein.setDescription("a healthier choice");
        assertEquals("a healthier choice", testProtein.getDescription());
    }
}