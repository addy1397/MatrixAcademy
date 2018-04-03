package com.addy1397.matrixacademy;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
public class UploadFragment extends Fragment {

    private DatabaseReference databaseReference;
    private FirebaseUser mAuth;

    public UploadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_upload, container, false);

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


        return view;
    }

}
