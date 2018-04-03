package com.addy1397.matrixacademy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class StudentActivity extends AppCompatActivity {

    private BottomNavigationView navigation;

    String ff;

    private ColorDrawable colorDrawable;
    private FragmentTransaction fragmentTransaction;
    private android.support.v7.widget.Toolbar mtoolbar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("ResourceAsColor")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_feed:
                    navigation.setItemBackgroundResource(R.color.colorblue);
                    colorDrawable = new ColorDrawable(Color.parseColor("#4267B2"));
                    getSupportActionBar().setBackgroundDrawable(colorDrawable);
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content,new FeedFragment());
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_test:
                    navigation.setItemBackgroundResource(R.color.colorred);
                    colorDrawable = new ColorDrawable(Color.parseColor("#B71C1C"));
                    getSupportActionBar().setBackgroundDrawable(colorDrawable);
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content,new TestFragment());
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_notifications:
                    navigation.setItemBackgroundResource(R.color.colorgreen);
                    colorDrawable = new ColorDrawable(Color.parseColor("#6ab344"));
                    getSupportActionBar().setBackgroundDrawable(colorDrawable);
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content,new NotificationFragment());
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_downloads:
                    navigation.setItemBackgroundResource(R.color.colorpurple);
                    colorDrawable = new ColorDrawable(Color.parseColor("#800080"));
                    getSupportActionBar().setBackgroundDrawable(colorDrawable);
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content,new DownloadFragment());
                    fragmentTransaction.commit();
                    return true;
            }
            return false;
        }
    };

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        Intent intent = getIntent();
        ff = intent.getStringExtra("type");

        mtoolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setIcon(R.drawable.ic_dashboard_black_24dp);
        getSupportActionBar().setTitle(R.string.app_title);

        colorDrawable = new ColorDrawable(Color.parseColor("#4267B2"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.setItemBackgroundResource(R.color.colorblue);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, new FeedFragment());
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.view_Profile){
            Intent intent = new Intent(StudentActivity.this,profile.class);
            intent.putExtra("type",ff);
            startActivity(intent);
        }

        if(item.getItemId() == R.id.home){
            onBackPressed();
        }
        return true;
    }
}
