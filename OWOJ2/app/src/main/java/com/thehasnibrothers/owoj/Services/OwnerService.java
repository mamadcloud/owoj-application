package com.thehasnibrothers.owoj.Services;

import com.thehasnibrothers.owoj.Databases.DatabaseConfig;
import com.thehasnibrothers.owoj.Databases.DatabaseHelper;
import com.thehasnibrothers.owoj.Models.Owner;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 17/4/2015.
 */
public class OwnerService {
    public long addOwner(Owner owner, DatabaseHelper dbHelper)
    {
        if(getOwners(dbHelper).size() <= 0) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DatabaseConfig.COLUMN_NAME, owner.getName());
            values.put(DatabaseConfig.COLUMN_JUZ, owner.getJuz());

            long id = db.insert(DatabaseConfig.TABLE_OWNER, null, values);

            return id;
        }
        else
        {
            return 0;
        }
    }

    public List<Owner> getOwners(DatabaseHelper dbHelper)
    {
        List<Owner> owners = new ArrayList<Owner>();

        String sqlQuery = "SELECT * FROM " + DatabaseConfig.TABLE_OWNER;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery,null);

        if(cursor.moveToFirst())
        {
            do{
                Owner owner= new Owner();
                owner.setID(cursor.getInt(cursor.getColumnIndex(DatabaseConfig.COLUMN_ID)));
                owner.setName(cursor.getString(cursor.getColumnIndex(DatabaseConfig.COLUMN_NAME)));
                owners.add(owner);
            }
            while (cursor.moveToNext());
        }
        return owners;
    }

    public Owner getOwner(DatabaseHelper dbHelper)
    {
        Owner owner = new Owner();

        String sqlQuery = "SELECT * FROM " + DatabaseConfig.TABLE_OWNER;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery,null);

        if(cursor.moveToFirst())
        {
            do{
                owner.setID(cursor.getInt(cursor.getColumnIndex(DatabaseConfig.COLUMN_ID)));
                owner.setName(cursor.getString(cursor.getColumnIndex(DatabaseConfig.COLUMN_NAME)));
                owner.setJuz(cursor.getInt(cursor.getColumnIndex(DatabaseConfig.COLUMN_JUZ)));
            }
            while (cursor.moveToNext());
        }
        return owner;
    }

    public boolean updateOwner(Owner owner, DatabaseHelper dbHelper)
    {
        boolean result = false;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConfig.COLUMN_NAME, owner.getName());
        values.put(DatabaseConfig.COLUMN_JUZ, owner.getJuz());

        String[] params = new String[]{String.valueOf(owner.getID())};
        db.update(DatabaseConfig.TABLE_OWNER, values, "id=?", params);
        return result;
    }
}
