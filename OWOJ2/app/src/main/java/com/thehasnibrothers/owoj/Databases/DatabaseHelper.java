package com.thehasnibrothers.owoj.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.thehasnibrothers.owoj.Models.Owner;
/**
 * Created by user on 16/4/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context)
    {
        super(context, DatabaseConfig.DATABASE_NAME, null, DatabaseConfig.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DatabaseConfig.TABLE_OWNER +
                        "(" + DatabaseConfig.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        DatabaseConfig.COLUMN_NAME + " TEXT NULL," +
                        DatabaseConfig.COLUMN_JUZ + " INTEGER NULL);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXIST " + DatabaseConfig.TABLE_OWNER);

        onCreate(db);
    }
}
