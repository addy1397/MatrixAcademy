package com.addy1397.matrixacademy;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
public class NotificationFragment extends Fragment {

    private FirebaseUser mAuth;
    private DatabaseReference databaseReference;

    private RecyclerView recyclerView;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        String Uid  = mAuth.getUid();

        Toast.makeText(getActivity(), Uid, Toast.LENGTH_SHORT).show();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_notif);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("database").child("Student").child(Uid).child("Notification");

        FirebaseRecyclerAdapter<notif,notifViewHolder> recyclerAdapter=new FirebaseRecyclerAdapter<notif,notifViewHolder>(
                notif.class,
                R.layout.notification_cardview,
                notifViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(notifViewHolder viewHolder, notif model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());
                viewHolder.setDate(model.getDate());
            }
        };
        recyclerView.setAdapter(recyclerAdapter);

        return view;
    }
    public static class notifViewHolder extends RecyclerView.ViewHolder  {
        View mView;
        TextView textView_Title;
        TextView textView_Desc;
        TextView textView_Date;

        private static Context context;

        public notifViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            context = itemView.getContext();
            textView_Title = (TextView) itemView.findViewById(R.id.textView_notif_title);
            textView_Desc = (TextView) itemView.findViewById(R.id.textView_notification_desc);
            textView_Date = (TextView) itemView.findViewById(R.id.textView_notification_time);
        }

        public void setTitle(String title)
        {
            textView_Title.setText(title);
        }
        public void setDescription(String attendence)
        {
            textView_Desc.setText(attendence);
        }
        public void setDate(String date)
        {
            textView_Date.setText(date);
        }

    }
}
