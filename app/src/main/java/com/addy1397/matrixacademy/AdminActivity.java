package com.addy1397.matrixacademy;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {


    private Button button_Feed;
    private Button button_sms;
    private Toolbar mtoolbar;

    public static String sname, sphone, smessage;

    private DatabaseReference databaseReference;
    private FirebaseUser mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar_admin);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setIcon(R.drawable.ic_dashboard_black_24dp);
        getSupportActionBar().setTitle(R.string.app_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        button_Feed = (Button) findViewById(R.id.button_add_new_feed);
        button_Feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AddFeedActivity.class);
                startActivity(intent);
            }
        });

        button_sms = (Button) findViewById(R.id.button_message);

        final ArrayList<String> phone = new ArrayList<>();

        final ArrayAdapter<String> phoneAdapter = new ArrayAdapter<String>(
                AdminActivity.this,
                android.R.layout.simple_list_item_1,
                phone
        );

        final ArrayList<String> name = new ArrayList<>();

        final ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(
                AdminActivity.this,
                android.R.layout.simple_list_item_1,
                name
        );

        final ArrayList<String> message = new ArrayList<>();

        final ArrayAdapter<String> messageAdapter = new ArrayAdapter<String>(
                AdminActivity.this,
                android.R.layout.simple_list_item_1,
                message
        );

        button_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance().getCurrentUser();
                String Uid = mAuth.getUid();

                databaseReference = FirebaseDatabase.getInstance().getReference().child("database").child("Message");

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            //Toast.makeText(AdminActivity.this, singleSnapshot.toString(), Toast.LENGTH_LONG).show();
                            phoneAdapter.add(singleSnapshot.child("Phone").getValue().toString());
                            nameAdapter.add(singleSnapshot.child("Name").getValue().toString());
                            messageAdapter.add(singleSnapshot.child("Message").getValue().toString());
                        }
                        dataSnapshot.getRef().removeValue();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                for (int i = 0; i < phoneAdapter.getCount(); ++i) {
                    sphone = phoneAdapter.getItem(i).toString();
                    sname = nameAdapter.getItem(i).toString();
                    smessage = sname + " " + messageAdapter.getItem(i).toString();
                            //sname + " was marked absent today at the class at MATRIX ACADEMY";
                    //Toast.makeText(AdminActivity.this, smessage+sphone, Toast.LENGTH_LONG).show();
                    //Intent intent = new Intent(AdminActivity.this, SendMessageActivity.class);
                    //intent.putExtra("Pnone",sphone);
                    //startActivity(intent);

                    //Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    //PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);

                    //SmsManager sms = new SmsManager().getDefault();
                    sendMessage(sphone ,smessage);
                    //sms.sendTextMessage(sphone,null,sname+" was marked absent today at the class at MATRIX ACADEMY",null,null);
                }
            }
        });

    }

    private void sendMessage(String no,String message) {

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(no,null,message,null,null);
        //Toast.makeText(this, "Sent!!", Toast.LENGTH_SHORT).show();

    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    
    
    protected void sendSMSMessage() {
        //phoneNo = txtphoneNo.getText().toString();
        //message = txtMessage.getText().toString();

        Toast.makeText(this, "Permission", Toast.LENGTH_SHORT).show();

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.SEND_SMS},
                        1);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    //smsManager.sendTextMessage(null,null,null,null,null);

                    Toast.makeText(this, "Reached!!!", Toast.LENGTH_SHORT).show();

                    smsManager.sendTextMessage(sphone, "MATRIX ACADEMY", smessage, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }
}
