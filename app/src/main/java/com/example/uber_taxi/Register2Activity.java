package com.example.uber_taxi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register2Activity extends AppCompatActivity {

    EditText email;
    EditText pwd;
    EditText confirm_pwd;
    Button reg_btn;
    TextView login;
    EditText dob;
    EditText mobile;
    EditText addr;
    EditText pincode;
    Switch user_type;
    EditText name;
    private FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        email = (EditText) findViewById(R.id.regEmail);
        pwd = (EditText) findViewById(R.id.regPwd);
        confirm_pwd= (EditText) findViewById(R.id.regPwdConfirm);

        dob = (EditText) findViewById(R.id.regDOB);
        mobile = (EditText) findViewById(R.id.regMobile);
        addr = (EditText) findViewById(R.id.regAddress);
        name = (EditText) findViewById(R.id.regName);
        reg_btn = (Button) findViewById(R.id.regBtn);
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerUser();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                Intent registerIntent = new Intent(Register2Activity.this, MainActivity.class);
                startActivity(registerIntent);
            }
        });

    }

    public void registerUser(){

        String dname = name.getText().toString();
        String ddob = dob.getText().toString();
        String daddr = addr.getText().toString();

        String dmobile =  mobile.getText().toString();
        String demail = email.getText().toString();
        String dpwd = pwd.getText().toString();
        String dconfirm_pwd = confirm_pwd.getText().toString();

        if(dpwd.equals(dconfirm_pwd)){

            //inserting into database
            rootNode = FirebaseDatabase.getInstance();//include all the tables of the database
            System.out.println("root node is "+rootNode);
            reference = rootNode.getReference("Customer");

            UserHelperClass m1 = new UserHelperClass(demail, dmobile, dpwd,dname,daddr,ddob);
            //stored in database
            //reference.setValue("First Data stored");
            reference.child(dmobile).setValue(m1);
            Toast.makeText(this, "Registered ", Toast.LENGTH_SHORT).show();

            Intent myIntent = new Intent(Register2Activity.this, MainActivity.class);
            startActivity(myIntent);

            Toast.makeText(this, "Successfully registered", Toast.LENGTH_SHORT).show();

        }
        else{
            Toast.makeText(this, "Your password doesn't match, please retry!", Toast.LENGTH_SHORT).show();
        }

}
}
