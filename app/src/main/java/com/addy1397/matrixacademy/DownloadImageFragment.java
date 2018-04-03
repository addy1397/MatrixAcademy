package com.addy1397.matrixacademy;


import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadImageFragment extends Fragment {

    private ProgressDialog progressDialog;

    public DownloadImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_download_image, container, false);

        Bundle arguments = getArguments();
        final String batchID = arguments.getString("Batch");
        final String typeID = arguments.getString("Type");
        final String URL = arguments.getString("Url");
        final String title = arguments.getString("title");

        //Toast.makeText(getActivity(), URL, Toast.LENGTH_SHORT).show();

        progressDialog  = new ProgressDialog(getContext());

        ImageButton imageButton_back = (ImageButton) view.findViewById(R.id.imageButton_back);
        imageButton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadListFragment fragment = new DownloadListFragment();

                Bundle arguments = new Bundle();
                arguments.putString( "Batch" , batchID);
                arguments.putString( "Type" , typeID);

                fragment.setArguments(arguments);
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content, fragment , "Download List");
                ft.commit();
            }
        });

        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Downloads").child(batchID).child(typeID).child(URL);

        final String imageDownloadURL = storageReference.toString();

        final ImageView imageView_show = (ImageView) view.findViewById(R.id.imageView_show);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getContext()).load(uri).into(imageView_show);
            }
        });

        //Toast.makeText(getActivity(), imageDownloadURL, Toast.LENGTH_LONG).show();


        final ImageButton imageButton_Download = (ImageButton) view.findViewById(R.id.imageButton_download);
        imageButton_Download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("Downlaoding...");

                final DownloadManager dm = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);

                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        imageButton_Download.setVisibility(View.GONE);

                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(uri.toString()));
                        dm.enqueue(request);

                        progressDialog.dismiss();
                    }
                });

            }
        });

        return view;
    }

}
