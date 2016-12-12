package com.example.notebook.myphonebook;

import android.content.Intent;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class CallsLogsActivity extends AppCompatActivity {

    TextView textView = null;
    ArrayList<String> phones;
    ArrayList<String> calls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calls_logs);

        //textView = (TextView) findViewById(R.id.textview_call);
        getCallDetails();

        ArrayAdapter<String> adapter  =  new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, calls);
        ListView listView = (ListView) findViewById(R.id.listViewCalls);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //String Selecteditem= itemname[+position];
                //Toast.makeText(getApplicationContext(), Selecteditem, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CallsLogsActivity.this, PersonInfoActivity.class);

                intent.putExtra("id", phones.get(position));
                startActivity(intent);


            }
        });
    }

    private void getCallDetails() {
        //StringBuffer sb = new StringBuffer();
        String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
  /* Query the CallLog Content Provider */
        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,
                null, null, strOrder);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        //sb.append("Call Log :");
        phones = new ArrayList<String>();
        calls = new ArrayList<String>();
        while (managedCursor.moveToNext()) {
            String phNum = managedCursor.getString(number);
            if(phNum.startsWith("8"))
                phones.add(phNum.substring(1));
            else
                if(phNum.startsWith("+7"))
                    phones.add(phNum.substring(2));
            else
                    phones.add(phNum);
            String callTypeCode = managedCursor.getString(type);
            String strcallDate = managedCursor.getString(date);
            Date callDate = new Date(Long.valueOf(strcallDate));
            String callDuration = managedCursor.getString(duration);
            String callType = null;
            int callcode = Integer.parseInt(callTypeCode);
            switch (callcode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    callType = "Outgoing";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    callType = "Incoming";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    callType = "Missed";
                    break;
            }
            String oneCall = "\nPhone Number:--- " + phNum + " \nCall Type:--- "
                    + callType + " \nCall Date:--- " + callDate
                    + " \nCall duration in sec :--- " + callDuration;
            //sb.append("\n----------------------------------");
            calls.add(oneCall);
        }
        managedCursor.close();
       // textView.setText(sb);
    }
}
