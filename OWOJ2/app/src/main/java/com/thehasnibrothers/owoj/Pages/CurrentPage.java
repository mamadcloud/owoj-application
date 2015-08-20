package com.thehasnibrothers.owoj.Pages;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.thehasnibrothers.owoj.Databases.DatabaseHelper;
import com.thehasnibrothers.owoj.Helper.HttpRequest;
import com.thehasnibrothers.owoj.Models.Owner;
import com.thehasnibrothers.owoj.R;
import com.thehasnibrothers.owoj.Services.OwnerService;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 16/4/2015.
 */
public class CurrentPage extends Fragment {
    View currentView;

    private OwnerService ownerService;
    private DatabaseHelper dbHelper;
    private Owner owner;
    TextView khatam, juzNo, doneText, juzText;
    Button doneButton;
    Activity activity;
    int currentKhatam = 1;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState)
    {
        currentView = inflater.inflate(R.layout.current_layout, container, false);
        ownerService = new OwnerService();
        activity = this.getActivity();
        dbHelper = new DatabaseHelper(activity);
        owner = ownerService.getOwner(dbHelper);
        //setPage();

        getUpdate();
        return currentView;
    }

    public void setPage(boolean isDone)
    {
        khatam = (TextView) currentView.findViewById(R.id.khatamNo);
        juzNo = (TextView) currentView.findViewById(R.id.juzNo);
        doneText = (TextView) currentView.findViewById(R.id.doneText);
        doneButton = (Button) currentView.findViewById(R.id.doneButton);
        juzText = (TextView) currentView.findViewById(R.id.juzText);

        khatam.setText("Khataman ke " + currentKhatam);
        juzText.setText("Juz Sekarang");
        int modJuz = (owner.getJuz() + currentKhatam - 1)%30;

        juzNo.setText(String.valueOf(modJuz).equals("0")?"30":String.valueOf(modJuz));
        if(isDone) {
            doneText.setVisibility(View.VISIBLE);
            doneButton.setText("Batalkan");
        }
        doneButton.setVisibility(View.VISIBLE);
        //else{
            doneButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //Toast.makeText(activity, "Khatam", Toast.LENGTH_SHORT).show();
                    boolean done = false;
                    done = !doneButton.getText().toString().toLowerCase().equals("batalkan");
                    (new PutRequest(done)).execute("http://owoj-api.dedinar.com/api/v1/owoj/index.json");
                    //doneButton.setVisibility(View.GONE);
                    doneButton.setText((done ? "Batalkan" : "Selesai?"));
                    if (done) doneText.setVisibility(View.VISIBLE);
                    else doneText.setVisibility(View.GONE);
                }

            });
        //}
    }

    public void failRequest()
    {
        juzText = (TextView) currentView.findViewById(R.id.juzText);
        juzText.setText("Koneksi ke internet gagal..");
    }

    public void getUpdate()
    {
        (new GetRequest()).execute("http://owoj-api.dedinar.com/api/v1/owoj/index.json");
    }

    public class GetRequest extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... address) {
            StringBuilder builder = new StringBuilder();
            try {
                HttpURLConnection response = new HttpRequest(address[0]).getRequest();
                int statusCode = response.getResponseCode();
                if (statusCode == 200) {
                    InputStream content = response.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                } else {
                    builder.append("error");
                }
            } catch (Exception e) {
                builder.append("error");
            }
            return builder.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if(result.equals("error"))
                {
                    failRequest();
                }
                else
                {
                    JSONObject reader = new JSONObject(result);
                    JSONObject jsonOwoj = reader.getJSONObject("owojs");
                    currentKhatam = jsonOwoj.getInt("id");
                    int modJuz = (owner.getJuz() + currentKhatam - 1) % 30;
                    String j = jsonOwoj.getString("juz" + (String.valueOf(modJuz).equals("0") ? "30" : String.valueOf(modJuz)));
                    setPage(!j.isEmpty());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class PutRequest extends AsyncTask<String, Void, String> {
        private boolean done = false;

        public PutRequest(boolean isDone)
        {
            done = isDone;
        }

        protected String doInBackground(String... address) {
            StringBuilder builder = new StringBuilder();
            try {
                HttpURLConnection response = new HttpRequest(address[0]).putRequest();
                //int statusCode = response.getResponseCode();
                //if (statusCode == 200) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();

                int modJuz = (owner.getJuz() + currentKhatam - 1)%30;
                JSONObject owojChilds = new JSONObject();
                JSONObject owojRoot = new JSONObject();
                owojChilds.put("khatamNo", currentKhatam);
                owojChilds.put("id", currentKhatam);
                owojChilds.put("juz"+String.valueOf(modJuz), (done?dateFormat.format(date):"cancel"));
                owojRoot.put("owojs", owojChilds);
                OutputStreamWriter wr= new OutputStreamWriter(response.getOutputStream());
                wr.write(owojRoot.toString());
                wr.flush();
                wr.close();

                InputStream content = response.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                //} else {
                //}
            } catch (Exception e) {
                e.printStackTrace();
            }
            return builder.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
