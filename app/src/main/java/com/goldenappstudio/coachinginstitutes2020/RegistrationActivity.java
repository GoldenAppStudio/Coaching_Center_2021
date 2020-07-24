
package com.goldenappstudio.coachinginstitutes2020;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private Button continueRegister ;
    private EditText fullname;
    private EditText stu_class;
    private EditText email;
    private EditText place;
    private EditText gender;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_registration);

        continueRegister = findViewById(R.id.continueRegister);
        fullname = findViewById(R.id.fullname);
        stu_class = findViewById(R.id.stu_class);
        email = findViewById(R.id.email);
        gender = findViewById(R.id.gender);
        place = findViewById(R.id.birthpalce);

        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();


        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    if (task.getResult().exists()) {

                        String name = task.getResult().getString("name");
                        String ageOFName = task.getResult().getString("Class");
                        String mail = task.getResult().getString("mail");
                        String address = task.getResult().getString("address");
                        String genderOfName = task.getResult().getString("gender");

                        fullname.setText(name);
                        stu_class.setText(ageOFName);
                        email.setText(mail);
                        place.setText(address);
                        gender.setText(genderOfName);

                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(RegistrationActivity.this, "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();

                }

            }
        });


        continueRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_name = fullname.getText().toString();
                final String user_address = place.getText().toString();
                final String user_email = email.getText().toString();
                final String user_class = stu_class.getText().toString();
                final String user_gender = gender.getText().toString();

                if (!TextUtils.isEmpty(user_name) && !TextUtils.isEmpty(user_gender) && (!TextUtils.isEmpty(user_address)) && (!TextUtils.isEmpty(user_class))) {


                    storeFirestore(user_name, user_email, user_address, user_class, user_gender);

                }

                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void storeFirestore(String user_name, String user_gender, String user_class, String user_address, String user_email) {

        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", user_name);
        userMap.put("gender", user_email);
        userMap.put("address", user_class);
        userMap.put("Class", user_address);
        userMap.put("mail", user_gender);

        firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(RegistrationActivity.this, "The user Settings are updated.", Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(RegistrationActivity.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();

                }

            }
        });


    }

}
