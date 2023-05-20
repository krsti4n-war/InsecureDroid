package lab.insecuredroid.challenges;

import static lab.insecuredroid.challenges.utils.AESCryptoHelper.encrypt;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import lab.insecuredroid.MainActivity;
import lab.insecuredroid.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        String username = getIntent().getStringExtra("username");

        EditText newPasswordInput = findViewById(R.id.new_password_input);
        EditText confirmNewPasswordInput = findViewById(R.id.confirm_new_password_input);
        Button resetButton = findViewById(R.id.reset_button);

        resetButton.setOnClickListener(view -> {
            String newPassword = newPasswordInput.getText().toString();
            String confirmNewPassword = confirmNewPasswordInput.getText().toString();
            if (newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                Toast.makeText(this,"Please input the new password!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Objects.equals(newPassword, confirmNewPassword)){
                Toast.makeText(this,"The new password and confirm password doesn't match!", Toast.LENGTH_SHORT).show();
                return;
            }

            changePassword(username, newPassword);
        });

    }

    private void changePassword(String username, String newPassword) {
        String RESET_URL = "http://68.183.235.219:8000/change-password";
        OkHttpClient okHttpClient = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("new_password", encrypt(newPassword));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Request request = new Request.Builder()
                .url(RESET_URL)
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

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } catch (IOException | JSONException | RuntimeException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}