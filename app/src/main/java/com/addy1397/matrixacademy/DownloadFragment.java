package com.addy1397.matrixacademy;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadFragment extends Fragment {

    private Button button_batch,button_type;
    String stype_,sbatch_;

    public DownloadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_download, container, false);

        button_batch = (Button) view.findViewById(R.id.button_chooseBatch_download);
        button_batch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBatch();
            }
        });
        button_type = (Button) view.findViewById(R.id.button_chooseType_download);
        button_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getType();
            }
        });

        Button button_OK = (Button) view.findViewById(R.id.button_OK_download);
        button_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadListFragment fragment = new DownloadListFragment();
                Bundle arguments = new Bundle();
                arguments.putString( "Batch" , sbatch_);
                arguments.putString( "Type" , stype_);
                fragment.setArguments(arguments);
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content, fragment , "Download List");
                ft.commit();
            }
        });

        return view;
    }

    public void getBatch(){

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        final String Uid = mUser.getUid();

        final ArrayList<String> batch = new ArrayList<>();
        final ArrayList<String> any = new ArrayList<>();

        //ListView listView = (ListView) view.findViewById(R.id.listView_showBatch);

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


        DatabaseReference firebaseDatabase  = FirebaseDatabase.getInstance().getReference().child("database").child("Student").child(Uid).child("Batch");

        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    //Toast.makeText(getActivity(), singleSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                    arrayAdapter.add(singleSnapshot.child("Name").getValue().toString());
                    batchadapter.add(singleSnapshot.getKey().toString());
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


        AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext());
        //alt_bld.setIcon(R.drawable.icon);
        alt_bld.setTitle("Choose Batch");
        alt_bld.setSingleChoiceItems( arrayAdapter, -1, new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //Toast.makeText(getContext(),
                //        "Pressed = "+ any.get(item), Toast.LENGTH_SHORT).show();
                dialog.dismiss();// dismiss the alertbox after chose option

                button_batch.setText(arrayAdapter.getItem(item));
                sbatch_ = batchadapter.getItem(item);
                Toast.makeText(getActivity(), sbatch_, Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();

    }

    public void getType(){
        final String type[]={"Assignment","Solution","Question Paper","Miscellaneous"};

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext());
        //alt_bld.setIcon(R.drawable.icon);
        alt_bld.setTitle("Choose Batch");
        alt_bld.setSingleChoiceItems( type, -1, new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getContext(),
                        "Pressed = "+ type[item], Toast.LENGTH_SHORT).show();
                dialog.dismiss();// dismiss the alertbox after chose option

                stype_ = type[item];
                button_type.setText(type[item]);
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }
}
