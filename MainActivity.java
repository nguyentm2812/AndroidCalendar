package calendar.example.com.calendarandroid;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;

import java.util.Calendar;

import static android.provider.CalendarContract.*;

public class MainActivity extends AppCompatActivity {
    final static String tag = "Calendar";

    /** Called wehn teh activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onClickListCalendar(MenuItem item) {

        listCalendars();
    }

    public void onClickShowCalendar(MenuItem item) {
        Calendar dateToShow = Calendar.getInstance();
        dateToShow.set(2016, Calendar.MARCH, 3, 10, 00);

        showCalendarAtTime(dateToShow);
    }

    public void onClickEditEvent(MenuItem item) {
        final long eventID = 179;
        editEvent(eventID);
    }


    public void onClickFindCalendar(MenuItem item) {
        long calendarID = findCalendar("com.google");
        Log.i(tag, String.format("Calendar ID: %d", calendarID));
    }

    long findCalendar(String s) {
        return 0;
    }

    public void onClickCreateEvent(MenuItem item) {
        long calendarID = findCalendar("com.google");

        Calendar start = Calendar.getInstance();
        start.set(2016, Calendar.MARCH, 3, 10, 00);
        Calendar end = Calendar.getInstance();
        end.set(2016, Calendar.MARCH, 3, 11, 00);

        long eventID = createEvent(calendarID, "CSCE 499", "Android Project", start, end);

        Log.i(tag, String.format("Event ID;%d", eventID));

    }

    public void onClickUpdateEvent(MenuItem item) {
        long eventID = 179;
        updateEvent(eventID, "CSCE 499", "Android project");
    }

    public void onClickDeleteEvent(MenuItem item){
        long eventID = 179;
        deleteEvent(eventID);
        logEvent(eventID);
        logEventIfNotDeleted(eventID);
    }
    public void onClickCreateEventRecurring(MenuItem item) {
        long calendarID = findCalendar("com.google");
        Calendar start = Calendar.getInstance();
        start.set(2015, Calendar.MARCH, 3, 10, 00);

        // Set the duration and rule

        String duration = "";
        String rRule = "";

        long eventID = createEventRecurring(calendarID, "com.google", start, duration, rRule);
        Log.i(tag, String.format("Event ID:%d", eventID));
    }

    public void onClickExit(MenuItem item) {

        finish();
    }

    private void showCalendarAtTime(Calendar dateToShow) {
        long epochMilliseconds = dateToShow.getTimeInMillis();

        Uri.Builder uriBuilder = CONTENT_URI.buildUpon();
        uriBuilder.appendPath("time");
        ContentUris.appendId(uriBuilder, epochMilliseconds);
        Uri uri = uriBuilder.build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);

        startActivity(intent);
    }

    private void editEvent(long eventID) {
        Uri uri = ContentUris.withAppendedId(Events.CONTENT_URI, eventID);

        Intent intent = new Intent(Intent.ACTION_EDIT).setData(uri);

        startActivity(intent);
    }

    private void createNewEvent(String title, String location, String description, Calendar begin, Calendar end, String invitees) {
        Intent intent = new Intent(Intent.ACTION_INSERT).setData(Events.CONTENT_URI);
        putNewEventExtras(intent, title, location, description, begin, end, invitees);
        startActivity(intent);
    }

    private void putNewEventExtras(Intent intent, String title, String location, String description, Calendar begin, Calendar end, String invitees) {
        if (title != null)
            intent.putExtra(Events.TITLE, title);
        if (location != null)
            intent.putExtra(Events.EVENT_LOCATION, location);
        if (description != null)
            intent.putExtra(Events.DESCRIPTION, description);
        if (begin != null)
            intent.putExtra(EXTRA_EVENT_BEGIN_TIME, begin.getTimeInMillis());
        if (end != null)
            intent.putExtra(EXTRA_EVENT_END_TIME, end.getTimeInMillis());
        if (invitees != null)
            intent.putExtra(Intent.EXTRA_EMAIL, invitees);

    }

    void listCalendars() {
        String[] returnColumns = new String[]{
                Calendars._ID,
                Calendars.ACCOUNT_NAME,
                Calendars.CALENDAR_DISPLAY_NAME,
                Calendars.ACCOUNT_TYPE
        };

        Cursor cursor = null;
        ContentResolver cr = getContentResolver();

        //Call query to get all rows from the calendar table


        //cursor = cr.query(CalendarContract.Calendars.CONTENT_URI, returnColumns, null, null, null);
        while (cursor.moveToNext()){
            long calID = 0;
            String displayName = null;
            String accountName = null;
            String accountType = null;

            // get the field values
            calID = cursor.getLong(0);
            displayName = cursor.getString(1);
            accountName = cursor.getString(2);
            accountType = cursor.getString(3);
            Log.i(tag, String.format("ID=%d Display=%s Account=%s Type=%s", calID, displayName, accountName, accountType));
        }

        cursor.close();
    }

    long findCalendar(String accountName, String accountType, String displayName){
        String[] returnColumns = new String[]{
                CalendarContract.Calendars._ID,
        };

        String selection = "((" + Calendars.ACCOUNT_NAME + "= ?) AND " + "(" + Calendars.ACCOUNT_TYPE + "=?) AND" + "(" + Calendars.CALENDAR_DISPLAY_NAME + "= ?))";
        String[] selectionArgs = {accountName, accountType, displayName};

        Cursor cursor = null;
        ContentResolver cr = getContentResolver();

        //cursor = cr.query(CalendarContract.Calendars.CONTENT_URI, returnColumns, selection, selectionArgs, null);

        long calendarID = -1;

        if (cursor.moveToNext())
            calendarID = cursor.getLong(0);

        return calendarID;
    }

    private long createEvent(long calendarID, String title, String description, String location, Calendar startDate, Calendar endDate){
        long eventID = -1;
        long startMilliseconds = startDate.getTimeInMillis();
        long endMilliseconds = endDate.getTimeInMillis();

        ContentResolver cr = getContentResolver();

        return eventID;
    }

    private void updateEvent(long eventID, String location, String description){
        ContentResolver cr = getContentResolver();

        Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.EVENT_LOCATION, location);
        values.put(CalendarContract.Events.DESCRIPTION,description);

        int rows = 0;

        rows = cr.update(uri, values, null, null);

        Log.i(tag, String.format("Event ID:%d | Row Updated:%", eventID, rows));

    }

    private void deleteEvent(long eventID){
        ContentResolver cr = getContentResolver();

        Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        int rows = 0;
        // issue the delete statement
        rows = cr.delete(uri,null,null);
        Log.i(tag, String.format("Event ID:%d | Rows delete: %d", eventID, rows));
    }

    private void logEvent(long eventID){
        String[] EVENT_PROJECTION = new String[]{
                CalendarContract.Events._ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DELETED
        };

        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI,eventID);
        cur = cr.query(uri, EVENT_PROJECTION, null, null, null);

        //use the cursor to step through the returned records
        if (cur.moveToNext()){
            long returnedEventID = cur.getLong(0);
            String title = cur.getString(1);
            int isDeleted = cur.getInt(2);

            Log.i(tag, String.format("Event ID=%s Is Deleted:%d", returnedEventID, title, isDeleted));
        }

        cur.close();
    }
    private void logEventIfNotDeleted(long eventID){
        String[] EVENT_PROJECTION = new String[]{
                CalendarContract.Events._ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DELETED,
        };

        String queryFilter = CalendarContract.Events.DELETED + "=?";
        String[] queryFilterValues = {"0"};

        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        cur = cr.query(uri, EVENT_PROJECTION, queryFilter, queryFilterValues, null);

        // Use the cursor to step through the returned records.
        if (cur.moveToNext()){
            long returnedEventID = cur.getLong(0);
            String title = cur.getString(1);
            int isDeleted = cur.getInt(2);

            Log.i(tag, String.format("Event ID=%d Title=%s Is Deleted:%d",returnedEventID, title, isDeleted));
        }
        cur.close();
    }

    private long createEventRecurring(long calendarID, String title, String description, String location, String duration, String rRule, Calendar startDate, Calendar endDate ){
        long eventID = -1;
        long startMilliseconds = startDate.getTimeInMillis();

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.CALENDAR_ID, calendarID);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, description);
        values.put(CalendarContract.Events.EVENT_LOCATION, location);
        values.put(CalendarContract.Events.DTSTART,startMilliseconds );
        values.put(CalendarContract.Events.EVENT_TIMEZONE,"US/Eastern");
        values.put(CalendarContract.Events.DURATION, duration);
        values.put(CalendarContract.Events.RRULE, rRule);

        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        eventID = ContentUris.parseId(uri);

        return eventID;

    }

}
