package labstuff.gcu.me.org.assessment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * Created by Christopher Dillon
 *  Student ID: S1514278
 */

public class DetailActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);

        //set the back (up) button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //find all our view components
        ImageView imageView = (ImageView) findViewById(R.id.image);
        TextView titleTV = (TextView) findViewById(R.id.title);
        TextView descriptionTV = (TextView) findViewById(R.id.description);
        TextView linkTV = (TextView) findViewById(R.id.link);
        TextView georssTV = (TextView) findViewById(R.id.georss);
        TextView authorTV = (TextView) findViewById(R.id.author);
        TextView commentsTV = (TextView) findViewById(R.id.comments);
        TextView pubDateTV = (TextView) findViewById(R.id.pubDate);

        //collect our intent and populate our layout
        Intent intent = getIntent();

        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String link = intent.getStringExtra("link");
        String georss = intent.getStringExtra("georss");
        String author = intent.getStringExtra("author");
        String comments = intent.getStringExtra("comments");
        String pubDate = intent.getStringExtra("pubDate");

        //set elements
        titleTV.setText(title);
        descriptionTV.setText(description);
        linkTV.setText("Link: " + link);
        georssTV.setText("Georss: " + georss);
        authorTV.setText("Author: " + author);
        commentsTV.setText("Comments: " + comments);
        pubDateTV.setText("Date Published: " + pubDate);
    }

    //Handle back button navigation
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



}