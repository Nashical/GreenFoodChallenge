package ca.sfu.greenfoodchallenge.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import ca.sfu.greenfoodchallenge.database.GreenFoodChallengeDatabase;
import ca.sfu.greenfoodchallenge.meal.DefaultProteins;
import ca.sfu.greenfoodchallenge.meal.MealCalendar;
import ca.sfu.greenfoodchallenge.meal.Period;
import ca.sfu.greenfoodchallenge.meal.Recommendation;

public class RecommendationFragment extends Fragment {

    Button Change;
    TextView display, title;

    float percentage[]= new float [7];

    String categories[] = DefaultProteins.NAMES;

    NumberFormat value = new DecimalFormat("#0.0");

    final static String DECIMAL_FORMAT = "#.#";
    final static String POP_UP_TAG = "Popup!";
    final static String PIE_CHART_LABEL = "ok";


    final static String TITLE_TEXT = "Recommended Meal Plan \n\n";
    final static String UPDATE_TEXT_0 = "With this diet you could save:\n";
    final static String UPDATE_TEXT_1 = "kg of carbon annually from your diet!\n\n";
    final static String UPDATE_TEXT_2 = "This is equivalent to saving:\n";
    final static String UPDATE_TEXT_3 = "km driven in a car\n";
    final static String UPDATE_TEXT_4 = "lb of coal burned\n";
    final static String UPDATE_TEXT_5 = " homes powered for a day\n";
    final static String UPDATE_TEXT_6 = "CO2 consumed by ";
    final static String UPDATE_TEXT_7 = " trees!\n\n";
    final static String UPDATE_TEXT_8 = "If every Vancouverite followed in your footsteps, everybody will save:\n";
    final static String UPDATE_TEXT_9 = " tonnes of CO2!\n\n";

    final static String[] PLAN_TEXT = {"MEAT EATER\nRetain most of your meat consumption",
                                        "BALANCED\nReduce meat consumption in favour of more sustainable options",
                                        "VEGETARIAN\nEliminate meat from your diet and replace with veggies, eggs, and beans"
                                        };

    double carbon;
    Double[] equivalence;
    double totalVancouverSaving;

    static int plan = Recommendation.BALANCED_PLAN;

    public final static String TAG = "Recommendation";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getFragmentManager().beginTransaction().add(R.id.fragment_container, this, TAG);


        MealCalendar mc = GreenFoodChallengeDatabase.getCurrentUser().getMealCalendar();
        Period P = new Period(mc);
        Recommendation recommendation = new Recommendation(P,plan);

        carbon = recommendation.currentYearlyCarbon();
        DecimalFormat value = new DecimalFormat(DECIMAL_FORMAT);

        carbon = recommendation.yearlyCarbonDifference();
        totalVancouverSaving =
                recommendation.yearlyCarbonSavedByVancouver();
        equivalence = recommendation.getEquivalences();

        View view = inflater.inflate(R.layout.fragment_recommendation, container, false);
        display = view.findViewById(R.id.Display);
        title = view.findViewById(R.id.Title);
        Change = view.findViewById(R.id.change);

        //title.setText(R.string.this_is_your_weekly_recomended);

        Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecomendationPlanChooserPopUp dialog = new RecomendationPlanChooserPopUp();
                dialog.show(getFragmentManager(), POP_UP_TAG);
                UpdateText();
            }
        });




        //Store data entries into chart
        List<Integer> colors = new ArrayList<>();
        List<PieEntry> pieEntries = new ArrayList<>();

        int colorCounter = 0;
        for (String proteinName:  DefaultProteins.NAMES){
           if(recommendation.getSuggestedPercentages().get(proteinName)!=null && recommendation.getSuggestedPercentages().get(proteinName).floatValue() != 0) {
               new PieEntry(2, PIE_CHART_LABEL);

               colors.add(DefaultProteins.COLORS[colorCounter]);
               colorCounter++;

               pieEntries.add(
                       new PieEntry(
                               recommendation.getSuggestedPercentages().get(proteinName).floatValue(),
                               proteinName
                       )
               );
           }
           else{
               colorCounter++;
           }

        }


        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setValueFormatter(new MyValueFormatter());
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(18);
       // dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData data = new PieData(dataSet);

        //Get chart
        PieChart chart = view.findViewById(R.id.chart);
        chart.setData(data);

        Legend legend = chart.getLegend();
        legend.setTextSize(14);

        chart.getDescription().setEnabled(false);
        chart.setEntryLabelColor(Color.BLACK);
        chart.animateY(1000);
        chart.invalidate();

        UpdateText();

        return view;
    }

    private void UpdateText(){
        display.setText(UPDATE_TEXT_0 + value.format(carbon) + UPDATE_TEXT_1 + UPDATE_TEXT_2 +
                value.format(equivalence[0]) + UPDATE_TEXT_3 + value.format(equivalence[1]) + UPDATE_TEXT_4 +
                value.format(equivalence[2]) + UPDATE_TEXT_5 + UPDATE_TEXT_6 + value.format(equivalence[3]) +
                UPDATE_TEXT_7 + UPDATE_TEXT_8 + value.format(totalVancouverSaving) + UPDATE_TEXT_9);

        title.setText(TITLE_TEXT + PLAN_TEXT[plan]);


    }



}
