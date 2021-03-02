package labstuff.gcu.me.org.assessment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * Created by Christopher Dillon
 *  Student ID: S1514278
 */

class trafficArrayAdapter extends ArrayAdapter<Traffic> implements Serializable {

    private Context context;
    private ArrayList<Traffic> trafficList;

    //constructor, call on creation
    public trafficArrayAdapter(Context context, int resource, List<Traffic> objects) {
        super(context, resource, objects);

        this.context = context;
        this.trafficList = (ArrayList)objects;
    }


    //called when rendering the list
    public View getView(int position, View convertView, ViewGroup parent) {

        //get the traffic object (incident or roadwork) we are displaying
        Traffic traffic = trafficList.get(position);

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.taffic_layout, null);

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView description = (TextView) view.findViewById(R.id.description);
        TextView colourSpace = (TextView) view.findViewById(R.id.colourSpace);

        String firstDate = "";
        String secondDate = "";

        if (traffic.getDescription().contains("\n")) { //Check if we are reading a roadworks feed
            String pattern = " EEEE, dd MMMM yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            String[] parts = traffic.getDescription().split("\n"); //Split the string in order to seperate the dates
            firstDate = parts[0];
            secondDate = parts[1];

            try {
                Date date = simpleDateFormat.parse(firstDate);
                Date date2 = simpleDateFormat.parse(secondDate);
                long diff = Math.abs(date.getTime() - date2.getTime());
                long diffDays = diff / (24 * 60 * 60 * 1000);
                if (diffDays != 1) {
                    diffDays = diffDays + 1; //calc the difference in days between each entry
                }

                //Set the background colour based on days of completion
                if (diffDays <= 1) {
                    colourSpace.setBackgroundColor(Color.GREEN);
                } else if(diffDays > 1 && diffDays <= 7) {
                    colourSpace.setBackgroundColor(Color.YELLOW);
                }
                else
                {
                    colourSpace.setBackgroundColor(Color.RED);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else
        {
            description.setText(traffic.getDescription()); //Set the description as normal if it is a incident object
        }
        title.setText(traffic.getTitle()); //Set the title regardless of object
        description.setText(traffic.getDescription());
        return view;
    }
}