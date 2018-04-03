package com.addy1397.matrixacademy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.PortUnreachableException;

public class SigninActivity extends AppCompatActivity {

    //add connected to internet
    private FirebaseAuth mAuth;

    private DatabaseReference databaseReference;

    private ProgressDialog mprogressdialog;

    boolean flag;

    private Switch mswitch;

    String change(String s){
        String p="";
        for(char c:s.toCharArray())
            if(c!='@' && c!='.')
                p+=c;
        return p;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        Button button = (Button) findViewById(R.id.button_signin);

        //final TextView titleMA = (TextView) findViewById(R.id.title_mA);
        //Typeface customfont = Typeface.createFromAsset(getAssets(),"fonts/carolyna.otf");

        //titleMA.setTypeface(customfont);

        mswitch = (Switch) findViewById(R.id.Switch);

        final EditText mail = (EditText) findViewById(R.id.edit_text_email);
        final EditText password = (EditText) findViewById(R.id.edit_text_password);

        mAuth = FirebaseAuth.getInstance();
        mprogressdialog = new ProgressDialog(SigninActivity.this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String smail = mail.getText().toString();
                final String spassword = password.getText().toString();

                if(smail.matches("") || spassword.matches(""))
                {
                    Toast.makeText(SigninActivity.this, "Fill In The Details", Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(SigninActivity.this, SigninActivity.class);
                    //startActivity(intent);
                }
                else {
                    mprogressdialog.setTitle("Signing In");
                    mprogressdialog.setMessage("Please Wait While Your Credentials Are Being Verified!");
                    mprogressdialog.setCanceledOnTouchOutside(false);
                    mprogressdialog.show();

                    final String[] user_ID = new String[10];

                    flag = mswitch.isChecked();

                    final String[] userID = new String[2];

                    mAuth.signInWithEmailAndPassword(smail, spassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Toast.makeText(SigninActivity.this, "WELCOME", Toast.LENGTH_LONG).show();
                                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                                String Uid = current_user.getUid();


                                try {
                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("database").child("Email").child(change(smail));
                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            userID[0] = dataSnapshot.child("ID").getValue().toString();
                                            //Toast.makeText(SigninActivity.this, userID, Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                                } catch (Exception e) {
                                    mprogressdialog.dismiss();
                                    Toast.makeText(SigninActivity.this, "Email can't be verified!!", Toast.LENGTH_SHORT).show();

                                    FirebaseAuth.getInstance().signOut();

                                    Intent intent = new Intent(SigninActivity.this, SigninActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                                try {
                                    DatabaseReference mdatabaseReference = FirebaseDatabase.getInstance().getReference().child("database").child("User");
                                    mdatabaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            //userID[1] = dataSnapshot.child("type").getValue().toString();
                                            userID[1] = dataSnapshot.child(userID[0]).child("type").getValue().toString();
                                            Toast.makeText(SigninActivity.this, userID[1], Toast.LENGTH_LONG).show();

                                            if (flag == true && userID[1].matches("Student")) {
                                                mprogressdialog.dismiss();
                                                Intent intent = new Intent(SigninActivity.this, StudentActivity.class);

                                                intent.putExtra("type", "Student");

                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            } else if (flag == false && userID[1].matches("Teacher")) {
                                                mprogressdialog.dismiss();
                                                Intent intent = new Intent(SigninActivity.this, TeacherActivity.class);

                                                intent.putExtra("type", "Teacher");

                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            } else {
                                                mprogressdialog.dismiss();
                                                Toast.makeText(SigninActivity.this, "Teacher - Student problem occuring", Toast.LENGTH_SHORT).show();

                                                FirebaseAuth.getInstance().signOut();

                                                Intent intent = new Intent(SigninActivity.this, SigninActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                                } catch (Exception e) {
                                    mprogressdialog.dismiss();
                                    Toast.makeText(SigninActivity.this, "No Data exists !!", Toast.LENGTH_SHORT).show();

                                    FirebaseAuth.getInstance().signOut();

                                    Intent intent = new Intent(SigninActivity.this, SigninActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            } else {
                                mprogressdialog.dismiss();
                                Toast.makeText(SigninActivity.this, "Re-try", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(SigninActivity.this, SigninActivity.class);

                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        // add teacher and student check-in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            //Intent intent = new Intent(SigninActivity.this,AddFeedActivity.class);
            //startActivity(intent);
            //finish();

            mprogressdialog.setTitle("Signing In");
            mprogressdialog.setMessage("Please Wait While You're Being Signed In");
            mprogressdialog.setCanceledOnTouchOutside(false);
            mprogressdialog.show();

            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
            final String Uid = current_user.getUid();

            DatabaseReference mdatabaseReference = FirebaseDatabase.getInstance().getReference().child("database").child("User");

            mdatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //userID[1] = dataSnapshot.child("type").getValue().toString();
                    String ss = dataSnapshot.child(Uid).child("type").getValue().toString();
                    Toast.makeText(SigninActivity.this, ss , Toast.LENGTH_LONG).show();

                    if(ss.matches("Student")){
                        mprogressdialog.dismiss();
                        Intent intent = new Intent(SigninActivity.this , StudentActivity.class);
                        intent.putExtra("type","Student");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else{
                        mprogressdialog.dismiss();
                        Intent intent = new Intent(SigninActivity.this , TeacherActivity.class);
                        intent.putExtra("type","Teacher");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }
}
