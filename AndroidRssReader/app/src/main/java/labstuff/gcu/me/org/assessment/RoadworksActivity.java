package labstuff.gcu.me.org.assessment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/*
 * Created by Christopher Dillon
 *  Student ID: S1514278
 */

public class RoadworksActivity extends AppCompatActivity {

    private List<Traffic> roadworksList;
    private List<Traffic> selectedList;

    private ArrayAdapter<Traffic> roadworksAdapter;
    private ArrayAdapter<Traffic> selectedAdapter;

    private Button getRoadworkBtn;
    private TextView selectDateTxt;
    private ImageView calendarIV;
    private EditText enterNameTxt;

    private ListView roadworksListView;
    private ListView selectedListView;

    Date selectedDate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roadworks_layout);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        selectDateTxt = (TextView)findViewById(R.id.selectDateTxt);
        selectDateTxt.setText("Search by either entering a title within the text box and selecting 'Submit' or by clicking the date icon " +
                "search for a specific date");

        calendarIV= (ImageView) findViewById(R.id.image);
        calendarIV.setBackgroundResource(R.drawable.datepicker);

        getRoadworkBtn = (Button) findViewById(R.id.getRoadworkBtn);
        enterNameTxt = (EditText) findViewById(R.id.enterNameTxt);

        roadworksListView = (ListView) findViewById(R.id.roadworksListView);
        selectedListView = (ListView) findViewById(R.id.selectedListView);

        selectedListView.setVisibility(View.INVISIBLE); //Set the alternative list to invisible upon creation

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Get back button

        roadworksList = (ArrayList<Traffic>)getIntent().getSerializableExtra("roadworksList");
        selectedList = new ArrayList<Traffic>();

        roadworksAdapter = new trafficArrayAdapter(RoadworksActivity.this, 0, roadworksList);
        roadworksListView.setAdapter(roadworksAdapter);

        roadworksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long rowId) { //Handle when a roadworks object is selected from list

                Traffic traffic = roadworksList.get(position);

                Intent intent = new Intent(RoadworksActivity.this, DetailActivity.class);
                intent.putExtra("title", traffic.getTitle());
                intent.putExtra("description", traffic.getDescription());
                intent.putExtra("link", traffic.getLink());
                intent.putExtra("georss", traffic.getGeorss());
                intent.putExtra("author", traffic.getAuthor());
                intent.putExtra("comments", traffic.getComments());
                intent.putExtra("pubDate", traffic.getPubDate());
                startActivity(intent);
            }
        });

        selectedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long rowId) { //Handles roadwork objects based on the selected list

                Traffic traffic = selectedList.get(position);

                Intent intent = new Intent(RoadworksActivity.this, DetailActivity.class);
                intent.putExtra("title", traffic.getTitle());
                intent.putExtra("description", traffic.getDescription());
                intent.putExtra("link", traffic.getLink());
                intent.putExtra("georss", traffic.getGeorss());
                intent.putExtra("author", traffic.getAuthor());
                intent.putExtra("comments", traffic.getComments());
                intent.putExtra("pubDate", traffic.getPubDate());
                startActivity(intent);
            }
        });


        //Grab datepicker data
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                selectedList = new ArrayList<Traffic>(); //Reset each time a new date is selected
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                selectedDate = myCalendar.getTime();
                getSelectedDates();
                if (selectedList.size() > 0) { //If the input returns any results, display the selectedlist and hide all entries
                    selectedAdapter = new trafficArrayAdapter(RoadworksActivity.this, 0, selectedList);
                    selectedListView.setAdapter(selectedAdapter);
                    selectedListView.setVisibility(View.VISIBLE);
                    roadworksListView.setVisibility(View.INVISIBLE);
                }
                else
                {
                    displayToastMessage(); //Error handling if the input returns no results
                    selectedListView.setVisibility(View.INVISIBLE);
                    roadworksListView.setVisibility(View.VISIBLE);
                }
            }

        };

        calendarIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Open datepicker upon click
                new DatePickerDialog(RoadworksActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        getRoadworkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE); //Close the virtual keyboard when button is pressed
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
                selectedList = new ArrayList<Traffic>(); //Reset each time a new date is selected
                String getTxtInput = enterNameTxt.getText().toString(); //Grab edittext input
                getRoadworksWithName(getTxtInput);
                if (selectedList.size() > 0) { //If the input returns any results, display the selectedlist and hide all entries
                    selectedAdapter = new trafficArrayAdapter(RoadworksActivity.this, 0, selectedList);
                    selectedListView.setAdapter(selectedAdapter);
                    selectedListView.setVisibility(View.VISIBLE);
                    roadworksListView.setVisibility(View.INVISIBLE);
                }
                else
                {
                    displayToastMessage(); //Error handling if the input returns no results
                    selectedListView.setVisibility(View.INVISIBLE);
                    roadworksListView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //Used to handled back functionality
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Used to display an error message
    public void displayToastMessage() {
        Toast.makeText(RoadworksActivity.this,
                "No entries found!",
                Toast.LENGTH_LONG).show();
    }

    //Gets input from user and cycles through list to find any suitable matches
    public void getRoadworksWithName(String searchTerm) {
        for (Traffic t : roadworksList) {
            String search = t.getTitle();
            //Sets both the query and entry to lowercase for proper matching
            if (search.toLowerCase().indexOf(searchTerm.toLowerCase()) != -1) {
                selectedList.add(t);
            }
        }
    }

    public void getSelectedDates() {
        for(Traffic t : roadworksList) {
            String pattern = " EEEE, dd MMMM yyyy"; //date format of rss feed
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            DateTime dateSelected = new DateTime(selectedDate);
            String[] parts = t.getDescription().split("\n"); //Split the string in order to seperate the dates
            String firstDate = parts[0];
            String secondDate = parts[1];

            try {
                Date from = simpleDateFormat.parse(firstDate); //Parse the dates
                Date to = simpleDateFormat.parse(secondDate);

                DateTime startDate = new DateTime(from); //Convert the dates to DateTime jodatime as easier to handle
                DateTime endDate = new DateTime(to);

                //Cycle through the dates starting with the initial startdate and closing when the end date is reached
                for(DateTime currentdate=startDate; currentdate.isBefore(endDate);currentdate=currentdate.plusDays(1)) {
                    String currentDatestr =currentdate.toString().substring(0,10);
                    String dateSelectedStr = dateSelected.toString().substring(0,10);
                    if (currentDatestr.equals(dateSelectedStr)) {
                        selectedList.add(t); //Add to the custom list of entries
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

}
