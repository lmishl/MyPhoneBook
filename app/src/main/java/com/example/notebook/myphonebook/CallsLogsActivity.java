package com.example.notebook.myphonebook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

        getCallDetails();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, calls);
        ListView listView = (ListView) findViewById(R.id.listViewCalls);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                findPhone(phones.get(position));

            }
        });
    }

    public void findPhone(String phoneToFind) {
        DbFinder finder = DbFinder.getInstance(this);

        Cursor c = finder.findByPhone(phoneToFind);
        if (c.getCount() == 0)
            Toast.makeText(this, "Ничего не найдено", Toast.LENGTH_SHORT).show();
        else {

            if (c.getCount() == 1) {

                Intent intent = new Intent(this, PersonInfoActivity.class);
                c.moveToNext();
                intent.putExtra("id", c.getString(0));
                startActivity(intent);


            } else {

                ArrayList<String> ids = new ArrayList<>();
                while (c.moveToNext()) {
                    ids.add(c.getString(0));
                }

                Intent intent = new Intent(this, FoundListActivity.class);
                intent.putExtra("ids", ids);
                startActivity(intent);
            }
        }
        c.close();
    }

    private void getCallDetails() {
        //StringBuffer sb = new StringBuffer();
        String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
  /* Query the CallLog Content Provider */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
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
                    callType = "Исходящий";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    callType = "Входящий";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    callType = "Пропущенный";
                    break;
        }
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
            String oneCall = "\nPhone Number:--- " + phNum + " \nCall Type:--- "
                    + callType + " \nCall Date:--- " + dateFormat.format(callDate)
                    + " \nCall duration in sec :--- " + callDuration;
            calls.add(oneCall);
        }
        managedCursor.close();
    }
}
