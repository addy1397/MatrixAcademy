package com.addy1397.matrixacademy;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class EnterMarksFragment extends Fragment {

    private DatabaseReference databaseReference;
    private DatabaseReference mdata;

    public static HashMap<String, String> marks_map = new HashMap<String, String>();

    private RecyclerView recyclerView;

    private EditText EditText_marks;

    private Button button_send;

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


    public EnterMarksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_enter_marks, container, false);

        Bundle arguments = getArguments();
        final String batchID = arguments.getString("batch");
        //Toast.makeText(getActivity(), batchID, Toast.LENGTH_SHORT).show();

        EditText_marks = (EditText) view.findViewById(R.id.editText_totalMarks);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_marks);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("database").child("Batch_Student").child(batchID);

        FirebaseRecyclerAdapter<Batch_Name, EnterMarksFragment.BlogViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<Batch_Name, EnterMarksFragment.BlogViewHolder>(
                Batch_Name.class,
                R.layout.markattendence_layout,
                EnterMarksFragment.BlogViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Batch_Name model, int position) {
                viewHolder.setname(model.getName());
                viewHolder.setid(model.getID());
                marks_map.put(model.getID(),"-1");
            }

        };
        recyclerView.setAdapter(recyclerAdapter);

        button_send = (Button) view.findViewById(R.id.button_SEND_marks);

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sTotal = EditText_marks.getText().toString();
                //Toast.makeText(getContext(), "SENDING,...", Toast.LENGTH_SHORT).show();A
                ArrayList<Double> marks = new ArrayList<>();
                double total = 0.0;
                Integer number = 0;
                for (Object value : marks_map.values()) {
                    if (value.toString().matches("-1")) {
                        continue;
                    }
                    else{
                        number ++;
                        marks.add(Double.parseDouble((String) value));
                        total += Double.parseDouble((String) value);
                    }
                }

                Collections.sort(marks);
                Collections.reverse(marks);

                Calendar calendar = Calendar.getInstance();
                final int date = calendar.get(Calendar.DATE);
                final int month = calendar.get(Calendar.MONTH)+1;
                final int year = calendar.get(Calendar.YEAR);
                final int hour = calendar.get(Calendar.HOUR);
                final int minute = calendar.get(Calendar.MINUTE);

                String Current_date  = ((EditText) view.findViewById(R.id.editText_testdate)).getText().toString();

                String Cur_Date = "" + (year) + ToString(month) + ToString(date) + ToString(hour) + ToString(minute);

                mdata = FirebaseDatabase.getInstance().getReference().child("database").child("Student");
                Iterator it = marks_map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    Integer pos = marks.indexOf(Double.parseDouble((String) pair.getValue()));

                    mdata.child(pair.getKey().toString()).child("Batch").child(batchID).child("Test").child(Cur_Date);

                    HashMap<String,String> hashmap = new HashMap<>();

                    hashmap.put("Date",Current_date);
                    hashmap.put("Marks",pair.getValue().toString());
                    hashmap.put("Total_Number",sTotal);
                    hashmap.put("Position",String.valueOf(pos+1));
                    hashmap.put("Total",String.valueOf(number));
                    hashmap.put("Average",String.valueOf(total/number));

                    mdata.child(pair.getKey().toString()).child("Batch").child(batchID).child("Test").child(Cur_Date).setValue(hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                                Toast.makeText(getActivity(), "Uploaded Test", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getActivity(), "Failed....Re-try", Toast.LENGTH_SHORT).show();
                        }
                    });

                    //Toast.makeText(getActivity(), pair.getKey() + " " + pair.getValue(), Toast.LENGTH_SHORT).show();
                    //System.out.println(pair.getKey() + " = " + pair.getValue());
                    it.remove(); // avoids a ConcurrentModificationException
                }

                //Toast.makeText(getActivity(), String.valueOf(total), Toast.LENGTH_SHORT).show();
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

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            context = itemView.getContext();
            textView_Name = (TextView) itemView.findViewById(R.id.textView_name);
            textView_attendence = (TextView) itemView.findViewById(R.id.textView_attendence);
            textView_ID = (TextView) itemView.findViewById(R.id.textView_ID_attendence);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(context, textView_Name.getText().toString(), Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    final EditText edittext = new EditText(context);
                    alert.setMessage("-1 for no marks");
                    alert.setTitle("MARKS");

                    alert.setView(edittext);

                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String ss = edittext.getText().toString();
                            setattendence(ss);
                            //marks_map[textView_ID.getText().toString()] = ss;
                            marks_map.put(textView_ID.getText().toString(),ss);
                        }
                    });

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
