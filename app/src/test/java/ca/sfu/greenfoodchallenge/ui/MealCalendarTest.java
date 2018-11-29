package ca.sfu.greenfoodchallenge.ui;

import org.junit.Test;

import java.util.Date;

import ca.sfu.greenfoodchallenge.meal.Day;
import ca.sfu.greenfoodchallenge.meal.DummyContent;
import ca.sfu.greenfoodchallenge.meal.MealCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;



public class MealCalendarTest {




    @Test
    public void getDay() {
        MealCalendar mc = new MealCalendar();

        //test a regular day
        Day d = DummyContent.day();
        Date date = MealCalendar.makeDate(2000,1,1);

        mc.updateDay(2000,1,1,d);

        assertEquals(d.getMealCount(),mc.getDay(date).getMealCount());

        //test an empty day
        Date emptyDayDate = MealCalendar.makeDate(2000,1,2);

        Day emptyDay = mc.getDay(emptyDayDate);

        assertEquals(0,emptyDay.getMealCount());

    }

    @Test
    public void getDay1() {

        //only test access

        MealCalendar mc = new MealCalendar();

        Day d = DummyContent.day();

        mc.updateDay(2000,1,1,d);

        assertEquals(d.getMealCount(),mc.getDay(2000,1,1).getMealCount());


    }

    @Test
    public void updateDay() {

        MealCalendar mc = new MealCalendar();

        //  test a regular day
            Day day1 = DummyContent.day();
            Date date1 = MealCalendar.makeDate(2000,1,1);

            mc.updateDay(date1,day1);

            assertEquals(day1.getMealCount(),mc.getDay(date1).getMealCount());
            assertFalse(day1.equals(mc.getDay(date1)));

        // add a second day
            Day day2 = DummyContent.day();
            Date date2 = MealCalendar.makeDate(2000,1,2);

            mc.updateDay(date2,day2);

            assertEquals(day2.getMealCount(),mc.getDay(date2).getMealCount());
            assertFalse(day1.equals(mc.getDay(date1)));


        //  modify an existing day
            Day modifiedDay1 = mc.getDay(date1);
            int oldMealCount = modifiedDay1.getMealCount();

            modifiedDay1.addMeal(DummyContent.meal("snack"));

            //check that the reference diddn't modify the day
            assertEquals(oldMealCount,mc.getDay(date1).getMealCount());

            mc.updateDay(date1,modifiedDay1);

            //check that the modification applied...
            assertEquals(oldMealCount+1,mc.getDay(date1).getMealCount());


            //but not to the original
            assertEquals(oldMealCount,day1.getMealCount());

        //  replace an existing day

            Day newDay = DummyContent.day();
            newDay.addMeal(DummyContent.meal("snack1"));
            newDay.addMeal(DummyContent.meal("snack2"));

            Day replacedDay = mc.getDay(date2);
            int replacedDayMealCount = replacedDay.getMealCount();

            mc.updateDay(date2,newDay);

            assertEquals(replacedDayMealCount+2, mc.getDay(date2).getMealCount());

    }

    @Test
    public void updateDay1() {

        // Here we only check that the access works, since this is just a forwarding of the other.

        MealCalendar mc = new MealCalendar();

        Day day = DummyContent.day();
        int mealCount = day.getMealCount();

        mc.updateDay(2000,1,1,day);

        assertEquals(mealCount,mc.getDay(2000,1,1).getMealCount());
    }


    public void getLastNDays()
    {

    }


}