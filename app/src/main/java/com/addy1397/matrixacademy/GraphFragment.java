package com.addy1397.matrixacademy;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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
public class GraphFragment extends Fragment {

    private DatabaseReference databaseReference;
    private FirebaseUser mAuth;
    BarChart barChart_marks,barChart_average;

    public static ArrayAdapter<String> marksArrayAdapter;


    public GraphFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  =  inflater.inflate(R.layout.fragment_graph, container, false);

        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        String Uid = mAuth.getUid();

        Bundle arguments = getArguments();
        String batchID = arguments.getString("batch");

        //Toast.makeText(getActivity(), batchID, Toast.LENGTH_SHORT).show();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("database").child("Student").child(Uid).child("Batch").child(batchID).child("Test");

         ArrayList<String> marks = new ArrayList<String>();
        ArrayList<String> total = new ArrayList<String>();
        ArrayList<String> average = new ArrayList<String>();

        final ArrayAdapter<String> marksArrayAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1,
                marks
        );
        final ArrayAdapter<String> totalArrayAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1,
                total
        );
        final ArrayAdapter<String> averageArrayAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1,
                average
        );


        //Toast.makeText(getActivity(), batchID, Toast.LENGTH_SHORT).show();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("database").child("Student").child(Uid).child("Batch").child(batchID).child("Test");

        FirebaseRecyclerAdapter<Test_Details,testViewHolder> recyclerAdapter=new FirebaseRecyclerAdapter<Test_Details,testViewHolder>(
                Test_Details.class,
                R.layout.test_details,
                testViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(testViewHolder viewHolder, Test_Details model, int position) {
                //viewHolder.setDate(model.getDate());
                viewHolder.setMarks(model.getMarks(),model.getTotal_Number());
                //viewHolder.setPosition(model.getPosition());
                //viewHolder.setTotal(model.getTotal());
                //viewHolder.setaverage(model.getAverage());
            }
        };

        Toast.makeText(view.getContext(), " --- " + String.valueOf(marksArrayAdapter.getCount()), Toast.LENGTH_SHORT).show();

        return view;
    }

    public static class testViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView textView_average;
        TextView textView_date;
        TextView textView_marks;
        TextView textView_position;
        TextView textView_total;
        TextView textView_total_marks;

        public testViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            textView_average = (TextView) itemView.findViewById(R.id.textView_Average);
            textView_date = (TextView) itemView.findViewById(R.id.textView_testDate);
            textView_marks = (TextView) itemView.findViewById(R.id.textView_position);
            textView_position = (TextView) itemView.findViewById(R.id.textView_Marks);
            textView_total = (TextView) itemView.findViewById(R.id.textView_totalAtemptees);

        }

        public void setDate(String date) {
            textView_date.setText("Date Of Test : " + date);
        }

        public void setaverage(String average) {
            textView_average.setText("Average : " + average);
        }

        public void setMarks(String marks,String total) {
            marksArrayAdapter.add(marks);
            //textView_marks.setText("Marks Obtainted : " + marks + " Out of " +total);
        }

        public void setPosition(String postition) {
            textView_position.setText("Position : " + postition);
        }

        public void setTotal(String total) {
            textView_total.setText("Total Attempters : " + total);
        }

    }
}