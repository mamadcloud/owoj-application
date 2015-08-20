package com.thehasnibrothers.owoj.Pages;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.thehasnibrothers.owoj.Databases.DatabaseHelper;
import com.thehasnibrothers.owoj.Helper.HttpRequest;
import com.thehasnibrothers.owoj.Models.DefaultJuz;
import com.thehasnibrothers.owoj.Models.Owner;
import com.thehasnibrothers.owoj.Models.Owoj;
import com.thehasnibrothers.owoj.R;
import com.thehasnibrothers.owoj.Services.OwnerService;
import com.thehasnibrothers.owoj.Services.OwojService;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by user on 16/4/2015.
 */
public class ListPage extends Fragment {
    View currentView;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat waDateFormat = new SimpleDateFormat("dd MMM yyyy");
    private OwnerService ownerService;
    private DatabaseHelper dbHelper;
    TextView khatam;
    Button doneButton;
    Activity activity;
    DefaultJuz defaultJuz;
    Owoj owoj;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.list_layout, container, false);
        ownerService = new OwnerService();
        activity = this.getActivity();
        dbHelper = new DatabaseHelper(activity);
        defaultJuz = new DefaultJuz();
        //setPage();

        (new GetRequest()).execute("http://owoj-api.dedinar.com/api/v1/owoj/index.json");
        return currentView;
    }

    public void setPage()
    {
        khatam = (TextView) currentView.findViewById(R.id.khatamNo);

        khatam.setText("Khataman ke " + owoj.getKhatamNo());

        TableLayout tableLayout = (TableLayout)currentView.findViewById(R.id.memberOwoj);

        try {
            for (Map.Entry<String, Integer> entry : defaultJuz.getDefaultJuzs().entrySet()) {
                TableRow row = new TableRow(getActivity());

                int modJuz = (entry.getValue() + owoj.getKhatamNo() - 1) % 30;

                String juzNo = String.valueOf(modJuz).equals("0") ? "30" : String.valueOf(modJuz);

                String juzDate = owoj.getJuz((modJuz == 0 ? 30 : modJuz) - 1, "yyyy-MM-dd");

                boolean isDone = (juzDate != null && !juzDate.isEmpty());

                TextView c1 = new TextView(getActivity());
                c1.setText(entry.getKey());
                c1.setTextSize(9);
                c1.setPadding(5, 5, 5, 5);

                TextView c2 = new TextView(getActivity());
                c2.setText(juzNo);
                c2.setGravity(Gravity.CENTER);
                c2.setTextSize(9);
                c2.setPadding(5, 5, 5, 5);

                TextView c3 = new TextView(getActivity());
                c3.setText((isDone ? "Sudah" : "Belum"));
                c3.setTextSize(9);
                c3.setPadding(5, 5, 5, 5);

                TextView c4 = new TextView(getActivity());
                c4.setText(juzDate);
                c4.setTextSize(9);
                c4.setPadding(5, 5, 5, 5);

                Button c5 = new Button(getActivity());
                c5.setText((isDone ? "Batalkan" : "Selesai?"));
                c5.setOnClickListener(getOnClickDoSomething(c5, c3, c4, modJuz == 0 ? 30 : modJuz));
                c5.setTextSize(9);
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                c4.setLayoutParams(params);
                row.addView(c1);
                row.addView(c2);
                row.addView(c3);
                row.addView(c4);
                row.addView(c5);

                tableLayout.addView(row);
            }

            Button share = (Button) currentView.findViewById(R.id.shareWhatsapp);
            share.setOnClickListener(buttonShareOnWhatsApp());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void shareOnWhatsApp()
    {
        StringBuilder builder = new StringBuilder();
        Intent waIntent = new Intent(Intent.ACTION_SEND);
        waIntent.setType("text/plain");
        builder.append("OWOJ Bin Achmad Assegaff\n");
        builder.append("Khataman ke " + String.valueOf(owoj.getKhatamNo()) + "\n");
        builder.append("( " + waDateFormat.format(owoj.getStartDate()).replace("Aug","Agu").replace("Oct","Okt").replace("Dec", "Des") + " - ");
        builder.append(waDateFormat.format(owoj.getEndDate()).replace("Aug","Agu").replace("Oct","Okt").replace("Dec", "Des") + ")\n");

        for (Map.Entry<String, Integer> entry : defaultJuz.getDefaultJuzs().entrySet()) {
            int modJuz = (entry.getValue() + owoj.getKhatamNo() - 1) % 30;
            String juzNo = String.valueOf(modJuz).equals("0") ? "30" : String.valueOf(modJuz);
            String juzDate = owoj.getJuz((modJuz == 0 ? 30 : modJuz) - 1, "dd MMM");
            boolean isDone = (juzDate != null && !juzDate.isEmpty());

            builder.append("juz " + juzNo + ": ");
            builder.append(entry.getKey()+ ": ");
            builder.append((isDone?"v, "+juzDate + " done":""));
            builder.append("\n");
        }


        //PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
        //Check if package exists or not. If not then code
        //in catch block will be called
        waIntent.setPackage("com.whatsapp");

        waIntent.putExtra(Intent.EXTRA_TEXT, builder.toString());
        startActivity(Intent.createChooser(waIntent, "Share with"));
    }

    View.OnClickListener buttonShareOnWhatsApp()  {
        return new View.OnClickListener() {
            public void onClick(View v) {
                (new GetShareRequest()).execute("http://owoj-api.dedinar.com/api/v1/owoj/index.json");
            }
        };
    }

    View.OnClickListener getOnClickDoSomething(final Button button, final TextView text1, final TextView text2, final int juzNo)  {
        return new View.OnClickListener() {
            public void onClick(View v) {
                //button.setVisibility(View.GONE);
                boolean done = !button.getText().toString().toLowerCase().equals("batalkan");
                text1.setText(done?"Sudah":"Belum");
                button.setText(done?"Batalkan":"Selesai?");
                (new PutRequest(juzNo, text2, done)).execute("http://owoj-api.dedinar.com/api/v1/owoj/index.json");
            }
        };
    }

    public class GetShareRequest extends AsyncTask<String, Void, String> {
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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return builder.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject reader = new JSONObject(result);
                JSONObject jsonOwoj  = reader.getJSONObject("owojs");
                owoj = (new OwojService()).parseJson(jsonOwoj);

                shareOnWhatsApp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return builder.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject reader = new JSONObject(result);
                JSONObject jsonOwoj  = reader.getJSONObject("owojs");
                owoj = (new OwojService()).parseJson(jsonOwoj);

                setPage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class PutRequest extends AsyncTask<String, Void, String> {
        private int juzNo;
        private TextView textView;
        private String dateDone;
        private boolean done = false;

        public PutRequest(int no, TextView text, boolean isDone)
        {
            juzNo = no;
            textView = text;
            done = isDone;
        }

        protected String doInBackground(String... address) {
            StringBuilder builder = new StringBuilder();
            try {
                HttpURLConnection response = new HttpRequest(address[0]).putRequest();
                Date date = new Date();
                dateDone = dateFormat.format(date);
                JSONObject owojChilds = new JSONObject();
                JSONObject owojRoot = new JSONObject();
                owojChilds.put("khatamNo", owoj.getKhatamNo());
                owojChilds.put("id", owoj.getId());
                owojChilds.put("juz"+String.valueOf(juzNo), (done?dateDone:"cancel"));
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
                if(done)
                    textView.setText(dateDone);
                else textView.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
