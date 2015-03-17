package com.example.student.myfriends;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class SqlLiteDbHelper extends SQLiteOpenHelper {




    private static final int DATABASE_VERSION = 1;
    // Database Name
    String dbname="";
    String s="";

    private static final String DB_PATH_SUFFIX = "/databases/";
    protected static Context ctx;

    public SqlLiteDbHelper(Context context,String name) {

        super(context,name, null, DATABASE_VERSION);
        ctx = context;
        this.dbname = name;
        int namee = dbname.lastIndexOf('.');   //try the other place
        if(namee>=0) {
            s = dbname.substring(0,namee);
        }else {
            s="s";
            Log.i("MY","s is "+s);
        }


    }


    // Getting single contact
    public Friends Get_ContactDetails(int n) {
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT * FROM "+s, null);


        if (cursor != null && cursor.move(n)){
            Friends cont = new Friends(cursor.getInt(0),cursor.getString(1), cursor.getString(2),cursor.getString(3));
// return contact
            cursor.close();
            db.close();

            return cont;

        }
        return null;
    }

    public List Get_Name() {
        List names = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+s, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Friends comment = cursorToComment(cursor);
            names.add(comment);  //add list
            cursor.moveToNext();
        }
// Make sure to close the cursor
        cursor.close();
        return names;
    }

    private Friends cursorToComment(Cursor cursor) {
        Friends comment = new Friends();
        comment.setID(cursor.getInt(0));
        comment.setName(cursor.getString(1));
        comment.setEmail(cursor.getString(2));
        comment.setMobileNo(cursor.getString(3));
        return comment;
    }

    public void CopyDataBaseFromAsset() throws IOException{

        InputStream myInput = ctx.getAssets().open(dbname);

// Path to the just created empty db
        String outFileName = getDatabasePath();

// if the path doesn't exist first, create it
        File f = new File(ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        if (!f.exists())
            f.mkdir();

// Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

// transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

// Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }
    private  String getDatabasePath() {
        return ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX
                + dbname;
    }

    public SQLiteDatabase openDataBase() throws SQLException{
        File dbFile = ctx.getDatabasePath(dbname);

        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                System.out.println("Copying success from Assets folder");
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }

        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// TODO Auto-generated method stub

    }
}
