package com.addy1397.matrixacademy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import id.zelory.compressor.Compressor;

public class AddFeedActivity extends AppCompatActivity {

    private Toolbar toolbar;
    Uri downloadUri;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StorageReference storageReference,mStorageReference;

    ProgressDialog pd;

    String filestore,imageUri;

    public static Uri filePath;

    private static ImageButton imageButton;
    private Button button_publish;

    private byte[] thumb_byte;

    public final static int GALLERY_PICK = 1;


    private EditText editText_title;
    private EditText editText_description;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feed);

        toolbar = (Toolbar) findViewById(R.id.toolbar_addfeed);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.matrix_academy);
        getSupportActionBar().setIcon(R.drawable.ic_dashboard_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        pd = new ProgressDialog(AddFeedActivity.this);

        /*storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://matrixacademy-74ef5.appspot.com/");
        filestore = "gs://matrixacademy-74ef5.appspot.com/";*/
        databaseReference = FirebaseDatabase.getInstance().getReference();
        editText_title = (EditText) findViewById(R.id.editText_feedTitle);
        editText_description = (EditText) findViewById(R.id.editText_feedDescription);

        mStorageReference = FirebaseStorage.getInstance().getReference();

        imageButton = (ImageButton) findViewById(R.id.imageButton_feed);

        button_publish  = (Button) findViewById(R.id.button_feedPublish);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(AddFeedActivity.this, "Image Button Pressed", Toast.LENGTH_SHORT).show();

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"),GALLERY_PICK);


            }
        });

        pd.setMessage("Uploading...");

        button_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Stitle = editText_title.getText().toString();
                final String Sdesc = editText_description.getText().toString();

                Calendar calendar = Calendar.getInstance();
                final int date = calendar.get(Calendar.DATE);
                final int month = calendar.get(Calendar.MONTH)+1;
                final int year = calendar.get(Calendar.YEAR);
                final int hour = calendar.get(Calendar.HOUR);
                final int minute = calendar.get(Calendar.MINUTE);

                String Cur_Date1 = "" + (3000-year) + ToString(12 - month) + ToString(31 - date) + ToString(24 - hour) + ToString(60-minute);

                pd.show();

                StorageReference filepath = mStorageReference.child("Feed").child(Cur_Date1+".jpg");

                final UploadTask uploadTask = filepath.putBytes(thumb_byte);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if(uploadTask.isSuccessful()) {
                            downloadUri = taskSnapshot.getDownloadUrl();
                            imageUri = downloadUri.toString();
                            pd.dismiss();
                            imageButton.setImageURI(downloadUri);
                            Picasso.with(getApplicationContext()).load(downloadUri).into(imageButton);

                            //Toast.makeText(AddFeedActivity.this, imageUri, Toast.LENGTH_LONG).show();
                            Calendar calendar = Calendar.getInstance();
                            final int date = calendar.get(Calendar.DATE);
                            final int month = calendar.get(Calendar.MONTH)+1;
                            final int year = calendar.get(Calendar.YEAR);
                            final int hour = calendar.get(Calendar.HOUR);
                            final int minute = calendar.get(Calendar.MINUTE);

                            String Current_date  = hour + ":" + minute + " " + date + "-" + month + "-" + year;

                            String Cur_Date = "" + (3000-year) + ToString(12 - month) + ToString(31 - date) + ToString(24 - hour) + ToString(60-minute);
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("database").child("feed").child(Cur_Date);

                            Toast.makeText(AddFeedActivity.this, imageUri, Toast.LENGTH_LONG).show();

                            HashMap<String,String> hashMap = new HashMap<>();

                            hashMap.put("date",Current_date);
                            hashMap.put("title",Stitle);
                            hashMap.put("description",Sdesc);
                            hashMap.put("image",imageUri);

                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                pd.dismiss();
                                Toast.makeText(AddFeedActivity.this, "Success!!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                pd.hide();
                                Toast.makeText(AddFeedActivity.this, "Failed Re-Try", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                        }
                    }
                });
            }
        });

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
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                //imageButton.setImageURI(resultUri);

                File thumb_filepath = new File(resultUri.getPath());

                Bitmap thumb_bitmap;

                try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxHeight(200)
                            .setMaxWidth(200)
                            .setQuality(50)
                            .compressToBitmap(thumb_filepath);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    thumb_byte = baos.toByteArray();


                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(30);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
