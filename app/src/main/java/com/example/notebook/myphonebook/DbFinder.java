package com.example.notebook.myphonebook;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;

/**
 * Created by NoteBook on 12.12.2016.
 */
public class DbFinder {
    private static DbFinder ourInstance = new DbFinder();
    private static DataBaseHelper myDbHelper;
    private static Context myContext;
    private static SQLiteDatabase db;

    public static DbFinder getInstance(Context context) {
        if(myContext==null) {
            myContext = context;
            init();
        }
        return ourInstance;
    }
    private static void init(){
        myDbHelper = new DataBaseHelper(myContext);

        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        db = myDbHelper.getReadableDatabase();

    }

    private DbFinder() {

    }

    public Cursor findByPhone(String phoneToFind){
       return db.rawQuery("select * from person where phone = ?", new String[]{phoneToFind});
    }

    public Cursor findById(String id){
        return db.rawQuery("select * from person where id_person = ?", new String[]{id});
    }
}
