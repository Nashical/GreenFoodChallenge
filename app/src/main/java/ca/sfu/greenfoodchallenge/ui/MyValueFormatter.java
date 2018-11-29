package ca.sfu.greenfoodchallenge.ui;


import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class MyValueFormatter implements IValueFormatter {
    final static String DECIMAL_FORMAT = "###,###,##0.0";
    final static String PERCENTAGE_SIGN = "%";

    private DecimalFormat mFormat;

    public MyValueFormatter() {
        mFormat = new DecimalFormat(DECIMAL_FORMAT); // use one decimal
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        // write your logic here
        return mFormat.format(value) + PERCENTAGE_SIGN; // e.g. append a %-sign
    }

}