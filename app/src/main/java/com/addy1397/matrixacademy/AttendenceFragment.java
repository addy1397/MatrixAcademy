package com.addy1397.matrixacademy;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AttendenceFragment extends Fragment {


    private DatabaseReference databaseReference;
    private FirebaseUser mAuth;

    public AttendenceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendence, container, false);

        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        final String Uid = mAuth.getUid();

        final ArrayList<String> batch = new ArrayList<>();
        final ArrayList<String> any = new ArrayList<>();

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                batch
        );
        final ArrayAdapter<String> batchadapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                any

        );


        databaseReference = FirebaseDatabase.getInstance().getReference().child("database").child("Teacher").child(Uid).child("Batches");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                    //final String ss = singleSnapshot.getValue().toString();
                    arrayAdapter.add(singleSnapshot.child("Name").getValue().toString());
                    batchadapter.add(singleSnapshot.child("ID").getValue().toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        for(int i=0;i<arrayAdapter.getCount();i++){
            batch.add(arrayAdapter.getItem(i));
        }
        for(int i=0;i<batchadapter.getCount();i++){
            any.add(batchadapter.getItem(i));
        }

        Button button = view.findViewById(R.id.button_choose_batch_attendence);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getContext(), textView_Name.getText().toString(), Toast.LENGTH_SHORT).show();

                AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext());
                //alt_bld.setIcon(R.drawable.icon);
                alt_bld.setTitle("Choose Batch");
                alt_bld.setSingleChoiceItems(arrayAdapter, -1, new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Toast.makeText(getContext(),
                                "Pressed = " + batch.get(item), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();// dismiss the alertbox after chose option

                        MarkAttendenceFragment fragment = new MarkAttendenceFragment();
                        Bundle arguments = new Bundle();
                        arguments.putString("batch", batchadapter.getItem(item).toString());
                        fragment.setArguments(arguments);
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.content, fragment, "MarkAttendenceFragment");
                        ft.commit();
                    }
                });
                AlertDialog alert = alt_bld.create();
                alert.show();

            }
        });
        return view;
    }

}
