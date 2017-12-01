package com.example.HCITeam18.FoodBuddy;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mtoogle;
    FragmentTransaction fragmentTransaction;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        setContentView(R.layout.activity_main);

        Intent intent = this.getIntent();
        String goWhere = intent.getStringExtra("key");
        String name = intent.getStringExtra("name");
        String date = intent.getStringExtra("date");

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        mtoogle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);


        mDrawerLayout.addDrawerListener(mtoogle);
        getSupportActionBar().setTitle("EnterFood");
        mtoogle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()) {

                    case R.id.home:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new RadarFrag());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Home");
                        item.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.enter_food:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();

                        fragmentTransaction.replace(R.id.main_container, new EnterFood());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Enter Food");
                        item.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.pass_records:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();

                        fragmentTransaction.replace(R.id.main_container, new PassRecordFrag());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Pass Records");
                        item.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        break;
                }
                return true;
            }

        });

        if(goWhere == null || goWhere== "finish" || goWhere.equals("finish")
                ||goWhere.equals("passrecord") || goWhere == "passrecord") {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.main_container, new RadarFrag());
            fragmentTransaction.commit();

        }

        else if(goWhere == "enterfood" || goWhere.equals("enterfood")) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.main_container, new EnterFood());
            fragmentTransaction.commit();


        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mtoogle.onOptionsItemSelected(item)) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
