package com.example.HCITeam18.FoodBuddy;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.example.HCITeam18.FoodBuddy.R.raw.baddiet;
import static com.example.HCITeam18.FoodBuddy.R.raw.carbs;
import static com.example.HCITeam18.FoodBuddy.R.raw.fats;
import static com.example.HCITeam18.FoodBuddy.R.raw.fiber;
import static com.example.HCITeam18.FoodBuddy.R.raw.gooddiet;
import static com.example.HCITeam18.FoodBuddy.R.raw.protein;
import static com.example.HCITeam18.FoodBuddy.R.raw.sodium;

/**
 * A simple {@link Fragment} subclass.
 */
public class RadarFrag extends Fragment{


    ArrayList<Entry> entries;
    private ProgressDialog pd;

    InputStream is = null;
    String line = null;
    String result = null;


    String recommend, avoid = null;
    TextView avoidTV;
    TextView recommendTV;

    String[] foodarray = new String[0];
    String[] fatarray = new String[0];
    String[] proteinarray = new String[0];
    String[] carbsarray = new String[0];
    String[] sodiumarray = new String[0];
    String[] fiberarray = new String[0];


    public RadarFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        Intent intent = getActivity().getIntent();
        final String name = intent.getStringExtra("name"); //if it's a string you stored.
        String datetime = intent.getStringExtra("date");
        String key = intent.getStringExtra("key");
        View view = inflater.inflate(R.layout.fragment_radar2, container, false);
        pd = new ProgressDialog(getContext());//maybe
        pd.setMessage("loading");

         recommendTV = (TextView)view.findViewById(R.id.TVRecommend);
         avoidTV = (TextView)view.findViewById(R.id.TVAvoid);

         entries = new ArrayList<>();

        if(key == null || key.equals("") ) {
            Toast.makeText(getActivity(), "Welcome " + name + "!", Toast.LENGTH_LONG).show();
        }
        else if(key == "finish" || key.equals("finish") ) {
            Toast.makeText(getActivity(), name + " take a look at the recommendation and avoid food for your next meal", Toast.LENGTH_LONG).show();
        }


        if(key == null || key.equals("") || key== "finish" || key.equals("finish")) {
            load_data_from_server(view, name);
            recommendTV.setText(recommend);
            avoidTV.setText(avoid);
        }
        else if(key.equals("passrecord") || key == "passrecord"){
            load_data_passrecords(view, name,datetime);
            recommendTV.setText(recommend);
            avoidTV.setText(avoid);
        }
        return view;
    }



    private void load_data_passrecords(View view, String name, String datetime) {

        pd.show();
        final MediaPlayer gooddiet1 = MediaPlayer.create(getActivity(), gooddiet);
        final MediaPlayer baddiet1 = MediaPlayer.create(getActivity(), baddiet);
        String naming = name;
        String url1 = "http://weijietest.000webhostapp.com/hci/dateAddress.php?name="+ name +"&datetime="+datetime;


        try {
            URL url = new URL(url1);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.setDoInput(true);

            is = new BufferedInputStream(con.getInputStream());


        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            JSONArray jsonarray = new JSONArray(result);

            foodarray = new String[jsonarray.length()];
            fatarray = new String[jsonarray.length()];
            proteinarray = new String[jsonarray.length()];
            carbsarray = new String[jsonarray.length()];
            fiberarray = new String[jsonarray.length()];
            sodiumarray = new String[jsonarray.length()];
            String efat = null;
            String eprotein = null;
            String ecarbs = null;
            String efiber = null;
            String esodium = null;
            JSONObject jsonobject;

            jsonobject = jsonarray.getJSONObject(0);
            efat = jsonobject.getString("EFats").trim();
            eprotein = jsonobject.getString("EProtein").trim();
            ecarbs = jsonobject.getString("ECarbs").trim();
            efiber = jsonobject.getString("EFiber").trim();
            esodium = jsonobject.getString("ESodium").trim();

            for (int i = 0; i < jsonarray.length(); i++) {

                 jsonobject = jsonarray.getJSONObject(i);

                foodarray[i] = jsonobject.getString("Food").trim();
                fatarray[i] = jsonobject.getString("Fats").trim();
                proteinarray[i] = jsonobject.getString("Protein").trim();
                carbsarray[i] = jsonobject.getString("Carbs").trim();
                fiberarray[i] = jsonobject.getString("Fiber").trim();
                sodiumarray[i] = jsonobject.getString("Sodium").trim();



            }
            Float totalfloatfat = 0.0f;
            Float totalfloatprotein = 0.0f;
            Float totalfloatcarbs =0.0f;
            Float totalfloatfiber =0.0f;
            Float totalfloatsodium =0.0f;


            for(int i =0; i<foodarray.length; i++) {
                totalfloatfat += Float.valueOf(fatarray[i]);
                totalfloatprotein += Float.valueOf(proteinarray[i]);
                totalfloatcarbs += Float.valueOf(carbsarray[i]);
                totalfloatfiber += Float.valueOf(fiberarray[i]);
                totalfloatsodium += Float.valueOf(sodiumarray[i]);

            }

             totalfloatfat = (totalfloatfat / (Float.valueOf(efat) * fatarray.length)) * 100;
             totalfloatprotein = (totalfloatprotein / (Float.valueOf(eprotein) * fatarray.length)) * 100;
             totalfloatcarbs = (totalfloatcarbs / (Float.valueOf(ecarbs) * fatarray.length)) * 100;
             totalfloatfiber = (totalfloatfiber / (Float.valueOf(efiber) * fatarray.length)) *100;
             totalfloatsodium = (totalfloatsodium / (Float.valueOf(esodium) * fatarray.length)) *100;


            entries.add(new Entry(totalfloatfat, 0));
            entries.add(new Entry(totalfloatprotein, 1));
            entries.add(new Entry(totalfloatcarbs, 2));
            entries.add(new Entry(totalfloatfiber, 3));
            entries.add(new Entry(totalfloatsodium, 4));

            Float lowesttemp = totalfloatfat;
            String lowesttempString = "fat";

            Float higesttemp = totalfloatfat;
            String highestempString = "fat";

            if(higesttemp < totalfloatprotein) {
                higesttemp = totalfloatprotein;
                highestempString = "protein";
            }
            if(higesttemp < totalfloatcarbs) {
                higesttemp = totalfloatcarbs;
                highestempString = "carbs";
            }

            if(higesttemp < totalfloatfiber) {
                higesttemp = totalfloatfiber;
                highestempString = "fiber";
            }

            if(higesttemp <totalfloatsodium) {
                higesttemp = totalfloatsodium;
                highestempString = "sodium";
            }




            if(lowesttemp > totalfloatprotein) {
                lowesttemp = totalfloatprotein;
                lowesttempString = "protein";
            }
            if(lowesttemp > totalfloatcarbs) {
                lowesttemp = totalfloatcarbs;
                lowesttempString = "carbs";
            }
            if(lowesttemp > totalfloatfiber) {
                lowesttemp = totalfloatfiber;
                lowesttempString = "fiber";
            }
            if(lowesttemp > totalfloatsodium) {
                lowesttemp = totalfloatsodium;
                lowesttempString = "sodium";
            }

            if(higesttemp > 100.0) {
                baddiet1.start();
            }

            if(higesttemp < 100.0) {
                gooddiet1.start();
            }

            if((lowesttempString.equals("sodium") && highestempString.equals("protein"))  ||
                    (lowesttempString.equals("protein") && highestempString.equals("sodium")) ) {
                if(lowesttempString.equals("sodium")) {
                    recommend = "Chicken Satay, Chicken Rice, Black Carrot Cake";
                    avoid = "Spaghetti and meatball, Fishball Noodles, PorkChop";
                }
                else
                {
                    avoid = "Chicken Satay, Chicken Rice, Black Carrot Cake";
                    recommend = "Spaghetti and meatball, Fishball Noodles, PorkChop";
                }
            }

            if((lowesttempString.equals("sodium") && highestempString.equals("carbs"))  ||
                    (lowesttempString.equals("carbs") && highestempString.equals("sodium")) ) {
                if(lowesttempString.equals("sodium")) {
                    recommend = "Chicken Satay, Chicken Rice, Black Carrot Cake";
                    avoid = "Roti Prata, Nasi Lemak, Charsiew Rice";
                } else {
                    avoid = "Chicken Satay, Chicken Rice, Black Carrot Cake";
                    recommend = "Roti Prata, Nasi Lemak, Charsiew Rice";
                }
            }

            if((lowesttempString.equals("sodium") && highestempString.equals("fiber"))  ||
                    (lowesttempString.equals("fiber")&& highestempString.equals("sodium")) ) {
                if(lowesttempString.equals("sodium")) {
                    recommend = "Chicken Satay, Chicken Rice, Black Carrot Cake";
                    avoid = "Slice Fish Beehoon Soup, Wonton Mee Soup, Fish Ball Noodle Dry";
                } else {
                    avoid = "Chicken Satay, Chicken Rice, Black Carrot Cake";
                    recommend = "Slice Fish Beehoon Soup, Wonton Mee Soup, Fish Ball Noodle Dry";
                }

            }

            if((lowesttempString.equals("sodium") && highestempString.equals("fat"))  ||
                    (lowesttempString.equals("fat") && highestempString.equals("sodium")) ) {
                if (lowesttempString.equals("sodium")) {
                    recommend = "Chicken Satay, Chicken Rice, Black Carrot Cake";
                    avoid = "Pork Chop, Nasi Lemak, Minced Meat noodle dry";
                } else {
                    avoid = "Chicken Satay, Chicken Rice, Black Carrot Cake";
                    recommend = "Pork Chop, Nasi Lemak, Minced Meat noodle dry";
                }
            }

            if((lowesttempString.equals("fat") && highestempString.equals("fiber"))  ||
                    (lowesttempString.equals("fiber") && highestempString.equals("fat")) ) {
                if (lowesttempString.equals("fat")) {
                    recommend = "Pork Chop, Nasi Lemak, Carrot Cake Black";
                    avoid = "Sliced Fish Beehoon, Wonton Mee, FishBall Noodle Dry";
                }
                else {
                    avoid = "Pork Chop, Nasi Lemak, Carrot Cake Black";
                    recommend = "Sliced Fish Beehoon, Wonton Mee, FishBall Noodle Dry";
                }
            }
            if((lowesttempString.equals("fat") && highestempString.equals("carbs"))  ||
                    (lowesttempString.equals("carbs") && highestempString.equals("fat")) ) {
                if (lowesttempString.equals("fat")) {
                    recommend = "Pork Chop, Chicken Rice, Carrot Cake Black";
                    avoid = "Roti Prata, NasiLemak, Charsiew Rice";
                }
                else {
                    avoid = "Pork Chop, Nasi Lemak, Carrot Cake Black";
                    recommend = "Sliced Fish Beehoon, Wonton Mee, FishBall Noodle Dry";
                }
            }

            if((lowesttempString.equals("fat") && highestempString.equals("protein"))  ||
                    (lowesttempString.equals("protein") && highestempString.equals("fat")) ) {
                if (lowesttempString.equals("fat")) {
                    recommend = "Nasi Lemak, Chicken Rice, Carrot Cake Black";
                    avoid = "Chicken Satay, Spaghetti and meatball, Pork Chop";
                }
                else {
                    avoid = "Nasi Lemak, Chicken Rice, Carrot Cake Black";
                    recommend ="Chicken Satay, Spaghetti and meatball, Pork Chop";
                }
            }

            if((lowesttempString.equals("protein") && highestempString.equals("carbs"))  ||
                    (lowesttempString.equals("carbs") && highestempString.equals("protein")) ) {
                if (lowesttempString.equals("protein")) {
                    recommend = "Chicken Satay, Spaghetti and meatball, Pork Chop";
                    avoid = "Nasi Lemak, Roti Prata, Char siew Rice";

                } else {
                    avoid = "Chicken Satay, Spaghetti and meatball, Pork Chop";
                    recommend = "Nasi Lemak, Roti Prata, Char siew Rice";
                }
            }
            if((lowesttempString.equals("protein") && highestempString.equals("fiber"))  ||
                    (lowesttempString.equals("fiber") && highestempString.equals("protein")) ) {
                if (lowesttempString.equals("protein")) {
                    recommend = "Chicken Satay, Spaghetti and meatball, Pork Chop";
                    avoid = "Sliced Fish noodle Soup, Wonton mee soup, FishBall Noodle Dry";

                } else {
                    avoid = "Chicken Satay, Spaghetti and meatball, Pork Chop";
                    recommend = "Sliced Fish noodle Soup, Wonton mee soup, FishBall Noodle Dry";
                }
            }

            if((lowesttempString.equals("carbs") && highestempString.equals("fiber"))  ||
                    (lowesttempString.equals("fiber") && highestempString.equals("carbs")) ) {
                if (lowesttempString.equals("fiber")) {

                    recommend = "Sliced Fish noodle Soup, Wonton mee soup, FishBall Noodle Dry";
                    avoid = "Roti Prata, Nasi Lemak, Charsiew Rice";

                } else {
                    avoid = "Sliced Fish noodle Soup, Wonton mee soup, FishBall Noodle Dry";
                    recommend = "Roti Prata, Nasi Lemak, Charsiew Rice";
                }
            }
        }

        catch (JSONException e) {
            e.printStackTrace();

        }

        RadarChart chart = (RadarChart) view.findViewById(R.id.chartr); //maybe

        ArrayList<RadarDataSet> dataSets = new ArrayList<RadarDataSet>();;



        for(int i = 0; i<foodarray.length; i++) {
            RadarDataSet dataset_comp1  = new RadarDataSet(entries, foodarray[i]);
            dataset_comp1.setValueTextSize(14f);
            dataset_comp1.setValueTextColor(Color.MAGENTA);

            if(i == (foodarray.length-1))
            {
                dataset_comp1.setDrawFilled(true);
            }
            dataSets.add(dataset_comp1);


        }
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Fat");
        labels.add("Protein");
        labels.add("Carbs");
        labels.add("Fiber");
        labels.add("Sodium");

        RadarData data = new RadarData(labels, dataSets);

        chart.setData(data);

        String description = "" ;
        chart.setDescription(description);



        chart.invalidate();
        chart.animate();
        pd.hide();

    }

    public void load_data_from_server(View view, String name) {
        pd.show();
        String naming = name;
        String url1 = "http://weijietest.000webhostapp.com/hci/getUser.php?name="+ naming;
        final MediaPlayer fatmusic = MediaPlayer.create(getActivity(), fats);
        final MediaPlayer proteinmusic = MediaPlayer.create(getActivity(), protein);
        final MediaPlayer carbsmusic = MediaPlayer.create(getActivity(), carbs);
        final MediaPlayer fibermusic = MediaPlayer.create(getActivity(), fiber);
        final MediaPlayer sodiummusic = MediaPlayer.create(getActivity(), sodium);

        try {
            URL url = new URL(url1);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.setDoInput(true);

            is = new BufferedInputStream(con.getInputStream());


        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String food = null;
        try {

            JSONArray jsonarray = new JSONArray(result);

            for (int i = 0; i < jsonarray.length(); i++) {

                JSONObject jsonobject = jsonarray.getJSONObject(i);

                food = jsonobject.getString("Food").trim();
                String fat = jsonobject.getString("Fats").trim();
                String protein = jsonobject.getString("Protein").trim();
                String carbs = jsonobject.getString("Carbs").trim();
                String fiber = jsonobject.getString("Fiber").trim();
                String sodium = jsonobject.getString("Sodium").trim();
                String efat = jsonobject.getString("EFats").trim();
                String eprotein = jsonobject.getString("EProtein").trim();
                String ecarbs = jsonobject.getString("ECarbs").trim();
                String efiber = jsonobject.getString("EFiber").trim();
                String esodium = jsonobject.getString("ESodium").trim();

                Float floatfat = ((Float.valueOf(fat) / Float.valueOf(efat)) * 100);
                Float floatprotein = ((Float.valueOf(protein) / Float.valueOf(eprotein)) * 100);
                Float floatcarbs = ((Float.valueOf(carbs) / Float.valueOf(ecarbs)) * 100);
                Float floatfiber = ((Float.valueOf(fiber) / Float.valueOf(efiber)) * 100);
                Float floatsodium = ((Float.valueOf(sodium) / Float.valueOf(esodium)) * 100);


                entries.add(new Entry(floatfat, 0));
                entries.add(new Entry(floatprotein, 1));
                entries.add(new Entry(floatcarbs, 2));
                entries.add(new Entry(floatfiber, 3));
                entries.add(new Entry(floatsodium, 4));

                Float lowesttemp = floatfat;
                String lowesttempString = "fat";

                Float higesttemp = floatfat;
                String highestempString = "fat";

                if(higesttemp < floatprotein) {
                    higesttemp = floatprotein;
                    highestempString = "protein";
                }
                if(higesttemp < floatcarbs) {
                    higesttemp = floatcarbs;
                    highestempString = "carbs";
                }

                if(higesttemp < floatfiber) {
                    higesttemp = floatfiber;
                    highestempString = "fiber";
                }

                if(higesttemp <floatsodium) {
                    higesttemp = floatsodium;
                    highestempString = "sodium";
                }


                if(lowesttemp > floatprotein) {
                    lowesttemp = floatprotein;
                    lowesttempString = "protein";
                }
                if(lowesttemp > floatcarbs) {
                    lowesttemp = floatcarbs;
                    lowesttempString = "carbs";
                }
                if(lowesttemp > floatfiber) {
                    lowesttemp = floatfiber;
                    lowesttempString = "fiber";
                }
                if(lowesttemp > floatsodium) {
                    lowesttemp = floatsodium;
                    lowesttempString = "sodium";
                }

                if(highestempString.equals("fat")) {
                    fatmusic.start();

                }else if(highestempString.equals("protein")) {
                    proteinmusic.start();

                }else if(highestempString.equals("carbs")) {
                    carbsmusic.start();

                }else if(highestempString.equals("fiber")) {
                    fibermusic.start();

                }else if(highestempString.equals("sodium")) {
                    sodiummusic.start();
                }


                if((lowesttempString.equals("sodium") && highestempString.equals("protein"))  ||
                        (lowesttempString.equals("protein") && highestempString.equals("sodium")) ) {
                    if(lowesttempString.equals("sodium")) {
                        recommend = "Chicken Satay, Chicken Rice, Black Carrot Cake";
                        avoid = "Spaghetti and meatball, Fishball Noodles, PorkChop";
                    }
                    else
                    {
                        avoid = "Chicken Satay, Chicken Rice, Black Carrot Cake";
                        recommend = "Spaghetti and meatball, Fishball Noodles, PorkChop";
                    }
                }

                if((lowesttempString.equals("sodium") && highestempString.equals("carbs"))  ||
                        (lowesttempString.equals("carbs") && highestempString.equals("sodium")) ) {
                    if(lowesttempString.equals("sodium")) {
                        recommend = "Chicken Satay, Chicken Rice, Black Carrot Cake";
                        avoid = "Roti Prata, Nasi Lemak, Charsiew Rice";
                    } else {
                        avoid = "Chicken Satay, Chicken Rice, Black Carrot Cake";
                        recommend = "Roti Prata, Nasi Lemak, Charsiew Rice";
                    }
                }

                if((lowesttempString.equals("sodium") && highestempString.equals("fiber"))  ||
                        (lowesttempString.equals("fiber")&& highestempString.equals("sodium")) ) {
                    if(lowesttempString.equals("sodium")) {
                        recommend = "Chicken Satay, Chicken Rice, Black Carrot Cake";
                        avoid = "Slice Fish Beehoon Soup, Wonton Mee Soup, Fish Ball Noodle Dry";
                    } else {
                        avoid = "Chicken Satay, Chicken Rice, Black Carrot Cake";
                        recommend = "Slice Fish Beehoon Soup, Wonton Mee Soup, Fish Ball Noodle Dry";
                    }

                }

                if((lowesttempString.equals("sodium") && highestempString.equals("fat"))  ||
                        (lowesttempString.equals("fat") && highestempString.equals("sodium")) ) {
                    if (lowesttempString.equals("sodium")) {
                        recommend = "Chicken Satay, Chicken Rice, Black Carrot Cake";
                        avoid = "Pork Chop, Nasi Lemak, Minced Meat noodle dry";
                    } else {
                        avoid = "Chicken Satay, Chicken Rice, Black Carrot Cake";
                        recommend = "Pork Chop, Nasi Lemak, Minced Meat noodle dry";
                    }
                }

                if((lowesttempString.equals("fat") && highestempString.equals("fiber"))  ||
                        (lowesttempString.equals("fiber") && highestempString.equals("fat")) ) {
                    if (lowesttempString.equals("fat")) {
                        recommend = "Pork Chop, Nasi Lemak, Carrot Cake Black";
                        avoid = "Sliced Fish Beehoon, Wonton Mee, FishBall Noodle Dry";
                    }
                    else {
                        avoid = "Pork Chop, Nasi Lemak, Carrot Cake Black";
                        recommend = "Sliced Fish Beehoon, Wonton Mee, FishBall Noodle Dry";
                    }
                }
                if((lowesttempString.equals("fat") && highestempString.equals("carbs"))  ||
                        (lowesttempString.equals("carbs") && highestempString.equals("fat")) ) {
                    if (lowesttempString.equals("fat")) {
                        recommend = "Pork Chop, Chicken Rice, Carrot Cake Black";
                        avoid = "Roti Prata, NasiLemak, Charsiew Rice";
                    }
                    else {
                        avoid = "Pork Chop, Nasi Lemak, Carrot Cake Black";
                        recommend = "Sliced Fish Beehoon, Wonton Mee, FishBall Noodle Dry";
                    }
                }

                if((lowesttempString.equals("fat") && highestempString.equals("protein"))  ||
                        (lowesttempString.equals("protein") && highestempString.equals("fat")) ) {
                    if (lowesttempString.equals("fat")) {
                        recommend = "Nasi Lemak, Chicken Rice, Carrot Cake Black";
                        avoid = "Chicken Satay, Spaghetti and meatball, Pork Chop";
                    }
                    else {
                        avoid = "Nasi Lemak, Chicken Rice, Carrot Cake Black";
                        recommend ="Chicken Satay, Spaghetti and meatball, Pork Chop";
                    }
                }

                if((lowesttempString.equals("protein") && highestempString.equals("carbs"))  ||
                        (lowesttempString.equals("carbs") && highestempString.equals("protein")) ) {
                    if (lowesttempString.equals("protein")) {
                        recommend = "Chicken Satay, Spaghetti and meatball, Pork Chop";
                        avoid = "Nasi Lemak, Roti Prata, Char siew Rice";

                    } else {
                        avoid = "Chicken Satay, Spaghetti and meatball, Pork Chop";
                        recommend = "Nasi Lemak, Roti Prata, Char siew Rice";
                    }
                }
                if((lowesttempString.equals("protein") && highestempString.equals("fiber"))  ||
                        (lowesttempString.equals("fiber") && highestempString.equals("protein")) ) {
                    if (lowesttempString.equals("protein")) {
                        recommend = "Chicken Satay, Spaghetti and meatball, Pork Chop";
                        avoid = "Sliced Fish noodle Soup, Wonton mee soup, FishBall Noodle Dry";

                    } else {
                        avoid = "Chicken Satay, Spaghetti and meatball, Pork Chop";
                        recommend = "Sliced Fish noodle Soup, Wonton mee soup, FishBall Noodle Dry";
                    }
                }

                if((lowesttempString.equals("carbs") && highestempString.equals("fiber"))  ||
                        (lowesttempString.equals("fiber") && highestempString.equals("carbs")) ) {
                    if (lowesttempString.equals("fiber")) {

                        recommend = "Sliced Fish noodle Soup, Wonton mee soup, FishBall Noodle Dry";
                        avoid = "Roti Prata, Nasi Lemak, Charsiew Rice";

                    } else {
                        avoid = "Sliced Fish noodle Soup, Wonton mee soup, FishBall Noodle Dry";
                        recommend = "Roti Prata, Nasi Lemak, Charsiew Rice";
                    }
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();


        }


        RadarChart chart = (RadarChart) view.findViewById(R.id.chartr);

        String abc = food;


        RadarDataSet dataset_comp1 = new RadarDataSet(entries, abc);

        dataset_comp1.setValueTextSize(14f);
        dataset_comp1.setValueTextColor(Color.MAGENTA);


        dataset_comp1.setColor(Color.parseColor("#ffa500"));
        dataset_comp1.setDrawFilled(true);


        ArrayList<RadarDataSet> dataSets = new ArrayList<RadarDataSet>();
        dataSets.add(dataset_comp1);


        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Fat");
        labels.add("Protein");
        labels.add("Carbs");
        labels.add("Fiber");
        labels.add("Sodium");


        RadarData data = new RadarData(labels, dataSets);
        chart.setData(data);
        String description = "";
        chart.setDescription(description);

        chart.invalidate();
        chart.animate();
        pd.hide();


    }

}



