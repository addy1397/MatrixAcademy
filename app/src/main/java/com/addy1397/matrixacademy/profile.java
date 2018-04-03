package com.addy1397.matrixacademy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ForwardingListener;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class profile extends AppCompatActivity {

    private android.support.v7.widget.Toolbar mtoolbar;

    private FirebaseDatabase firebaseDatabase;

    private FirebaseAuth mAuth;
    CircleImageView circleImageView;

    static byte[] thumb_byte_;

    private StorageReference storageReference;

    private TextView textView_name;
    private TextView textView_email;
    private TextView textView_phone;

    private ProgressDialog pd;

    private ImageButton imageButton_change;

    private ImageButton imageButton;
    private ImageButton imageButton_faculty;

    private static final int GALLERY_PICK = 1;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        pd = new ProgressDialog(getApplicationContext());

        mtoolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setIcon(R.drawable.ic_dashboard_black_24dp);
        getSupportActionBar().setTitle(R.string.app_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        imageButton_faculty  = (ImageButton) findViewById(R.id.imageButton_faculty);

        imageButton_faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profile.this, FacultyActivity.class);
                startActivity(intent);
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        textView_email = (TextView) findViewById(R.id.textView_email);
        textView_name = (TextView) findViewById(R.id.textView_name);
        textView_phone = (TextView) findViewById(R.id.textView_phone);

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        final String Uid = current_user.getUid();

        circleImageView = (CircleImageView) findViewById(R.id.circle_view_profile);

        final String ss = getIntent().getStringExtra("type");
        //Toast.makeText(this, ss, Toast.LENGTH_LONG).show();
        try {
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("database").child(ss).child(Uid);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Toast.makeText(profile.this, dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
                    final String Sname = dataSnapshot.child("name").getValue().toString();
                    final String Semail = dataSnapshot.child("email").getValue().toString();
                    final String Sphone = dataSnapshot.child("phone").getValue().toString();
                    final String Simage = dataSnapshot.child("image").getValue().toString();

                    textView_name.setText(Sname);
                    textView_email.setText(Semail);
                    textView_phone.setText(Sphone);

                    if(!Simage.matches("default")) {
                        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

                        StorageReference filepath = FirebaseStorage.getInstance().getReference().child("Profile").child(UID);

                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.with(getApplicationContext()).load(uri.toString()).into(circleImageView);
                            }
                        });
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        catch(Exception e){
            Toast.makeText(this, "Can't retrive", Toast.LENGTH_SHORT).show();
        }


        Button button = (Button) findViewById(R.id.button_logout);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Signing Out");
                progressDialog.setMessage("Please wait!!");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                try{
                    FirebaseAuth.getInstance().signOut();
                    progressDialog.dismiss();
                    Toast.makeText(profile.this, "Signed Out!!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(profile.this,SigninActivity.class);
                    startActivity(intent);
                    finish();
                }
                catch(Exception e) {
                    progressDialog.hide();
                    Toast.makeText(profile.this, "Failed to sign out"+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        pd.setMessage("Uploading");

        final Button Upload = (Button)findViewById(R.id.button_UPLOAD);

        imageButton_change = (ImageButton) findViewById(R.id.imageButton_changePhoto);


        imageButton_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"),GALLERY_PICK);

                Upload.setVisibility(View.VISIBLE);

            }
        });


        Upload.setVisibility(View.INVISIBLE);

        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

                StorageReference filepath = FirebaseStorage.getInstance().getReference().child("Profile").child(UID);

                final UploadTask uploadTask = filepath.putBytes(thumb_byte_);

                String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("database").child(ss).child(Uid).child("image");
                databaseReference.setValue(Uid);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //pd.dismiss();

                        Intent intent = getIntent();
                        intent.putExtra("type",ss);
                        finish();
                        startActivity(intent);

                    }
                });
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(100);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK)
        {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);


            //Toast.makeText(getActivity(), "image"+imageUri.toString(), Toast.LENGTH_SHORT).show();

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();


                //Toast.makeText(getActivity(), "URI" + resultUri.toString(), Toast.LENGTH_SHORT).show();

                File thumb_filepath = new File(resultUri.getPath());

                Bitmap thumb_bitmap;

                try {
                    thumb_bitmap = new Compressor((Context) getApplicationContext())
                            .setMaxHeight(200)
                            .setMaxWidth(200)
                            .setQuality(75)
                            .compressToBitmap(thumb_filepath);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    thumb_byte_ = baos.toByteArray();

                    circleImageView.setImageBitmap(thumb_bitmap);

                    //circleImageView.setImageURI(resultUri);

                    //Toast.makeText(getActivity(), thumb_bitmap.toString(), Toast.LENGTH_LONG).show();


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(profile.this, "Exception Failed", Toast.LENGTH_SHORT).show();
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(profile.this, "Failed", Toast.LENGTH_SHORT).show();
                Exception error = result.getError();
            }
        }
    }
}