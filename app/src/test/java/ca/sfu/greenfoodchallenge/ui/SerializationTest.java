package ca.sfu.greenfoodchallenge.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;

import ca.sfu.greenfoodchallenge.meal.Day;
import ca.sfu.greenfoodchallenge.meal.DefaultProteins;
import ca.sfu.greenfoodchallenge.meal.DummyContent;
import ca.sfu.greenfoodchallenge.meal.MealCalendar;
import ca.sfu.greenfoodchallenge.meal.Protein;

import static junit.framework.Assert.assertEquals;

/*Tests indirectly that serialization od MealCalendar and Protein classes work.
 */
public class SerializationTest {
    MealCalendar mc;
    Day testDay;
    Date date;
    Protein[] testProtein;

    @Before
    public void setData() {
        mc = new MealCalendar();
        testDay = DummyContent.day();
        date = MealCalendar.makeDate(2000,1,1);
        mc.updateDay(date,testDay);
        date = MealCalendar.makeDate(2002,2,2);
        mc.updateDay(date,testDay);
        date = MealCalendar.makeDate(2003,3,3);
        mc.updateDay(2003,3,3,testDay);
        date = MealCalendar.makeDate(2004,4,4);
        mc.updateDay(2004,4,4,testDay);
        date = MealCalendar.makeDate(2005,5,5);
        mc.updateDay(2005,5,5,testDay);
        date = MealCalendar.makeDate(2006,6,6);
        mc.updateDay(2006,6,6,testDay);

        testProtein = DefaultProteins.array();
    }

    @Test
    public void saveMealCalendarData() {

        Gson gson = new GsonBuilder().setDateFormat("EEE MMM dd HH:mm:ss z yyyy").create();

        String json = gson.toJson(mc);

        MealCalendar mcAfter = gson.fromJson(json, MealCalendar.class);

        assertEquals(mc.getDay(2000,1,1).totalCarbon(),
                mcAfter.getDay(2000,1,1).totalCarbon(), 0.0001);
        assertEquals(mc.getDay(2002, 2, 2).totalCarbon(),
                mcAfter.getDay(2002, 2, 2).totalCarbon(), 0.0001);
        assertEquals(mc.getDay(2003,3,3).totalCarbon(),
                mcAfter.getDay(2003,3,3).totalCarbon(), 0.0001);
        assertEquals(mc.getDay(2004,4,4).totalCarbon(),
                mcAfter.getDay(2004,4,4).totalCarbon(), 0.0001);
        assertEquals(mc.getDay(2005,5,5).totalCarbon(),
                mcAfter.getDay(2005,5,5).totalCarbon(), 0.0001);
        assertEquals(mc.getDay(2006,6,6).totalCarbon(),
                mcAfter.getDay(2006,6,6).totalCarbon(), 0.0001);
    }

    @Test
    public void saveProteins() {
        Gson gson = new GsonBuilder().setDateFormat("EEE MMM dd HH:mm:ss z yyyy").create();
        String json = gson.toJson(testProtein);
        Protein[] proteinsAfter = gson.fromJson(json, Protein[].class);

       for(int i = 0; i < testProtein.length; i++) {
           assertEquals(testProtein[i].getCarbonPerKg(), proteinsAfter[i].getCarbonPerKg());
           assertEquals(testProtein[i].getName(), proteinsAfter[i].getName());
       }

    }

    @Ignore
    public Object saveData(Object object, Object objectAfter) {
        Gson gson = new GsonBuilder().setDateFormat("EEE MMM dd HH:mm:ss z yyyy").create();
        String json = gson.toJson(object);

        objectAfter = gson.fromJson(json, Object.class);

        return objectAfter;
    }

    @After
    public void tearDown() {
        date = null;
        mc = null;
        testDay = null;
        testProtein = null;
    }



}
