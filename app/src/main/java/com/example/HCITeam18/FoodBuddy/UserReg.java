package com.example.HCITeam18.FoodBuddy;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserReg extends AppCompatActivity {

    EditText nameEt;
    Button nameBtn;

    String address = "http://weijietest.000webhostapp.com/hci/hci.php";
    InputStream is = null;
    String line = null;
    String result = null;
    String[] name = new String[0];
    String nameString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        setContentView(R.layout.activity_user_reg);

        nameEt = (EditText)findViewById(R.id.nameET);
        nameBtn = (Button)findViewById(R.id.enterBTN);

        getData();



        nameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = null;
                
               nameString  = nameEt.getText().toString();
                boolean enter = false;
                for(int i = 0; i<name.length; i++) {
                    if(nameString.equals(name[i])) {
                        enter = true;
                    }
                }
                if(enter) {
                    myIntent = new Intent(getApplicationContext(), MainActivity.class);
                        myIntent.putExtra("name", nameString);
                        startActivity(myIntent);
                }
                else {
                    myIntent = new Intent(getApplicationContext(), EnterBmi.class);
                    myIntent.putExtra("name", nameString);
                    startActivity(myIntent);
                }
            }
        });

    }
    private void getData()
    {
        try {
            URL url = new URL(address);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.setDoInput(true);

            is = new BufferedInputStream(con.getInputStream());



        }
        catch (Exception e) {
            e.printStackTrace();
        }


        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            while((line=br.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result=sb.toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        //Parse JSON DATA
        try
        {
            JSONArray ja = new JSONArray(result);
            JSONObject jo = null;

            name = new String[ja.length()];


            for(int i = 0; i<ja.length(); i++) {
                jo = ja.getJSONObject(i);
                name[i] = jo.getString("name");

            }



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
