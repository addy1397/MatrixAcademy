package com.addy1397.matrixacademy;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.xml.sax.DTDHandler;


/**
 * A simple {@link Fragment} subclass.
 */
public class TestShowFragment extends Fragment {


    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;


    private FirebaseUser mAuth;

    public TestShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test_show, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_testShow);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));





        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        String Uid = mAuth.getUid();

        Bundle arguments = getArguments();
        String batchID = arguments.getString("batch");

        Toast.makeText(getActivity(), batchID, Toast.LENGTH_SHORT).show();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("database").child("Student").child(Uid).child("Batch").child(batchID).child("Test");

        FirebaseRecyclerAdapter<Test_Details,testViewHolder> recyclerAdapter=new FirebaseRecyclerAdapter<Test_Details,testViewHolder>(
                Test_Details.class,
                R.layout.test_details,
                testViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(testViewHolder viewHolder, Test_Details model, int position) {
                viewHolder.setDate(model.getDate());
                viewHolder.setMarks(model.getMarks(),model.getTotal_Number());
                //viewHolder.setPosition(model.getPosition());
                viewHolder.setTotal(model.getTotal());
                viewHolder.setaverage(model.getAverage());
            }
        };

        recyclerView.setAdapter(recyclerAdapter);

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
            textView_marks.setText("Marks Obtainted : " + marks + " Out of " +total);
        }

        public void setPosition(String postition) {
            textView_position.setText("Position : " + postition);
        }

        public void setTotal(String total) {
            textView_total.setText("Total Attempters : " + total);
        }

    }
}
