package ca.sfu.greenfoodchallenge.database;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import ca.sfu.greenfoodchallenge.meal.Day;
import ca.sfu.greenfoodchallenge.meal.DefaultProteins;
import ca.sfu.greenfoodchallenge.meal.DummyContent;
import ca.sfu.greenfoodchallenge.meal.Meal;
import ca.sfu.greenfoodchallenge.meal.MealCalendar;
import ca.sfu.greenfoodchallenge.meal.MealComponent;

import static org.junit.Assert.*;

public class MealsSearchTest {

    MealsSearch mealsSearch;
    int addedMealsCounter = 0;
    @Before
    public void generateUserList(){

        ArrayList<User> users = new ArrayList<User>();



        for (int i = 0; i < 3; i++) {
            User user = new User(
                    "--",
                    "--",
                    "--",
                    0,
                    "--"
            );

            MealCalendar mc = new MealCalendar();

            for (int j = 0; j < 3; j++) {
                Day day = new Day();

                for (int k = 0; k < 3; k++) {
                    Meal meal = new Meal("meal-"+j+"-"+k);
                    meal.addComponent(new MealComponent(DefaultProteins.array()[k],10));
                    meal.setDescription("desc-"+j+"-"+k);
                    meal.setLocation("loc-"+j+"-"+k);
                    meal.setMunicipality(k + 1);
                    if (k % 2 == 0) {
                        meal.setShared(true);
                        addedMealsCounter++;
                    }
                    day.addMeal(meal);
                }


                mc.updateDay("2018-10-0" + j, day);
            }
            user.setMealCalendar(mc);
            users.add(user);
        }

        mealsSearch = new MealsSearch(users);

    }

    @Test
    public void constructorTest(){
        System.out.println(addedMealsCounter);
        assertEquals(addedMealsCounter,mealsSearch.getSharedMealsCount());
    }


    @Test
    public void byMainProtein() {


        assertEquals(
                "meal-2-0",
                mealsSearch.byMainProtein(DefaultProteins.array()[0]).get(0).getMealName()
        );

        assertEquals(
                "meal-1-0",
                mealsSearch.byMainProtein(DefaultProteins.array()[0]).get(4).getMealName()
        );
    }

    @Test
    public void byMunicipality() {

        assertEquals(
                9,
                mealsSearch.byMunicipality(1).size()
        );

        assertEquals(
                3,
                mealsSearch.byMunicipality(3).get(1).getMunicipality()
        );


    }

    @Test
    public void fullText() {

        assertEquals(
                3,
                mealsSearch.fullText("desc-0-0").size()
        );


        assertEquals(
                9,
                mealsSearch.fullText(Municipality.NAME_BELCARRA).size()
        );

    }
}