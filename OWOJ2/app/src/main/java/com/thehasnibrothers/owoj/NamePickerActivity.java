package com.thehasnibrothers.owoj;

import android.app.Activity;
import android.content.Intent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.View;

import com.thehasnibrothers.owoj.Databases.DatabaseHelper;
import com.thehasnibrothers.owoj.Models.DefaultJuz;
import com.thehasnibrothers.owoj.Models.Owner;
import com.thehasnibrothers.owoj.Services.OwnerService;

public class NamePickerActivity extends Activity {

    private Spinner spinner;
    private OwnerService ownerService;
    private DatabaseHelper dbHelper;
    private DefaultJuz defaultJuz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ownerService = new OwnerService();
        dbHelper = new DatabaseHelper(this);
        defaultJuz  = new DefaultJuz();

        if(ownerService.getOwners(dbHelper).size() > 0)
        {
            endActivity();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.namepicker_activity);
        addListenerOnButton();
        addItemsOnSpinner();


    }

    public void endActivity()
    {
        Intent i = new Intent(NamePickerActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void addItemsOnSpinner() {

        spinner = (Spinner) findViewById(R.id.namePicker);
        List<String> list = new ArrayList<String>();

        for (Map.Entry<String, Integer> entry : defaultJuz.getDefaultJuzs().entrySet()) {
            list.add(entry.getKey());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    // get the selected dropdown list value
    public void addListenerOnButton() {
        Button selectButton = (Button) findViewById(R.id.selectButton);
        spinner = (Spinner) findViewById(R.id.namePicker);
        selectButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(NamePickerActivity.this,
                        "Nama yang anda pilih: " + String.valueOf(spinner.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();
                Owner owner = new Owner();
                owner.setName(String.valueOf(spinner.getSelectedItem()));
                owner.setJuz(defaultJuz.getDefaultJuz(owner.getName()).getValue());
                ownerService.addOwner(owner, dbHelper);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        endActivity();
                    }
                }, 1000);
            }

        });
    }
}
