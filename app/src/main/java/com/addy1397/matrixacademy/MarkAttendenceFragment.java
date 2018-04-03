package com.addy1397.matrixacademy;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MarkAttendenceFragment extends Fragment {


    private DatabaseReference databaseReference;

    public static HashMap<String, String> attendence_map = new HashMap<String, String>();
    public static HashMap<String, String> ID_name = new HashMap<>();

    private RecyclerView recyclerView;

    private Button button_send;

    public MarkAttendenceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mark_attendence, container, false);

        Bundle arguments = getArguments();
        String batchID = arguments.getString("batch");

        Toast.makeText(getActivity(), batchID, Toast.LENGTH_SHORT).show();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_markattendence);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("database").child("Batch_Student").child(batchID);

        FirebaseRecyclerAdapter<Batch_Name, BlogViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<Batch_Name, BlogViewHolder>(
                Batch_Name.class,
                R.layout.markattendence_layout,
                BlogViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Batch_Name model, int position) {
                viewHolder.setname(model.getName());
                attendence_map.put(model.getID(),"P");
                viewHolder.setid(model.getID());
            }
        };
        recyclerView.setAdapter(recyclerAdapter);

        button_send = (Button) view.findViewById(R.id.button_SEND_attendende);

        final DatabaseReference dbmessage = FirebaseDatabase.getInstance().getReference().child("database").child("Message");

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("database").child("Student");
                for (Map.Entry<String, String> entry : attendence_map.entrySet()) {
                    String key = entry.getKey();
                    final String value = entry.getValue();
                    if(value.matches("P"))
                        continue;
                    //Toast.makeText(getActivity(), key+value, Toast.LENGTH_SHORT).show();
                    databaseReference.child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String Phone_no = dataSnapshot.child("phone").getValue().toString();
                            String message;
                            String name = dataSnapshot.child("name").getValue().toString();

                            if(value.matches("L"))
                                message = "was marked LATE today at the class at MATRIX ACADEMY";
                            else
                                message = "was marked ABSENT today at the class at MATRIX ACADEMY";

                            HashMap<String,String> message_stud = new HashMap<>();

                            message_stud.put("Name",name);
                            message_stud.put("Phone",Phone_no);
                            message_stud.put("Message",message);

                            DatabaseReference ref = dbmessage.push();
                            ref.setValue(message_stud);

                            //Toast.makeText(getActivity(), dataSnapshot.child("email").getValue().toString(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        return view;
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder  {
        View mView;
        TextView textView_Name;
        TextView textView_ID;
        TextView textView_attendence;

        private static Context context;
        private static Activity activity;

        public BlogViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;
            context = itemView.getContext();
            textView_Name = (TextView) itemView.findViewById(R.id.textView_name);
            textView_attendence = (TextView) itemView.findViewById(R.id.textView_attendence);
            textView_ID = (TextView) itemView.findViewById(R.id.textView_ID_attendence);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String[] type2 = {"Present","Late","Absent"};
                    final String[] type = {"P","L","A"};

                    //Toast.makeText(context, textView_Name.getText().toString(), Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
                    //alt_bld.setIcon(R.drawable.icon);
                    alt_bld.setTitle("Attendence");
                    alt_bld.setSingleChoiceItems(type2, -1, new DialogInterface
                            .OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            //Toast.makeText(context,
                            //        "Pressed = "+type2[item], Toast.LENGTH_SHORT).show();
                            String SS = textView_ID.getText().toString();
                            attendence_map.put(textView_ID.getText().toString(),type[item]);
                            Toast.makeText(itemView.getContext(), SS+" "+attendence_map.get(SS), Toast.LENGTH_SHORT).show();
                            setattendence(type[item]);
                            dialog.dismiss();// dismiss the alertbox after chose option

                        }
                    });
                    AlertDialog alert = alt_bld.create();
                    alert.show();
                }
            });
        }


        public void setname(String title)
        {
            textView_Name.setText(title);
        }
        public void setattendence(String attendence)
        {
            textView_attendence.setText(attendence);
        }
        public void setid(String ID)
        {
            textView_ID.setText(ID);
        }

    }
}
