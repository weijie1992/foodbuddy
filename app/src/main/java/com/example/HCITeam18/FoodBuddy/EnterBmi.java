package com.example.HCITeam18.FoodBuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class EnterBmi extends AppCompatActivity {


    EditText heightET, weightET, eweightET;
    Button enterBTN;
    RadioButton maleRadio, femaleRadio;
    String insert_url = "http://weijietest.000webhostapp.com/hci/insertUser.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_bmi);
        Toast.makeText(this, "First time user, please register Thank You", Toast.LENGTH_LONG).show();
        Intent intent = getIntent();
        final String name = intent.getStringExtra("name"); //if it's a string you stored.
        heightET = (EditText) findViewById(R.id.heightET);
        weightET = (EditText) findViewById(R.id.weightET);
       eweightET = (EditText) findViewById(R.id.eweightET);
        enterBTN = (Button) findViewById(R.id.enterBTN);
        maleRadio = (RadioButton)findViewById(R.id.maleRadio);
        femaleRadio = (RadioButton)findViewById(R.id.femaleRadio);

        enterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float heightFloat, weightFloat, bmiFloat,eweightFloat,ebmiFloat;

                int radio = 0;
                if(maleRadio.isChecked()) {
                    radio = 1;
                }
                if(femaleRadio.isChecked())
                {
                    radio = 2;
                }
                String type = "insertUser";
                heightFloat = Float.valueOf(heightET.getText().toString());
                weightFloat = Float.valueOf(weightET.getText().toString());
                eweightFloat = Float.valueOf(eweightET.getText().toString());
                double eprotein,efat,ecarbs,efiber,esodium;


                bmiFloat = weightFloat/(heightFloat * heightFloat);
                ebmiFloat = eweightFloat/(heightFloat * heightFloat);

                esodium = 2300/3;
                efiber = 38/3;

                if(ebmiFloat<18.5) {
                    eprotein = 107.45/3;
                    efat = 71.6/3;
                    ecarbs = 268.625/3;

                }
                else if((ebmiFloat>=18.5) && (ebmiFloat < 25) ){
                    eprotein = 120/3;
                    efat = 80/3;
                    ecarbs = 300/3;
                }
                else if((ebmiFloat>=25) && (ebmiFloat < 30) ){
                    eprotein = 135.85/3;
                    efat = 87/3;
                    ecarbs = 327/3;
                }
                else
                {
                    eprotein = 141.5/3;
                    efat = 94/3;
                    ecarbs = 353.75/3;
                }

                String stringbmi = String.valueOf(bmiFloat);

                insert(name,heightFloat,weightFloat,bmiFloat,ebmiFloat,radio,eprotein,efat,ecarbs,
                        esodium,efiber);


                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                myIntent.putExtra("key", "enterfood");
                myIntent.putExtra("name",name);
                startActivity(myIntent);
            }
        });

    }

    public void insert(String name, float heightFloat, float weightFloat, float bmiFloat, float ebmiFloat, int radio, double eprotein, double efat, double ecarbs, double esodium, double efiber) {
        try {
            String heightString = String.valueOf(heightFloat);
            String weightString = String.valueOf(weightFloat);
            String bmiString = String.valueOf(bmiFloat);
            String ebmiString = String.valueOf(ebmiFloat);
            String radioString = String.valueOf(radio);
            String eproteinString = String.valueOf(eprotein);
            String efatString = String.valueOf(efat);
            String ecarbsString = String.valueOf(ecarbs);
            String esodiumString = String.valueOf(esodium);
            String efiberString = String.valueOf(efiber);


            URL url = new URL(insert_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8")
                    + "&" + URLEncoder.encode("height", "UTF-8") + "=" + URLEncoder.encode(heightString, "UTF-8")
                    + "&" + URLEncoder.encode("weight", "UTF-8") + "=" + URLEncoder.encode(weightString, "UTF-8")
                    + "&" + URLEncoder.encode("bmi", "UTF-8") + "=" + URLEncoder.encode(bmiString, "UTF-8")
                    + "&" + URLEncoder.encode("ebmi", "UTF-8") + "=" + URLEncoder.encode(ebmiString, "UTF-8")
                    + "&" + URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(radioString, "UTF-8")
                    + "&" + URLEncoder.encode("eprotein", "UTF-8") + "=" + URLEncoder.encode(eproteinString, "UTF-8")
                    + "&" + URLEncoder.encode("efat", "UTF-8") + "=" + URLEncoder.encode(efatString, "UTF-8")
                    + "&" + URLEncoder.encode("ecarbs", "UTF-8") + "=" + URLEncoder.encode(ecarbsString, "UTF-8")
                    + "&" + URLEncoder.encode("esodium", "UTF-8") + "=" + URLEncoder.encode(esodiumString, "UTF-8")
                    + "&" + URLEncoder.encode("efiber", "UTF-8") + "=" + URLEncoder.encode(efiberString, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = "";
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
