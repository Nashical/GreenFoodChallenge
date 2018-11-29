package ca.sfu.greenfoodchallenge.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import java.util.Date;

import ca.sfu.greenfoodchallenge.database.GreenFoodChallengeDatabase;
import ca.sfu.greenfoodchallenge.meal.Day;
import ca.sfu.greenfoodchallenge.meal.MealCalendar;

public class CalendarFragment extends Fragment{
    final static String TAG = "CALENDAR_FRAGMENT_TAG";


    View view;
    CalendarView calendarView;

    RecyclerView mealsInADayRecyclerView;
    RecyclerView.Adapter mealsInADayAdapter;
    RecyclerView.LayoutManager mealsInADayLayoutManager;

    Button add;

    static Date selectedDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        getFragmentManager().beginTransaction().add(R.id.fragment_container,this, TAG);

        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = view.findViewById(R.id.calendarView);
        add = view.findViewById(R.id.selectDate);


        selectedDate = new Date(calendarView.getDate());

        mealsInADayRecyclerView = (RecyclerView) view.findViewById(R.id.mealsInADay);
        // Set up the recycle viewer at the bottom of the page where the meals of a day are
        // displayed
            mealsInADayRecyclerView.setHasFixedSize(true);

            // there is no ConstraintLayoutManager
            mealsInADayLayoutManager = new LinearLayoutManager(this.getContext());
            mealsInADayRecyclerView.setLayoutManager(mealsInADayLayoutManager);

            mealsInADayAdapter = new MealsInADayAdapter(getContext());
            mealsInADayRecyclerView.setAdapter(mealsInADayAdapter);


        //Shows meals on current date on create
        updateSelectedDayView();


        // This serves as a hook to refresh the page, for some reason getFragmentManager
        // isn't working due to support vs non support fragments.
        MealViewer.calendarFragment = this;

        //Shows meals on selected date
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = MealCalendar.makeDate(year, month, dayOfMonth);
                updateSelectedDayView();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Tell the pop up which day to create
                Intent intent = new Intent(getContext(),MealViewer.class);
                intent.putExtra(MealViewer.NEW_MEAL,true);
                intent.putExtra(MealViewer.SELECTED_DATE,MealCalendar.date2String(selectedDate));
                startActivity(intent);
            }
        });

        return view;
    }

    void updateSelectedDayView() {
        MealCalendar mc = GreenFoodChallengeDatabase.getCurrentUser().getMealCalendar();

        Day mealDay = mc.getDay(selectedDate);

        ((MealsInADayAdapter) mealsInADayAdapter).updateDay(mealDay,selectedDate);
    }

}
