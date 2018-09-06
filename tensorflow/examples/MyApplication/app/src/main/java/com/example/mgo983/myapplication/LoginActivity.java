package com.example.mgo983.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * Created by mgo983 on 9/4/18.
 */

public class LoginActivity extends Activity {

    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public static final String USERNAME = "com.example.mgo983.myapplication.USERNAME";

    @Override

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button = findViewById(R.id.login);
        signInAnonymously();

        final EditText editTextFirstName = (EditText) findViewById(R.id.first_name);
        final EditText editTextLastName = (EditText) findViewById(R.id.last_name);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*OrderInstructions orderInstructions = new OrderInstructions(
                        1,0,0,0,0,
                        0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0);*/
                /*Map<String, Object> result = orderInstructions.toMap();
                *DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user");
                * String key = databaseReference.push().getKey();
                databaseReference.child(key).setValue(result);*/

                String firstName = editTextFirstName.getText().toString();
                String lastName = editTextLastName.getText().toString();
                String userName = firstName + lastName;

                Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                mainActivityIntent.putExtra(USERNAME, userName);
                startActivity(mainActivityIntent);

            }
        });

    }

    public void signInAnonymously() {
        firebaseAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

            }


        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }



}
