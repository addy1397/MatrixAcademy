package com.addy1397.matrixacademy;

import android.net.Uri;
import android.os.TransactionTooLargeException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class FacultyActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    private RecyclerView recyclerView;
    private Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar_faculty);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setIcon(R.drawable.ic_dashboard_black_24dp);
        getSupportActionBar().setTitle(R.string.app_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_faculty);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("database").child("Teacher");

        FirebaseRecyclerAdapter<Teacher,BlogViewHolder> recyclerAdapter=new FirebaseRecyclerAdapter<Teacher,BlogViewHolder>(
                Teacher.class,
                R.layout.faculty_card_view,
                BlogViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Teacher model, int position) {
                viewHolder.setname(model.getName());
                viewHolder.setSpecialization(model.getSpecialization_());
                viewHolder.setbiodata(model.getBiodata());
                viewHolder.setImage(model.getImage());
            }
        };
        recyclerView.setAdapter(recyclerAdapter);

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView textView_name;
        TextView textView_specailization;
        TextView textView_bio;
        ImageView imageView;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            textView_name = (TextView)itemView.findViewById(R.id.textView_faculty_name);
            textView_specailization = (TextView) itemView.findViewById(R.id.textView_faculty_specs);
            textView_bio = (TextView) itemView.findViewById(R.id.textView_faculty_bio);
            imageView=(ImageView)itemView.findViewById(R.id.imageView_faculty_image);
        }
        public void setname(String name)
        {
            textView_name.setText(name);
        }
        public void setSpecialization(String specs)
        {
            textView_specailization.setText(specs);
        }
        public void setbiodata(String biodata)
        {
            textView_bio.setText(biodata);
        }
        public void setImage(String image)
        {
            Toast.makeText(mView.getContext(), image, Toast.LENGTH_SHORT).show();
            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("Profile");

            filepath.child(image).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    //Toast.makeText(mView.getContext(), uri.toString(), Toast.LENGTH_SHORT).show();
                    Picasso.with(mView.getContext()).load(uri.toString()).into(imageView);
                }
            });
        }
    }
}
