package ca.sfu.greenfoodchallenge.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.sfu.greenfoodchallenge.database.GreenFoodChallengeDatabase;
import ca.sfu.greenfoodchallenge.meal.Day;
import ca.sfu.greenfoodchallenge.meal.DefaultProteins;
import ca.sfu.greenfoodchallenge.meal.Meal;
import ca.sfu.greenfoodchallenge.meal.MealCalendar;
import ca.sfu.greenfoodchallenge.meal.MealComponent;


public class DailyMeal extends DialogFragment {
    final static String UNIT_NAME = "g";
    final static String NON_EMPTY_SPACE = "";
    final static String EMPTY_SPACE = " ";
    final static int NUMBER_SCALES_HUNDRED = 100;
    final static int NUMBER_SCALES_TEN = 10;

    Button Save, Cancel;
    TextView MealDisplay;

    List<TextView> textViews = new ArrayList<>();
    //TextView BeefView, PorkView, ChickenView, FishView, EggView, BeansView, VeggiesView;
    List<SeekBar> seekBars = new ArrayList<>();
    //SeekBar beef, pork, chicken, fish, egg, beans, veggies;

    String selectedMealName;
    Meal oldMeal;
    Meal newMeal;

    //this is needed to trigger the change in the recycle viewer when an item is created
    static CalendarFragment calendarFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.daily_meal, container, false);

        Cancel = view.findViewById(R.id.back);
        Save = view.findViewById(R.id.next);
        MealDisplay = view.findViewById(R.id.MealName);

        Bundle args = getArguments();
        selectedMealName = (String) args.get(MealViewer.SELECTED_MEAL);


        //Date of the day we are adding a meal tof
        oldMeal = getSelectedMeal(selectedMealName);
        newMeal = new Meal(oldMeal.getMealName());


        //IMPORTANT: thses have to follow the order in the deafult protein list
        seekBars.add( (SeekBar) view.findViewById(R.id.Beef));
        seekBars.add( (SeekBar) view.findViewById(R.id.Pork));
        seekBars.add( (SeekBar) view.findViewById(R.id.Chicken));
        seekBars.add( (SeekBar) view.findViewById(R.id.Fish));
        seekBars.add( (SeekBar) view.findViewById(R.id.Egg));
        seekBars.add( (SeekBar) view.findViewById(R.id.Beans));
        seekBars.add( (SeekBar) view.findViewById(R.id.Veggies));

        textViews.add( (TextView) view.findViewById(R.id.BeefDisplay));
        textViews.add( (TextView) view.findViewById(R.id.PorkDisplay));
        textViews.add( (TextView) view.findViewById(R.id.ChickenDisplay));
        textViews.add( (TextView) view.findViewById(R.id.FishDisplay));
        textViews.add( (TextView) view.findViewById(R.id.EggDisplay));
        textViews.add( (TextView) view.findViewById(R.id.BeanDisplay));
        textViews.add( (TextView) view.findViewById(R.id.VeggieDisplay));

        MealDisplay.setText(oldMeal.getMealName());



        //Manage the status bars
        for (int i = 0; i < DefaultProteins.NUMBER_OF_PROTEINS; i++) {

            final SeekBar seekBar = seekBars.get(i);
            final TextView textView = textViews.get(i);
            final String currentProteinName = DefaultProteins.NAMES[i];

            final double oldGramsValue = oldMeal.getGramsByProteinName(currentProteinName);

            newMeal.addComponent(new MealComponent(DefaultProteins.array()[i],
                    oldMeal.getKilogramsByProteinName(currentProteinName)
            ));


            final double oldSliderValue = oldGramsValue / NUMBER_SCALES_HUNDRED;

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                //ranges from 1 to 10 where 10 is one kilogram
                int currentValue = (int) oldSliderValue;

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    currentValue = progress;
                    textView.setText(NON_EMPTY_SPACE + currentValue * NUMBER_SCALES_HUNDRED + UNIT_NAME);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                    MealComponent newComponent = newMeal.getComponentByProteinName(currentProteinName);
                    //this takes input in kilograms
                    newComponent.setServingSize(((double)currentValue)/NUMBER_SCALES_TEN);

                    Toast.makeText(
                            getActivity().getBaseContext(),
                            currentProteinName + EMPTY_SPACE
                                    + String.valueOf(currentValue * NUMBER_SCALES_HUNDRED)
                                    + UNIT_NAME, Toast.LENGTH_SHORT
                    ).show();
                }

            });//end seekbarcurrentProteinNamecurrentProteinName

            seekBar.setProgress((int)oldSliderValue);
        }


        // Manage the buttons at the bottom
        Cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMeal(selectedMealName,newMeal);
                getDialog().dismiss();

                calendarFragment.updateSelectedDayView();
            }

        });

        return view;
    }

    private Meal getSelectedMeal( String selectedMeal){

        MealCalendar mc = GreenFoodChallengeDatabase.getCurrentUser().getMealCalendar();

        Day mealDay = mc.getDay(CalendarFragment.selectedDate);

        return mealDay.getMeal(selectedMeal);
    }

    private void updateMeal( String selectedMeal, Meal newMeal) {
        MealCalendar mc = GreenFoodChallengeDatabase.getCurrentUser().getMealCalendar();

        Day selectedDay = mc.getDay(CalendarFragment.selectedDate);

        selectedDay.removeMeal(selectedMeal);
        selectedDay.addMeal(newMeal);

        mc.updateDay(CalendarFragment.selectedDate,selectedDay);

        GreenFoodChallengeDatabase.updateCurrentUser();
    }

}

