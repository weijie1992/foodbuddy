package com.example.HCITeam18.FoodBuddy;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.io.InputStream;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class PassRecordFrag extends Fragment {

    InputStream is = null;
    String line = null;
    String result = null;

    EditText startDate;
    DatePickerDialog datePickerDialog;
    String startDateChoose = null;

    Button download;



    String[] date = new String[0];
    String[] username = new String[0];

    public PassRecordFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pass_record, container, false);

        Intent intent = getActivity().getIntent();
        final String name = intent.getStringExtra("name");

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));


        startDate = (EditText)view.findViewById(R.id.startdate);

        startDate.setInputType(InputType.TYPE_NULL);
        // perform click event on edit text
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                startDateChoose = year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth;
//

                                startDate.setText(startDateChoose);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        download = (Button)view.findViewById(R.id.downloadBTN);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getContext(), MainActivity.class);
                myIntent.putExtra("key", "passrecord"); //Optional parameters
                myIntent.putExtra("name", name);
                myIntent.putExtra("date", startDateChoose);
                getActivity().finish();
                startActivity(myIntent);

            }
        });
     return view;
    }


}


