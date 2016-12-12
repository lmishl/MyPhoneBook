package com.example.notebook.myphonebook;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.CallLog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void buttonReadCallsClick(View view) {
        Intent intent = new Intent(MainActivity.this, CallsLogsActivity.class);
        startActivity(intent);

        //Intent intent = new Intent(Intent.ACTION_VIEW);
       // intent.setType(CallLog.Calls.CONTENT_TYPE);
        //intent.putExtra(CallLog.Calls.EXTRA_CALL_TYPE_FILTER, CallLog.Calls.MISSED_TYPE);
       // startActivity(intent);
    }

    public void findPhone(String phoneToFind)
    {
        DbFinder finder = DbFinder.getInstance(this);

        Cursor c = finder.findByPhone(phoneToFind);
        if (c.getCount() == 0)
            Toast.makeText(this, "Ничего не найдено", Toast.LENGTH_SHORT).show();
        else {

            if (c.getCount() == 1) {

                Intent intent = new Intent(MainActivity.this, PersonInfoActivity.class);
                c.moveToNext();
                intent.putExtra("id", c.getString(0));
                startActivity(intent);


            } else {


                ArrayList<String> ids = new ArrayList<>();
                while (c.moveToNext()) {
                    ids.add(c.getString(0));
                }

                Intent intent = new Intent(MainActivity.this, FoundListActivity.class);
                intent.putExtra("ids", ids);
                startActivity(intent);
            }


        }
        c.close();


    }

    public void buttonFindClick(View view) {

        String phoneToFind = ((EditText) findViewById(R.id.editText2)).getText().toString();

        findPhone(phoneToFind);

    }

}
