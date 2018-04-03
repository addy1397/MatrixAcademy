package com.addy1397.matrixacademy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;


public class UploadFragment2 extends Fragment {

    String filestore;
    Uri downloadUri;

    private DatabaseReference databaseReference;
    Button button_upload_type,button_batch;
    public static byte[] thumb_byte_;

    private StorageReference storageReference;
    public static String stype_,sBatch_;

    public UploadFragment2() {
        // Required empty public constructor
    }

    private ImageButton imageButton;

    private Button button_upload,button_choosetype;

    private String imageUri;
    private EditText editText_title;

    private ProgressDialog pd;

    private final static Integer GALLERY_PICK = 1;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_upload_fragment2, container, false);

        pd = new ProgressDialog(getActivity());
        imageButton = view.findViewById(R.id.imageButton_chooseImage);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"),GALLERY_PICK);

            }
        });

        pd.setMessage("Uploading...");

        editText_title = (EditText) view.findViewById(R.id.editText_uploadTitle);
        button_upload_type = (Button) view.findViewById(R.id.button_upload_type);
        button_upload_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUploadType();
            }
        });
        button_batch = (Button) view.findViewById(R.id.button_choose_batch_upload);
        button_batch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBatch();
            }
        });


        button_upload = view.findViewById(R.id.button_Upload);
        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button button_upload = (Button) view.findViewById(R.id.button_Upload);

                button_upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EditText sTitle = (EditText) view.findViewById(R.id.editText_uploadTitle);

                        final String Stitle = sTitle.getText().toString();

                        if (Stitle.matches("")) {
                            Toast.makeText(getActivity(), "Enter Title", Toast.LENGTH_SHORT).show();
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.content, new UploadFragment2());
                            fragmentTransaction.commit();
                        }

                        if (stype_.matches("")) {
                            Toast.makeText(getActivity(), "Choose File Type", Toast.LENGTH_SHORT).show();
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.content, new UploadFragment2());
                            fragmentTransaction.commit();
                        }

                        if (sBatch_.matches("")) {
                            Toast.makeText(getActivity(), "Choose Batch", Toast.LENGTH_SHORT).show();
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.content, new UploadFragment2());
                            fragmentTransaction.commit();
                        }

                        pd.show();

                        Calendar calendar = Calendar.getInstance();
                        final int date = calendar.get(Calendar.DATE);
                        final int month = calendar.get(Calendar.MONTH) + 1;
                        final int year = calendar.get(Calendar.YEAR);
                        final int hour = calendar.get(Calendar.HOUR);
                        final int minute = calendar.get(Calendar.MINUTE);
                        final int second = calendar.get(Calendar.SECOND);

                        final String Cur_Date1 = "" + (3000 - year) + ToString(12 - month) + ToString(31 - date) + ToString(24 - hour) + ToString(60 - minute) + ToString(60 - second) + ".jpg";

                        StorageReference filepath = FirebaseStorage.getInstance().getReference().child("Downloads").child(sBatch_).child(stype_).child(Cur_Date1);

                        final UploadTask uploadTask = filepath.putBytes(thumb_byte_);

                        final String finalCur_Date = Cur_Date1;
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                if (uploadTask.isSuccessful()) {

                                    downloadUri = taskSnapshot.getDownloadUrl();
                                    imageUri = downloadUri.toString();
                                    pd.dismiss();
                                    imageButton.setImageURI(downloadUri);
                                    Picasso.with(getContext()).load(downloadUri).into(imageButton);



                                    //Toast.makeText(AddFeedActivity.this, imageUri, Toast.LENGTH_LONG).show();
                                    Calendar calendar = Calendar.getInstance();
                                    final int date = calendar.get(Calendar.DATE);
                                    final int month = calendar.get(Calendar.MONTH) + 1;
                                    final int year = calendar.get(Calendar.YEAR);
                                    final int hour = calendar.get(Calendar.HOUR);
                                    final int minute = calendar.get(Calendar.MINUTE);
                                    final int second = calendar.get(Calendar.SECOND);

                                    String Current_date = date + "-" + month + "-" + year;

                                    String Cur_Date = "" + (3000 - year) + ToString(12 - month) + ToString(31 - date) + ToString(24 - hour) + ToString(60 - minute);
                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("database").child("Batch").child(sBatch_).child("Downloads").child(stype_).child(Cur_Date);

                                    Toast.makeText(getContext(), imageUri, Toast.LENGTH_LONG).show();

                                    HashMap<String, String> hashMap = new HashMap<>();

                                    hashMap.put("date", Current_date);
                                    hashMap.put("title", Stitle);
                                    hashMap.put("image", Cur_Date1);

                                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                pd.dismiss();
                                                Toast.makeText(getActivity(), "Success!!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                pd.hide();
                                                Toast.makeText(getActivity(), "Failed Re-Try", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
            }
        });
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(getActivity(), "Started", Toast.LENGTH_SHORT).show();
        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK)
        {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(getContext(), this);


            Toast.makeText(getActivity(), "image"+imageUri.toString(), Toast.LENGTH_SHORT).show();

        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imageButton.setImageURI(resultUri);

                Toast.makeText(getActivity(), "URI" + resultUri.toString(), Toast.LENGTH_SHORT).show();

                File thumb_filepath = new File(resultUri.getPath());

                Bitmap thumb_bitmap;

                try {
                    thumb_bitmap = new Compressor(getActivity())
                            .setMaxHeight(200)
                            .setMaxWidth(200)
                            .setQuality(75)
                            .compressToBitmap(thumb_filepath);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    thumb_byte_ = baos.toByteArray();

                    Toast.makeText(getActivity(), thumb_bitmap.toString(), Toast.LENGTH_LONG).show();

                    imageButton.setImageBitmap(thumb_bitmap);


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Exception Failed", Toast.LENGTH_SHORT).show();
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                Exception error = result.getError();
            }
        }
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(5);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    public void getUploadType(){

        final String type[]={"Assignment","Solution","Question Paper","Miscellaneous"};

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext());
        //alt_bld.setIcon(R.drawable.icon);
        alt_bld.setTitle("Choose Batch");
        alt_bld.setSingleChoiceItems( type, -1, new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getContext(),
                        "Pressed = "+ type[item], Toast.LENGTH_SHORT).show();
                dialog.dismiss();// dismiss the alertbox after chose option

                stype_ = type[item];
                button_upload_type.setText(type[item]);
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    public void getBatch(){

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        final String Uid = mUser.getUid();

        final ArrayList<String> batch = new ArrayList<>();
        final ArrayList<String> any = new ArrayList<>();

        //ListView listView = (ListView) view.findViewById(R.id.listView_showBatch);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                batch
        );
        final ArrayAdapter<String> batchadapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                any
        );

        databaseReference = FirebaseDatabase.getInstance().getReference().child("database").child("Teacher").child(Uid).child("Batches");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                    //final String ss = singleSnapshot.getValue().toString();
                    arrayAdapter.add(singleSnapshot.child("Name").getValue().toString());
                    batchadapter.add(singleSnapshot.child("ID").getValue().toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        for(int i=0;i<arrayAdapter.getCount();i++){
            batch.add(arrayAdapter.getItem(i));
        }
        for(int i=0;i<batchadapter.getCount();i++){
            any.add(batchadapter.getItem(i));
        }


        AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext());
                //alt_bld.setIcon(R.drawable.icon);
        alt_bld.setTitle("Choose Batch");
        alt_bld.setSingleChoiceItems( arrayAdapter, -1, new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getContext(),
                        "Pressed = "+ batch.get(item), Toast.LENGTH_SHORT).show();
                dialog.dismiss();// dismiss the alertbox after chose option

                button_batch.setText(batchadapter.getItem(item));
                sBatch_ = batchadapter.getItem(item);
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }
}
