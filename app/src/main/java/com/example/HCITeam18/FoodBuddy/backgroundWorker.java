package com.example.HCITeam18.FoodBuddy;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

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

/**
 * Created by admin on 11/17/2017.
 */

public class backgroundWorker extends AsyncTask<String, Void, String> {
    Context context;
    AlertDialog alertDialog;

    backgroundWorker(Context ctx){

        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {

        if (params[0] == "insertFood") {
            String insert_url = "http://weijietest.000webhostapp.com/hci/insertFood.php";

            try {

                String username = params[1];
                String gender = params[2];
                String height = params[3];
                String weight = params[4];
                String bmi = params[5];
                String ebmi = params[6];
                String food = params[7];
                String fat = params[8];
                String protein = params[9];
                String carbs = params[10];
                String fiber = params[11];
                String sodium = params[12];
                String efat = params[13];
                String eprotein = params[14];
                String ecarbs = params[15];
                String efiber = params[16];
                String esodium = params[17];

                URL url = new URL(insert_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("food", "UTF-8") + "=" + URLEncoder.encode(food, "UTF-8")
                        + "&" + URLEncoder.encode("fat", "UTF-8") + "=" + URLEncoder.encode(fat, "UTF-8")
                        + "&" + URLEncoder.encode("protein", "UTF-8") + "=" + URLEncoder.encode(protein, "UTF-8")
                        + "&" + URLEncoder.encode("carbs", "UTF-8") + "=" + URLEncoder.encode(carbs, "UTF-8")
                        + "&" + URLEncoder.encode("fiber", "UTF-8") + "=" + URLEncoder.encode(fiber, "UTF-8")
                        + "&" + URLEncoder.encode("sodium", "UTF-8") + "=" + URLEncoder.encode(sodium, "UTF-8")
                        + "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")
                        + "&" + URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(gender, "UTF-8")
                        + "&" + URLEncoder.encode("height", "UTF-8") + "=" + URLEncoder.encode(height, "UTF-8")
                        + "&" + URLEncoder.encode("weight", "UTF-8") + "=" + URLEncoder.encode(weight, "UTF-8")
                        + "&" + URLEncoder.encode("bmi", "UTF-8") + "=" + URLEncoder.encode(bmi, "UTF-8")
                        + "&" + URLEncoder.encode("ebmi", "UTF-8") + "=" + URLEncoder.encode(ebmi, "UTF-8")
                        + "&" + URLEncoder.encode("efat", "UTF-8") + "=" + URLEncoder.encode(efat, "UTF-8")
                        + "&" + URLEncoder.encode("eprotein", "UTF-8") + "=" + URLEncoder.encode(eprotein, "UTF-8")
                        + "&" + URLEncoder.encode("ecarbs", "UTF-8") + "=" + URLEncoder.encode(ecarbs, "UTF-8")
                        + "&" + URLEncoder.encode("efiber", "UTF-8") + "=" + URLEncoder.encode(efiber, "UTF-8")
                        + "&" + URLEncoder.encode("esodium", "UTF-8") + "=" + URLEncoder.encode(esodium, "UTF-8");
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


                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        return null;
    }
}
