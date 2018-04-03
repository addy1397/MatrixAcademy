package com.addy1397.matrixacademy;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.min;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendNotificationFragment extends Fragment {


    private ListView listView_batches;
    private EditText editText_title;
    private EditText editText_description;
    private TextView textView_batch;

    private ImageButton imageButton;

    private Button button_publish;

    private DatabaseReference databaseReference;
    private DatabaseReference notificationDatabase;

    private FirebaseUser mAuth;

    private String sID;

    String ToString(int x){

        int c=0,p=x;

        while( x>0 ){
            x/=10;
            c++;
        }
        if( c==1 )
            return "0"+p;
        return ""+p;

    }


    public SendNotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_send_notification, container, false);

        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        String Uid = mAuth.getUid();


        imageButton = (ImageButton) view.findViewById(R.id.imageButton_admin);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());


                final EditText edittext = new EditText(getContext());
                alert.setMessage("Enter Password");
                alert.setTitle("Admin");

                alert.setView(edittext);

                alert.setPositiveButton("LOG-IN", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //What ever you want to do with the value
                        String password = edittext.getText().toString();

                        if(password.matches("admin123")){
                            Intent intent = new Intent(getActivity(), AdminActivity.class);
                            startActivity(intent);
                        }
                    }
                });

                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });

                alert.show();
            }
        });


        editText_title = (EditText) view.findViewById(R.id.editText_NotificationTitle);
        editText_description = (EditText) view.findViewById(R.id.editText_NotificationDescription);


        ArrayList<String> batch = new ArrayList<>();
        final ArrayList<String> batchID = new ArrayList<>();

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                batch
        );

        final ArrayAdapter<String> arrayAdapterID = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                batchID
        );


        notificationDatabase = FirebaseDatabase.getInstance().getReference().child("database").child("Student");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("database").child("Teacher").child(Uid).child("Batches");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    arrayAdapter.add(singleSnapshot.child("Name").getValue().toString());
                    arrayAdapterID.add(singleSnapshot.child("ID").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Failed to load Batches", Toast.LENGTH_SHORT).show();
            }
        });

        for(int i=0;i<arrayAdapter.getCount();i++){
            batch.add(arrayAdapter.getItem(i));
        }
        for(int i=0;i<arrayAdapterID.getCount();i++){
            batchID.add(arrayAdapterID.getItem(i));
        }

        final Button button = view.findViewById(R.id.button_choose_batch_notif);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getContext(), textView_Name.getText().toString(), Toast.LENGTH_SHORT).show();

                AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext());
                //alt_bld.setIcon(R.drawable.icon);
                alt_bld.setTitle("Choose Batch");
                alt_bld.setSingleChoiceItems( arrayAdapter, -1, new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Toast.makeText(getContext(),
                                "Pressed = "+ batchID.get(item), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();// dismiss the alertbox after chose option
                        button.setText(arrayAdapter.getItem(item));

                    }
                });
                AlertDialog alert = alt_bld.create();
                alert.show();
            }
        });

        Calendar calendar = Calendar.getInstance();
        final int date = calendar.get(Calendar.DATE);
        final int month = calendar.get(Calendar.MONTH)+1;
        final int year = calendar.get(Calendar.YEAR);
        final int hour = calendar.get(Calendar.HOUR);
        final int minute = calendar.get(Calendar.MINUTE);

        final String Current_date  = ToString(hour) + ":" + ToString(minute) + " " + ToString(date) + "-" + ToString(month) + "-" + ToString(year);

        final String Cur_Date = "" + (3000 - year) + ToString(12 - month) + ToString(31- date) + ToString(24 - hour) + ToString(60 - minute);

        button_publish = (Button) view.findViewById(R.id.button_publish_Notification);

        button_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference().child("database").child("Batch_Student").child(sID);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            String stud_id = singleSnapshot.child("ID").getValue().toString();

                            Toast.makeText(getActivity(), stud_id, Toast.LENGTH_SHORT).show();
                            HashMap<String,String> hashMap = new HashMap<>();

                            hashMap.put("Date",Current_date);
                            hashMap.put("Title",editText_title.getText().toString());
                            hashMap.put("Description",editText_description.getText().toString());

                            notificationDatabase.child(stud_id).child("Notification").child(Cur_Date).setValue(hashMap);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        return view;
    }

}
