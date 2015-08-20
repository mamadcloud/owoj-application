package com.thehasnibrothers.owoj;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.thehasnibrothers.owoj.Databases.DatabaseHelper;
import com.thehasnibrothers.owoj.Models.DefaultJuz;
import com.thehasnibrothers.owoj.Models.Owner;
import com.thehasnibrothers.owoj.Services.OwnerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class NameSettingActivity extends ActionBarActivity {

    private Spinner spinner;
    private OwnerService ownerService;
    private DatabaseHelper dbHelper;
    private Owner owner;
    private DefaultJuz defaultJuz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_setting);
        CharSequence title = "Ganti Nama";
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        ownerService = new OwnerService();
        dbHelper = new DatabaseHelper(this);

        owner = ownerService.getOwner(dbHelper);
        defaultJuz = new DefaultJuz();

        addListenerOnButton();
        addItemsOnSpinner();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //onBackPressed();
                Intent i = new Intent(NameSettingActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        spinner.setSelection(dataAdapter.getPosition(owner.getName()));
    }

    // get the selected dropdown list value
    public void addListenerOnButton() {
        Button selectButton = (Button) findViewById(R.id.selectButton);
        spinner = (Spinner) findViewById(R.id.namePicker);

        selectButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(NameSettingActivity.this,
                        "Nama yang anda pilih: " + String.valueOf(spinner.getSelectedItem()) +
                        "\nNama sudah terganti",
                        Toast.LENGTH_SHORT).show();

                owner.setName(String.valueOf(spinner.getSelectedItem()));
                owner.setJuz(defaultJuz.getDefaultJuz(owner.getName()).getValue());
                ownerService.updateOwner(owner, dbHelper);
            }

        });
    }
}
