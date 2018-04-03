package com.addy1397.matrixacademy;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadListFragment extends Fragment {

    private FirebaseUser mAuth;
    private DatabaseReference databaseReference;

    public static ArrayAdapter<String> arrayAdapter;

    private RecyclerView recyclerView;

    public DownloadListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_download_list, container, false);


        Bundle arguments = getArguments();
        final String batchID = arguments.getString("Batch");
        final String typeID = arguments.getString("Type");

        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        String Uid  = mAuth.getUid();

        //Toast.makeText(getActivity(), Uid, Toast.LENGTH_SHORT).show();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_DownloadList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final ArrayList<String> URL = new ArrayList<>();
        final ArrayList<String> Title = new ArrayList<>();

        arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                URL
        );

        final ArrayAdapter<String> titleAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                Title
        );

        databaseReference = FirebaseDatabase.getInstance().getReference().child("database").child("Batch").child(batchID).child("Downloads").child(typeID);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    arrayAdapter.add(singleSnapshot.child("image").getValue().toString());
                    titleAdapter.add(singleSnapshot.child("title").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<DownloadList,DownloadListFragment.DownloadViewHolder> recyclerAdapter= new FirebaseRecyclerAdapter<DownloadList, DownloadViewHolder>(
                DownloadList.class,
                R.layout.download_cardview,
                DownloadViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(DownloadViewHolder viewHolder, DownloadList model, int position) {
                viewHolder.setTitle(model.gettitle());
                //viewHolder.setImage(model.getimage());
                viewHolder.setDate(model.getDate());
            }


            @Override
            public DownloadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                DownloadViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new DownloadViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getActivity(), "Item click at " + arrayAdapter.getItem(position), Toast.LENGTH_SHORT).show();

                        DownloadImageFragment fragment = new DownloadImageFragment();
                        Bundle arguments = new Bundle();
                        arguments.putString( "Batch" , batchID);
                        arguments.putString( "Type" , typeID);
                        arguments.putString( "Url", arrayAdapter.getItem(position));
                        arguments.putString( "Title", (String) titleAdapter.getItem(position));

                        fragment.setArguments(arguments);
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.content, fragment , "Download List");
                        ft.commit();

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        return ;
                    }
                });

                return viewHolder;
            }
        };

        recyclerView.setAdapter(recyclerAdapter);


        return view;
    }

    public static class DownloadViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView textView_Title;
        TextView textView_Date;
        TextView textView_Image;

        String simage,stitle;

        private static Context context;

        public DownloadViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            context = itemView.getContext();
            textView_Title = (TextView) itemView.findViewById(R.id.textView_download_title);
            textView_Image = (TextView) itemView.findViewById(R.id.textView_image_download);
            textView_Date = (TextView) itemView.findViewById(R.id.textView_download_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mClickListener.onItemLongClick(v, getAdapterPosition());
                    return true;
                }
            });
        }

        private DownloadViewHolder.ClickListener mClickListener;

        //Interface to send callbacks...
        public interface ClickListener{
            public void onItemClick(View view, int position);
            public void onItemLongClick(View view, int position);
        }

        public void setOnClickListener(DownloadViewHolder.ClickListener clickListener){
            mClickListener = clickListener;
        }

        public void setTitle(String title)
        {
            stitle = title;
            textView_Title.setText(title);
        }
        public void setDate(String date)
        {
            textView_Date.setText(date);
        }

        public void setImage(String image)
        {
            simage = image;
            arrayAdapter.add(image);
            //textView_Image.setText(image);
        }
    }
}
