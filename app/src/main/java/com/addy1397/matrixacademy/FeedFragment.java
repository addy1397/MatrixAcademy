package com.addy1397.matrixacademy;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {

    private RecyclerView recyclerView;

    private DatabaseReference databaseReference;

    public static int[] colors = {R.color.colorred,R.color.colorblue,R.color.colorbluegray,R.color.colorcyan,
            R.color.colorgray,R.color.colorgreen,R.color.colororange,R.color.colorAccent,
            R.color.colorpurple};

    public static Random rand;

    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        //ListView listview = (ListView) view.findViewById(R.id.List_View);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("database").child("feed");

        rand = new Random();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter< Feed , FeedViewHolder> firebaseRecyclerAdapter  = new FirebaseRecyclerAdapter<Feed, FeedViewHolder>(
                Feed.class,
                R.layout.feedfragment_layout,
                FeedViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(FeedViewHolder viewHolder, Feed model, int position) {

                viewHolder.settitle(model.getTitle());
                viewHolder.setdescription(model.getDescription());
                viewHolder.setdate(model.getDate());
                viewHolder.setimage(model.getImage());
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
     }


    public static class FeedViewHolder extends RecyclerView.ViewHolder{

        View view;

        public FeedViewHolder(View itemView) {
            super(itemView);
            view = itemView;

        }

        public void settitle(String title){

            TextView textView_title = (TextView) view.findViewById(R.id.textView_Title);
            ImageView image_background = (ImageView) view.findViewById(R.id.imageView_feed_bacground);
            int n = rand.nextInt(9);
            image_background.setColorFilter(colors[n]);
            textView_title.setText(title);
        }

        public void setdescription(String desc){

            TextView textView_desc = (TextView) view.findViewById(R.id.textView_description);
            textView_desc.setText(desc);

        }

        public void setdate(String date){

            TextView textView_date = (TextView) view.findViewById(R.id.textView_date);
            textView_date.setText(date);

        }

        public void setimage(String image){
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView_feed);
            try {

                Glide.with(view.getContext())
                        .load(image)
                        .into(imageView);
                //Picasso.with(view.getContext()).setLoggingEnabled(true);
                /*Picasso.with(view.getContext())
                        .load(image)
                        .resize(201, 201)
                        .into(imageView);*/
            }
            catch(Exception e){
                Toast.makeText(view.getContext(), "Exception "+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

