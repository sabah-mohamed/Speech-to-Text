package com.projects.lab4;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EventsActivity extends AppCompatActivity {

    ListView lv;
    MyCustomAdapter myCustomAdapter;
    DBManager dbManager;
    ArrayList<EventList> eventList = new ArrayList<EventList>();
    boolean doubleClick = false;
    //EditActivity editActivity;

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
        setContentView(R.layout.activity_events);

        //dbManager = new DBManager(this);
        openDB();
        eventList = returnData();
        lv = (ListView) findViewById(R.id.eventList);
        myCustomAdapter = new MyCustomAdapter(eventList);
        lv.setAdapter(myCustomAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (doubleClick) {
                    EventList d = (EventList) lv.getItemAtPosition(position);
                    dbManager.deleteRow(lv.getItemIdAtPosition(position));
                    eventList.remove(d);
                    myCustomAdapter.notifyDataSetChanged();

                }

                doubleClick = true;
                Toast.makeText(getBaseContext(), "Press Again To Delete", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleClick = false;
                    }
                }, 2000);
            }
        });

    }

        private class MyCustomAdapter extends BaseAdapter{

        public ArrayList<EventList> eventList;

        public MyCustomAdapter(ArrayList<EventList> eventList) {
            //super(ctx,eventList);

            this.eventList = eventList;
        }

        @Override
        public int getCount() {
            return eventList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.event_list, null);
            final EventList events = eventList.get(position);

            TextView eventname = (TextView) view.findViewById(R.id.event);
            eventname.setText(events.getEventName());

            TextView eventdate = (TextView) view.findViewById(R.id.date);
            eventdate.setText(events.getEventDate());

            TextView eventtime = (TextView) view.findViewById(R.id.time);
            eventtime.setText(events.getEventTime());

            return view;
        }
    }

    private ArrayList<EventList> returnData()
    {
        ArrayList<EventList> eventEntry = new ArrayList<EventList>();
        Cursor cursor = dbManager.getAllRows();
        if (cursor.moveToFirst())
        {
            do{
                int id = cursor.getInt(dbManager.COL_ROWID);
                String nameP = cursor.getString(dbManager.COL_USERNAME);
                String time = cursor.getString(dbManager.COL_TIME);
                String dateP = cursor.getString(dbManager.COL_DATE);
                eventEntry.add(new EventList(nameP, time, dateP));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return eventEntry;
    }
}
