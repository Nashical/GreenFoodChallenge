package ca.sfu.greenfoodchallenge.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class MealSummaryFragment extends Fragment{
    final static float SCALE_KG_TO_G = 1000;
    final static String TITLE_PIE_CHART = "";
    final static String EXPLANATION_TOTAL_EMISSION = "Total emission is ";
    final static String UNIT_TOTAL_EMISSION = " kg per week";

    private final int MAX_NUM_OF_INDEX = 6;

    int gramsOfBeef = (int) 0;

    NumberFormat value = new DecimalFormat("#0.0");

    // Data for a pie chart
    private int consumptionGramOfMeat[] = {100, 200, 300, 400, 500, 600, 700};
    private String meatNames[] = {"Beef", "Pork", "Chicken", "Fish", "Eggs", "Beans", "Vegetables"};

    // Data for a horizontal bar chart
    private int co2Emission[] = {30, 60, 80, 100, 25, 10, 7};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        MealCalendar mc = GreenFoodChallengeDatabase.getCurrentUser().getMealCalendar();
        Period P = new Period(mc);

//        // Generates a pie chart
//        List<PieEntry> pieEntries = new ArrayList<>();
//        for (int i = 0; i < consumptionGramOfMeat.length; i++) {
//            pieEntries.add(new PieEntry(consumptionGramOfMeat[i], meatNames[i]));
//        }

        //Store data entries into chart
        List<PieEntry> pieEntries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        int colorCounter = 0;
        for (String proteinName:  DefaultProteins.NAMES){
            if(P.proteinAmountsByName().get(proteinName)!=null && P.proteinAmountsByName().get(proteinName).floatValue() != 0) {
                new PieEntry(2, "ok");

                colors.add(DefaultProteins.COLORS[colorCounter]);
                colorCounter++;

                pieEntries.add(
                        new PieEntry(
                                P.proteinAmountsByName().get(proteinName).floatValue(),
                                proteinName
                        )
                );
            }
            else{
                colorCounter++;
            }
        }

//        Recommendation rec = new Recommendation(P);
//
//        Double array[] = new Double [7];
//
//        Map<Protein, Double> temp = P.getProteinEquivalence();
//
//        array[0] = temp.get("Beef");
//        array[1] = temp.get("Pork");
//        array[2] = temp.get("Chicken");
//        array[3] = temp.get("Fish");
//        array[4] = temp.get("Eggs");
//        array[5] = temp.get("Beans");
//        array[6] = temp.get("Vegetables");


        View view = inflater.inflate(R.layout.fragment_meal_sumary, container, false);

        PieDataSet dataSet = new PieDataSet(pieEntries, TITLE_PIE_CHART);
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(18);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData data = new PieData(dataSet);

        PieChart chart = (PieChart) view.findViewById(R.id.pie_chart);
        chart.setData(data);


        Legend legend = chart.getLegend();
        legend.setTextSize(14);

        chart.getDescription().setEnabled(false);
        chart.setEntryLabelColor(Color.BLACK);
        chart.animateY(1000);
        chart.invalidate();

        double totalEmission = P.totalCarbon();
        TextView txt = (TextView) view.findViewById(R.id.total_emission);
        String text = EXPLANATION_TOTAL_EMISSION + value.format(totalEmission) + UNIT_TOTAL_EMISSION;
        txt.setText(text);

//        // Generates a horizontal bar chart
//        ArrayList<String> labels = setArray();
//        HorizontalBarChart mChart = (HorizontalBarChart) view.findViewById(R.id.bar_chart);
//        mChart.setDrawBarShadow(false);
//        mChart.setDrawValueAboveBar(true);
//        mChart.getDescription().setEnabled(false);
//        mChart.setPinchZoom(false);
//        mChart.setDrawGridBackground(false);
//        mChart.setTouchEnabled(false);
//
//        XAxis xl = mChart.getXAxis();
//        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xl.setDrawAxisLine(true);
//        xl.setDrawGridLines(false);
//        xl.setTextSize(18);
//        CategoryBarChartXaxisFormatter xaxisFormatter = new CategoryBarChartXaxisFormatter(labels);
//        xl.setValueFormatter(xaxisFormatter);
//        xl.setGranularity(1);
//
//        YAxis yl = mChart.getAxisLeft();
//        yl.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
//        yl.setDrawGridLines(false);
//        yl.setEnabled(false);
//        yl.setAxisMinimum(0f);
//
//        YAxis yr = mChart.getAxisRight();
//        yr.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
//        yr.setDrawGridLines(false);
//        yr.setAxisMinimum(0f);
//        yr.setEnabled(false);
//        yr.setDrawLabels(false);
//
//        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
//        for (int i = 0; i <= MAX_NUM_OF_INDEX; i++) {
//            yVals1.add(new BarEntry(i, co2Emission[i]));
//        }
//
//        BarDataSet set1;
//        set1 = new BarDataSet(yVals1, "DataSet 1");
//        set1.setColors(MY_COLORS);
//        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
//        dataSets.add(set1);
//        BarData horData = new BarData(dataSets);
//        horData.setValueTextSize(15f);
//        horData.setBarWidth(.7f);
//        mChart.setData(horData);
//        mChart.getLegend().setEnabled(false);
//        mChart.getDescription().setEnabled(false);

        return view;
    }

    private ArrayList<String> setArray() {
        ArrayList<String> labels = new ArrayList<>();
        labels.add(DefaultProteins.NAME_BEEF);
        labels.add(DefaultProteins.NAME_PORK);
        labels.add(DefaultProteins.NAME_CHICKEN);
        labels.add(DefaultProteins.NAME_FISH);
        labels.add(DefaultProteins.NAME_EGGS);
        labels.add(DefaultProteins.NAME_BEANS);
        labels.add(DefaultProteins.NAME_VEGGIES);

        return labels;
    }

//    public class CategoryBarChartXaxisFormatter implements IAxisValueFormatter {
//
//        ArrayList<String> mValues;
//
//        public CategoryBarChartXaxisFormatter(ArrayList<String> values) {
//            this.mValues = values;
//        }
//
//        @Override
//        public String getFormattedValue(float value, AxisBase axis) {
//
//            int val = (int) value;
//            String label = "";
//            if (val >= 0 && val < mValues.size()) {
//                label = mValues.get(val);
//            } else {
//                label = "";
//            }
//            return label;
//        }
//    }

    Double null2Zero(Double d){
        if(d == null){
            return new Double(0);
        }
        return d;
    }
}





