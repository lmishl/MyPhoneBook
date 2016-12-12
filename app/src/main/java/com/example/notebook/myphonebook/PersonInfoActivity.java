package com.example.notebook.myphonebook;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import static com.example.notebook.myphonebook.R.id.textView;

public class PersonInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);

        DbFinder finder = DbFinder.getInstance(this);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Cursor c = finder.findById(id);
        c.moveToNext();

        TextView lastnameView = (TextView) findViewById(R.id.textViewLastName);
        lastnameView.setText(c.getString(1));
        ((TextView)findViewById(R.id.textViewFirstName)).setText(c.getString(2));
        ((TextView)findViewById(R.id.textViewMiddleName)).setText(c.getString(3));
        ((TextView)findViewById(R.id.textViewBirthday)).setText(c.getString(4));
        ((TextView)findViewById(R.id.textViewAddress)).setText(c.getString(6));
        ((TextView)findViewById(R.id.textViewPhone)).setText(c.getString(5));
    }
}
