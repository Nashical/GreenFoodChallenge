package ca.sfu.greenfoodchallenge.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

import ca.sfu.greenfoodchallenge.meal.Day;
import ca.sfu.greenfoodchallenge.meal.MealCalendar;

import static ca.sfu.greenfoodchallenge.ui.MealViewer.NEW_MEAL;
import static ca.sfu.greenfoodchallenge.ui.MealViewer.SELECTED_DATE;
import static ca.sfu.greenfoodchallenge.ui.MealViewer.SELECTED_MEAL;
import static ca.sfu.greenfoodchallenge.ui.CalendarFragment.selectedDate;

public class MealsInADayAdapter extends RecyclerView.Adapter<MealsInADayAdapter.MyViewHolder> {

    Day day;
    Date date;
    private Context context;

    public MealsInADayAdapter(Context context){
        this.context = context;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public Button Edit;
        public TextView mealsInADayTextBox;
        public String mealName;


        public MyViewHolder(View v) {
            super(v);
            mealsInADayTextBox = (TextView) v.findViewById(R.id.mealName);
            Edit = v.findViewById(R.id.edit);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MealsInADayAdapter() {
    }


    // Create new views (invoked by the layout manager)
    @Override
    public MealsInADayAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meal_in_a_day_view, parent, false);

        final MyViewHolder vh = new MyViewHolder(v);

        vh.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(parent.getContext(),MealViewer.class);
                intent.putExtra(NEW_MEAL,false);
                intent.putExtra(SELECTED_DATE, MealCalendar.date2String(selectedDate));
                intent.putExtra(SELECTED_MEAL,vh.mealName);

                context.startActivity(intent);

            }
        });



        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        String mealName = (String) day.getMealList().keySet().toArray()[position];

        holder.mealsInADayTextBox.setText(mealName);

        holder.mealName = mealName;

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return day.getMealCount();
    }




    public void updateDay(Day day, Date date)
    {
        this.day = day;
        this.date = date;
        notifyDataSetChanged();
    }


}