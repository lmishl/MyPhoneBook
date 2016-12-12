package com.example.notebook.myphonebook;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FoundListActivity extends AppCompatActivity {

    DataBaseHelper myDbHelper;
    ArrayList<String> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_list);

        myDbHelper = new DataBaseHelper(this);
        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        SQLiteDatabase db = myDbHelper.getReadableDatabase();




        Intent intent = getIntent();
        ids = intent.getStringArrayListExtra("ids");
        ArrayList<String>  fio = new ArrayList<String>();
        for (String id : ids) {
            Cursor c = db.rawQuery("select * from person where id_person = ?", new String[]{id});
            c.moveToNext();
            fio.add(c.getString(1) + " " + c.getString(2) + " " + c.getString(3));

            c.close();


        }

        ArrayAdapter<String> adapter  =  new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, fio);


        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //String Selecteditem= itemname[+position];
                //Toast.makeText(getApplicationContext(), Selecteditem, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FoundListActivity.this, PersonInfoActivity.class);

                intent.putExtra("id", ids.get(position));
                startActivity(intent);


            }
        });


    }
}
