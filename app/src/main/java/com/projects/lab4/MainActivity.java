package com.projects.lab4;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ImageButton mic;
    TextView hint;
    Button go;
    final int VOICE_RECOGNITION =100;
    DBManager dbManager;
    //TextView resultText;


    private void openDB(){
        dbManager = new DBManager(this);
        dbManager.open();

    }
    private void closeDB(){
        dbManager.close();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       mic = (ImageButton) findViewById(R.id.imageButton);
       hint = (TextView) findViewById(R.id.textView);
       go = (Button) findViewById(R.id.buttonGo);
        openDB();
       //resultText = (TextView) findViewById(R.id.outputText);
        //dbManager = new DBManager(this);


       mic.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               promptSpeech();
               //onActivityResult();
              /* Intent intent = new Intent(MainActivity.this,EditActivity.class);
               startActivity(intent);*/
           }
       });

       go.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this,EventsActivity.class);
               startActivity(intent);
           }
       });



    }

    public void promptSpeech(){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Go Ahead");
        i.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        try {
            startActivityForResult(i,VOICE_RECOGNITION);

        }
        catch (ActivityNotFoundException a){
            Toast.makeText(getApplicationContext(),"Sorry your Device is not supported", Toast.LENGTH_LONG).show();
        }
    }

    public void onActivityResult(int request_code, int resultCode , Intent data){

        if(request_code == VOICE_RECOGNITION){
            if(resultCode == RESULT_OK && data != null){
                String daysFormat ="(Sunday|Monday|Tuesday|Wednesday|Thursday|Friday|Saturday)";
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                String days = getDay(result.get(0));
                String [] splittedResults = result.get(0).split(daysFormat);
                if(splittedResults.length >1 && days!= null){

                    if(getDay(splittedResults[1]) == null){
                        String event = splittedResults[0];
                        String day = getDay(splittedResults[1]);
                        String dayNo = getDayFormat(splittedResults[1]);
                        String month = getMonthFormat(splittedResults[1]);
                        String year = getYearFormat(splittedResults[1]);
                        String time = getTimeFormat(splittedResults[1]);

                        if(event!=null && month!=null && dayNo!=null && year!=null && time!=null) {
                            Intent intent = new Intent(getBaseContext(), EditActivity.class);
                            intent.putExtra("Days", days);
                            intent.putExtra("Event", event);
                            intent.putExtra("Day", days);
                            intent.putExtra("DayNo", dayNo);
                            intent.putExtra("Month", month);
                            intent.putExtra("Year", year);
                            intent.putExtra("Time", time);
                            startActivity(intent);
                            finish();
                        }
                    }
                }

                else
                {
                    Toast.makeText(getBaseContext(),"Invalid Format. Be Specific!",Toast.LENGTH_LONG).show();
                }




            }

        }
    }

    private String getDay(String result)
    {
        if (result.contains("Sunday"))
            return "Sunday";
        if (result.contains("Monday"))
            return "Monday";
        if (result.contains("Tuesday"))
            return "Tuesday";
        if (result.contains("Wednesday"))
            return "Wednesday";
        if (result.contains("Thursday"))
            return "Thursday";
        if (result.contains("Friday"))
            return "Friday";
        if (result.contains("Saturday"))
            return "Saturday";
        return null;
    }

    private String getDayFormat(String date)
    {
        String dayFormat = "[0-9]{1,2}";
        Pattern regex = Pattern.compile(dayFormat);
        Matcher matcher = regex.matcher(date);
        if(matcher.find())
        {
            return matcher.group(0);

        }
        Toast.makeText(getBaseContext(),"No Day Added",Toast.LENGTH_LONG).show();
        return null;
    }

    private String getMonthFormat(String date)
    {
        String monthFormat = "(January|February|March|April|May|June|July|August|September|October|November|December)";
        Pattern regex = Pattern.compile(monthFormat);
        Matcher matcher = regex.matcher(date);
        if(matcher.find())
        {
            return matcher.group(0);
        }
        Toast.makeText(getBaseContext(),"No Month Added",Toast.LENGTH_LONG).show();
        return null;
    }

    private String getYearFormat(String date)
    {
        String yearFormat = "[0-9]{4}";
        Pattern regex = Pattern.compile(yearFormat);
        Matcher matcher = regex.matcher(date);
        if(matcher.find())
        {
            return matcher.group(0);

        }
        Toast.makeText(getBaseContext(),"No Year Added",Toast.LENGTH_LONG).show();
        return null;
    }

    private String getTimeFormat(String date)
    {
        String []results = date.split(" at ");
        if(results.length > 1 && results[1] != "")
        {
            if(results[1].contains("p.m.")||results[1].contains("a.m.") ) {
                return results[1];
            }
            else
            {
                Toast.makeText(getBaseContext(),"No Period Added",Toast.LENGTH_LONG).show();
                return null;
            }
        }
        Toast.makeText(getBaseContext(),"No Time Added",Toast.LENGTH_LONG).show();
        return null;
    }


}
