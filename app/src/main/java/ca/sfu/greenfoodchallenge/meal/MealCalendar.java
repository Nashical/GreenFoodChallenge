package ca.sfu.greenfoodchallenge.meal;


import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Calendar class form which all meals that were ever filled in can be accessed.
 *
 * Essentially it's only a collection of days. Days in which a meal has never been set are not
 * stored at all.
 */
public class MealCalendar {


    final static String DATE_FORMAT = "yyyy-MM-dd";


    // Days that have been defined. These are days that either have a meal of had a meal at some
    // point.
    private SortedMap<String,Day> definedDays;

    //TODO load calendar from storage
    public MealCalendar()
    {
        definedDays = new TreeMap<>();
    }


    // Returns a day. An empty one if no meal has been defined.
    // The returned day is not a reference to an internal day. This is so that when we access a day
    // that is not defined, we have to manually save it, so that empty days are not stored
    public Day getDay(Date date){
        if(definedDays.get(MealCalendar.date2String(date))!=null) {
            return new Day(definedDays.get(MealCalendar.date2String(date)));


        } else
            return new Day();
    }

    // Same as above, alternative date
    public Day getDay(int year, int month, int dayOfMonth) {
        return getDay(MealCalendar.makeDate(year, month, dayOfMonth));
    }

    // Same as above, alternative date
    public Day getDay(String date) {
        return getDay(MealCalendar.string2Date(date));
    }



    // Since not all days are stored, to make sure a day is stored, call this
    public void updateDay(Date date, Day day){
        definedDays.put(MealCalendar.date2String(date),new Day(day));
    }

    // Same as above, alternative date
    public void updateDay(int year, int month, int dayOfMonth, Day day) {
        updateDay(MealCalendar.makeDate(year, month, dayOfMonth),day);
    }

    // Same as above, alternative date
    public void updateDay(String date, Day day) {
        definedDays.put(date,day);
    }





    //FIX ME: If map is sorted by ascending date the search function has to be reversed.
    public SortedMap<Date, Day> getLastNDays(int period) {

        Calendar calendar = GregorianCalendar.getInstance();
        Date dayToPullDate = new Date();
        calendar.setTime(dayToPullDate);

        SortedMap<Date,Day> lastNDays = new TreeMap<>();

        for (int i = 0; i < period; i++) {
            lastNDays.put(dayToPullDate,getDay(dayToPullDate));
            calendar.add(Calendar.DATE,-1); //go back by a day
            dayToPullDate = calendar.getTime();

        }

        return lastNDays;
    }


    public SortedMap<String, Day> getDefinedDays() {
        return definedDays;
    }

    //Return the number of days specified by period,
        //Or all elements in the map if period > map size.
    public static Date makeDate(int year, int month, int dayOfMonth) {

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        return calendar.getTime();
    }


    public static String date2String(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.CANADA);
        return sdf.format(date);
    }


    public static Date string2Date(String dateString) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        try {
            return sdf.parse(dateString);
        } catch (ParseException parseException){
            //todo{ find  a better default}
            return new Date();
        }
    }

}
