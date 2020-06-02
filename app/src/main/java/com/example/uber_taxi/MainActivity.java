package com.example.uber_taxi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    EditText mEmail,mPassword;
    Button login,register;

    String dmobile,dpwd;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FusedLocationProviderClient client;

    public static double latitude,longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user!= null){
                    Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                    startActivity(intent);

                    finish();
                    return;
                }
            }
        };

        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(this);

        mEmail= (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);

        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,Register.class);
//                startActivity(intent);

                final String email = mEmail.getText().toString();
                final String pwd = mPassword.getText().toString();
                System.out.println(email+pwd);
                mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
//                            System.out.println(email+pwd);
                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(userId);
                            current_user.setValue(true);

                        }else{
                            Toast.makeText(MainActivity.this, "registration error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(e);
                    }
                });

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                doThis();
//                loginUser();



                final String email = mEmail.getText().toString();
                final String pwd = mPassword.getText().toString();

                mAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Sign in error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }



    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);
    }

//    public void doThis(){}


    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
