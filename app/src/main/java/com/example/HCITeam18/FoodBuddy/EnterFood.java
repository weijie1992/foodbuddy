package com.example.HCITeam18.FoodBuddy;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class EnterFood extends Fragment {

    String foodAddress = "http://weijietest.000webhostapp.com/hci/food.php";
    InputStream is = null;
    String line = null;
    String result = null;
    Spinner spinner;
    ImageView imageView;


    String[] id = new String[0];
    String[] food = new String[0];
    String[] fat = new String[0];
    String[] protein = new String[0];
    String[] carbs = new String[0];
    String[] sodium = new String[0];
    String[] fiber = new String[0];

    String[] userid = new String[0];
    String[] username = new String[0];
    String[] gender = new String[0];
    String[] height = new String[0];
    String[] weight = new String[0];
    String[] bmi = new String[0];
    String[] ebmi = new String[0];
    String[] efat = new String[0];
    String[] eprotein = new String[0];
    String[] ecarbs = new String[0];
    String[] esodium = new String[0];
    String[] efiber = new String[0];

    Integer[] ImageID = {R.drawable.f1,
                        R.drawable.f2,
                        R.drawable.f3,
                        R.drawable.f4,
                        R.drawable.f5,
                        R.drawable.f6,
                        R.drawable.f7,
                        R.drawable.f8,
                        R.drawable.f9,
                        R.drawable.f10,
                        R.drawable.f11,
                        R.drawable.f12,
                        R.drawable.f13,
                        R.drawable.f14,
                        R.drawable.f15,
                        R.drawable.f16,

    };

    public EnterFood() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        View view = inflater.inflate(R.layout.fragment_enter_food, container, false);
        ListView lv = (ListView)view.findViewById(R.id.listview);

        Intent intent = getActivity().getIntent();
        final String name = intent.getStringExtra("name");

        getData();

        lv.setAdapter(new MyListAdaper(getActivity(),R.layout.list_item ,food,fat,protein,carbs,sodium,fiber));

        getUserData(name);


       final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                      @Override
                                      public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                                          alert.setTitle("Confirm Selection").setMessage(food[position]);

                                          alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                              @Override
                                              public void onClick(DialogInterface dialog, int which) {

                                                  String type = "insertFood";
                                                  String foodString = food[position];
                                                  String fatstring = fat[position];
                                                  String proteinString = protein[position];
                                                  String carbsString = carbs[position];
                                                  String sodiumString = sodium[position];
                                                  String fiberString = fiber[position];
                                                  backgroundWorker BackgroundWorker = new backgroundWorker(getContext());
                                                  BackgroundWorker.execute(type, username[0], gender[0], height[0], weight[0], bmi[0], ebmi[0], foodString,
                                                          fatstring, proteinString, carbsString, fiberString, sodiumString,
                                                          efat[0], eprotein[0], ecarbs[0], efiber[0], esodium[0]);


                                                  Intent myIntent = new Intent(getContext(), MainActivity.class);
                                                  myIntent.putExtra("key", "finish"); //Optional parameters
                                                  myIntent.putExtra("name", name);
                                                  getActivity().finish();
                                                  startActivity(myIntent);
                                              }
                                          });
                                          alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                                              @Override
                                              public void onClick(DialogInterface dialog, int which) {
                                                  dialog.dismiss();
                                              }
                                          });
                                          alert.show();

                                      }
                                  });
        return view;
    }

    private void getData() {
        try {
            URL url = new URL(foodAddress);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.setDoInput(true);

            is = new BufferedInputStream(con.getInputStream());



        }
        catch (Exception e) {
            e.printStackTrace()    ;
        }

        //Read content
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

            id = new String[ja.length()];
            food = new String[ja.length()];
            fat = new String[ja.length()];
            protein = new String[ja.length()];
            carbs = new String[ja.length()];
            fiber = new String[ja.length()];
            sodium = new String[ja.length()];


            for(int i = 0; i<ja.length(); i++) {
                jo = ja.getJSONObject(i);
                id[i] = jo.getString("id");
                food[i] = jo.getString("Name");
                fat[i] = jo.getString("Fat");
                protein[i] = jo.getString("Protein");
                carbs[i] = jo.getString("Carbs");
                fiber[i] = jo.getString("Fiber");
                sodium[i] = jo.getString("Sodium");
            }

                    }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getUserData(String name) {
        try {
            String userAdddress = "http://weijietest.000webhostapp.com/hci/getUserChart.php?name="+ name;
            URL url = new URL(userAdddress);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.setDoInput(true);

            is = new BufferedInputStream(con.getInputStream());



        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //Read content
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

             userid = new String[ja.length()];
            username =new String[ja.length()];
             gender =new String[ja.length()];
             height = new String[ja.length()];
            weight = new String[ja.length()];
            bmi = new String[ja.length()];
            ebmi = new String[ja.length()];
             efat =new String[ja.length()];
             eprotein =new String[ja.length()];
            ecarbs = new String[ja.length()];
            esodium = new String[ja.length()];
             efiber = new String[ja.length()];




            for(int i = 0; i<ja.length(); i++) {
                jo = ja.getJSONObject(i);
                userid[i] = jo.getString("id");
                username[i] = jo.getString("name");
                gender[i] = jo.getString("gender");
                height[i] = jo.getString("height");
                weight[i] = jo.getString("weight");
                bmi[i] = jo.getString("bmi");
                ebmi[i] = jo.getString("ebmi");
                efat[i] = jo.getString("EFats");
                eprotein[i] = jo.getString("EProtein");
                ecarbs[i] = jo.getString("ECarbs");
                efiber[i] = jo.getString("EFiber");
                esodium[i] = jo.getString("ESodium");
//
            }
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, food);
            spinner.setAdapter(spinnerAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                    spinner.setSelection(position);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });




        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private class MyListAdaper extends BaseAdapter {
        LayoutInflater mInflater;
        String[] food;
        String[] fat;
        String[] protein;
        String[] carbs;
        String[] sodium;
        String[] fiber;
        private int layout;
        private MyListAdaper(Context c, int resource, String[] food, String[] fat
                , String[] protein, String[] carbs, String[] sodium, String[] fiber) {
            layout = resource;
            this.food = food;
            this.fat = fat;
            this.protein = protein;
            this.carbs = carbs;
            this.sodium = sodium;
            this.fiber = fiber;

            mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(getActivity());
            convertView = inflater.inflate(layout, parent, false);
            TextView foodtv = (TextView) convertView.findViewById(R.id.foodtv);
            TextView fattv = (TextView) convertView.findViewById(R.id.fatTV);
            TextView proteintv = (TextView) convertView.findViewById(R.id.proteinTV);
            TextView carbstv = (TextView) convertView.findViewById(R.id.carbsTV);
            TextView fibertv = (TextView) convertView.findViewById(R.id.fiberTV);
            TextView sodiumtv = (TextView) convertView.findViewById(R.id.sodiumTv);
            imageView= (ImageView)convertView.findViewById(R.id.imageView10);
            String foodString = food[position];
            String carbString = carbs[position];
            String fatString = fat[position];
            String proteinString = protein[position];
            String fiberString = fiber[position];
            String sodiumString = sodium[position];

            foodtv.setText(foodString);
            fattv.setText(fatString +"g");
            proteintv.setText(proteinString + "g");
            carbstv.setText(carbString + "g");
            fibertv.setText(fiberString + "g");
            sodiumtv.setText(sodiumString + "mg");
            imageView.setImageResource(ImageID[position]);

            return convertView;
        }


        @Override
        public int getCount() {
            return food.length;
        }

        @Override
        public Object getItem(int position) {
            return food[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

}
