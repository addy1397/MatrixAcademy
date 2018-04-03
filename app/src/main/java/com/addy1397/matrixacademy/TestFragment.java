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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends Fragment {

    private FirebaseUser mAuth;

    private DatabaseReference firebaseDatabase;

    private ListView listView;

    public TestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_test, container, false);

        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        String Uid = mAuth.getUid();

        listView = (ListView) view.findViewById(R.id.listView_batch_test);

        ArrayList<String> batch = new ArrayList<>();
        ArrayList<String> batchID = new ArrayList<>();

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


        listView.setAdapter(arrayAdapter);

        firebaseDatabase  = FirebaseDatabase.getInstance().getReference().child("database").child("Student").child(Uid).child("Batch");

        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    //Toast.makeText(getActivity(), singleSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                    arrayAdapterID.add(singleSnapshot.child("Name").getValue().toString());
                    arrayAdapter.add(singleSnapshot.getKey().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Unable to Retrive Batch", Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StudentTestFragment fragment = new StudentTestFragment();
                Bundle arguments = new Bundle();
                arguments.putString( "batch" , arrayAdapter.getItem(position).toString());
                fragment.setArguments(arguments);
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content, fragment , "ShowTestFragment");
                ft.commit();
            }
        });

        return view;
    }

}
