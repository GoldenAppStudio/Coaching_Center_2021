
package com.goldenapp.questionhub;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.shuhart.stepview.StepView;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import static com.goldenapp.questionhub.MainActivity.isNetworkAvailable;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        if(!isNetworkAvailable(this)) {
            Toast.makeText(this,"Connect to internet first", Toast.LENGTH_LONG).show();
            finish(); // Calling this method to close this activity when internet is not available.
            System.exit(0);
        }
    }

    private int currentStep = 0;
    LinearLayout layout1,layout2;
    StepView stepView;
    AlertDialog dialog_verifying, profile_dialog;

    private static String uniqueIdentifier = null;
    private static final String UNIQUE_ID = "UNIQUE_ID";
    private static final long ONE_HOUR_MILLI = 60*60*1000;

    private static final String TAG = "FirebasePhoneNumAuth";

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth firebaseAuth;

    private String phoneNumber;
    private Button sendCodeButton;
    private Button verifyCodeButton;
    private Button signOutButton;
    private Button button3;

    private EditText phoneNum;
    private PinView verifyCodeET;
    private TextView phonenumberText;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private FirebaseAuth mAuth;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        sendCodeButton = findViewById(R.id.submit1);
        verifyCodeButton = findViewById(R.id.submit2);
        firebaseAuth = FirebaseAuth.getInstance();
        phoneNum = findViewById(R.id.phonenumber);
        verifyCodeET = findViewById(R.id.pinView);
        phonenumberText = findViewById(R.id.phonenumberText);

        stepView = findViewById(R.id.step_view);
        stepView.setStepsNumber(2);
        stepView.go(0, true);
        layout1.setVisibility(View.VISIBLE);

        sendCodeButton.setOnClickListener(view -> {
            String phoneNumber = phoneNum.getText().toString();
            phonenumberText.setText(phoneNumber);

            if (TextUtils.isEmpty(phoneNumber)) {
                phoneNum.setError("Enter a Phone Number");
                phoneNum.requestFocus();
            } else if (phoneNumber.length() < 10) {
                phoneNum.setError("Please enter a valid phone");
                phoneNum.requestFocus();
            } else {
                if (currentStep < stepView.getStepCount() - 1) {
                    currentStep++;
                    stepView.go(currentStep, true);
                } else stepView.done(true);

                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + phoneNumber,  // Phone number to verify
                        60,     // Timeout duration
                        TimeUnit.SECONDS, // Unit of timeout
                        LoginActivity.this, // Activity (for callback binding)
                        mCallbacks);  // OnVerificationStateChangedCallbacks
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NotNull PhoneAuthCredential phoneAuthCredential) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout= inflater.inflate(R.layout.processing_dialog,null);
                AlertDialog.Builder show = new AlertDialog.Builder(LoginActivity.this);

                show.setView(alertLayout);
                show.setCancelable(false);
                dialog_verifying = show.create();
                dialog_verifying.show();
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.e(TAG, e.getMessage());
            }
            @Override
            public void onCodeSent(@NotNull String verificationId,
                                   @NotNull PhoneAuthProvider.ForceResendingToken token) {
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                // ...
            }
        };

        verifyCodeButton.setOnClickListener(view -> {
            String verificationCode = verifyCodeET.getText().toString();
            if(verificationCode.isEmpty()){
                Toast.makeText(LoginActivity.this,"Enter verification code", Toast.LENGTH_SHORT).show();
            }else {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout= inflater.inflate(R.layout.processing_dialog,null);
                AlertDialog.Builder show = new AlertDialog.Builder(LoginActivity.this);

                show.setView(alertLayout);
                show.setCancelable(false);
                dialog_verifying = show.create();
                dialog_verifying.show();

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                signInWithPhoneAuthCredential(credential);
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        dialog_verifying.dismiss();
                        if (currentStep < stepView.getStepCount() - 1) {
                            currentStep++;
                            stepView.go(currentStep, true);
                        } else {
                            stepView.done(true);
                        }
                        layout1.setVisibility(View.GONE);
                        layout2.setVisibility(View.GONE);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        // ...
                    } else {
                        dialog_verifying.dismiss();
                        Toast.makeText(LoginActivity.this,"OTP is Incorrect OR Network Connection slow...", Toast.LENGTH_LONG).show();
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {}
                    }
                });
    }
}
