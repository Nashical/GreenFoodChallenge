package ca.sfu.greenfoodchallenge.ui;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SortedMap;

import ca.sfu.greenfoodchallenge.meal.Day;
import ca.sfu.greenfoodchallenge.meal.Meal;
import ca.sfu.greenfoodchallenge.meal.MealCalendar;
import ca.sfu.greenfoodchallenge.meal.MealComponent;
import ca.sfu.greenfoodchallenge.meal.Period;
import ca.sfu.greenfoodchallenge.meal.Protein;

import static org.junit.Assert.assertEquals;

public class PeriodTest {


    MealCalendar testCalendar;
    Period testPeriod;


    @Before
    public void setUp() {
        testCalendar = new MealCalendar();


        Calendar calendar = GregorianCalendar.getInstance();

        for (int i = 0; i < 10; i++) {
            Date date = calendar.getTime();
            Day testDay = new Day();
            Meal testMeal = new Meal("meal of day "+i);
            testMeal.addComponent(
                    new MealComponent(
                            new Protein("p1",0.5,""),
                            Math.pow(2,i)
                    )
            );
            testDay.addMeal(testMeal);
            testCalendar.updateDay(date,testDay);
            calendar.add(Calendar.DATE,-1);
        }

        testPeriod = new Period(testCalendar);

    }

    @Test
    public void getPeriodStartDate() {
        Calendar begin = GregorianCalendar.getInstance();
        begin.add(Calendar.DATE,-(Period.DEFAULT_PERIOD_LENGTH-1));
        Date beginDate = begin.getTime();

        assertEquals(
                MealCalendar.date2String(beginDate),
                MealCalendar.date2String(testPeriod.getPeriodStartDate())
        );
    }

    @Test
    public void getPeriodEndDate() {

        Date today = GregorianCalendar.getInstance().getTime();

        assertEquals(
                MealCalendar.date2String(today),
                MealCalendar.date2String(testPeriod.getPeriodEndDate())
        );

    }



    @Test
    public void getPeriodSize() {
        assertEquals(Period.DEFAULT_PERIOD_LENGTH,testPeriod.getPeriodSize());
    }

    @Test
    public void getPeriodDays() {
        SortedMap<Date,Day> days = testPeriod.getPeriodDays();

        Calendar referenceCalendar = GregorianCalendar.getInstance();
        referenceCalendar.add(Calendar.DATE,-(Period.DEFAULT_PERIOD_LENGTH-1));

        int i = 6;

        for (SortedMap.Entry<Date,Day> entry: days.entrySet()) {
            assertEquals(
                    MealCalendar.date2String(referenceCalendar.getTime()),
                    MealCalendar.date2String(entry.getKey())
            );

            assertEquals(
                    "meal of day "+i,
                    entry.getValue()
                            .getMealList()
                            .entrySet()
                            .iterator()
                            .next()
                            .getValue()
                            .getMealName()
            );

            referenceCalendar.add(Calendar.DATE,1);
            i--;
        }


    }

    @Test
    public void update() {

        MealCalendar emptyCalendar = new MealCalendar();

        Day day = new Day();
        Meal meal = new Meal("dinner");
        meal.addComponent(
                new MealComponent(
                        new Protein("p1",1,""),
                        1
                )
        );
        day.addMeal(meal);

        emptyCalendar.updateDay(
                GregorianCalendar.getInstance().getTime(),
                day
        );


        testPeriod.update(emptyCalendar);

        assertEquals(1.0,testPeriod.totalServing(),0.001);

    }




    @Test
    public void proteinAmountsByName() {
    }

    @Test
    public void proteinPercentagesByName() {
    }



    @Test
    public void totalServing() {
        assertEquals(
                Math.pow(2,7)-1, //geometric series
                testPeriod.totalServing(),
                0.001
        );
    }


    @Test
    public void totalCarbon() {
        assertEquals(
                (Math.pow(2,7)-1)/2, //geometric series
                testPeriod.totalCarbon(),
                0.001
        );
    }

}