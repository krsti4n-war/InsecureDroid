package lab.insecuredroid.challenges;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import lab.insecuredroid.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VerifyOTPActivity extends AppCompatActivity {
    private int remainingAttempts = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        String username = getIntent().getStringExtra("username");

        EditText otpInput = findViewById(R.id.otp_input);
        Button submitButton = findViewById(R.id.submit_button);
        TextView requestNewOTPTextView = findViewById(R.id.request_new_otp_text_view);

        submitButton.setOnClickListener(v -> {
            String enteredOTP = otpInput.getText().toString();
            if (enteredOTP.isEmpty()) {
                Toast.makeText(this,"Please enter the OTP!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (enteredOTP.length() != 6 ) {
                Toast.makeText(this,"Please enter a valid OTP with 6 digit numbers!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (remainingAttempts > 0) {
                verifyOTP(username, enteredOTP);
            } else {
                Toast.makeText(this, "Maximum attempts reached.", Toast.LENGTH_LONG).show();
                submitButton.setEnabled(false); // Disable the submit button if the maximum attempts are reached
            }
        });

        requestNewOTPTextView.setOnClickListener(v -> requestNewOTP(username));
    }

    private void verifyOTP(String username, String enteredOTP) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference otpRef = database.getReference("users/" + username + "/otp");

        otpRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String storedOTP = dataSnapshot.getValue(String.class);
                if (Objects.equals(storedOTP, enteredOTP)) {
                    Toast.makeText(getApplicationContext(),"OTP match!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), LoginSuccessfulActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    remainingAttempts--;
                    Toast.makeText(getApplicationContext(), "Invalid OTP. " + remainingAttempts + " attempts remaining.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Error code: " + databaseError.getCode());
                Log.e("FirebaseError", "Error message: " + databaseError.getMessage());
                Log.e("FirebaseError", "Error details: " + databaseError.getDetails());
            }
        });
    }


    private void requestNewOTP(String username) {
        String REQUEST_OTP_URL = "http://68.183.235.219:8000/otp";
        OkHttpClient okHttpClient = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Request request = new Request.Builder()
                .url(REQUEST_OTP_URL)
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(),"Request Failed : " + e.getMessage(), Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    String responseBody = response.body().string();
                    JSONObject responseJson = new JSONObject(responseBody);
                    String message = responseJson.getString("message");
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show());
                } catch (IOException | JSONException | RuntimeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}