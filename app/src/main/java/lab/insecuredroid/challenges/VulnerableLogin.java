package lab.insecuredroid.challenges;

import static lab.insecuredroid.challenges.utils.AESCryptoHelper.encrypt;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import lab.insecuredroid.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VulnerableLogin extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vulnerable_login, container, false);

        EditText usernameInput = view.findViewById(R.id.username_input);
        EditText passwordInput = view.findViewById(R.id.password_input);
        TextView forgotPasswordTextView = view.findViewById(R.id.forgot_password_text_view);
        Button loginButton = view.findViewById(R.id.login_button);
        Button registerButton = view.findViewById(R.id.register_button);

        forgotPasswordTextView.setOnClickListener(v -> Toast.makeText(requireContext(),"Sorry the feature still on working!", Toast.LENGTH_LONG).show());

        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(),"Username and Password data must be filled!", Toast.LENGTH_SHORT).show();
                return;
            }

            login(username, password);
        });

        registerButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(),"Username and Password data must be filled!", Toast.LENGTH_SHORT).show();
                return;
            }

            register(username, password);
        });

        Button hint1Button = view.findViewById(R.id.hint1_button);
        Button hint2Button = view.findViewById(R.id.hint2_button);
        TextView hintTextView = view.findViewById(R.id.hint_text_view);

        hint1Button.setOnClickListener(v -> hintTextView.setText(R.string.vulnerable_login_hint1));
        hint2Button.setOnClickListener(v -> hintTextView.setText(R.string.vulnerable_login_hint2));

        return view;
    }

    private void login(String username, String password) {
        String LOGIN_URL = "http://68.183.235.219:8000/login";
        OkHttpClient okHttpClient = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", encrypt(password));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Request request = new Request.Builder()
                .url(LOGIN_URL)
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(),"Request Failed : " + e.getMessage(), Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    String responseBody = response.body().string();
                    JSONObject responseJson = new JSONObject(responseBody);
                    String message = responseJson.getString("message");
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show());

                    if (response.isSuccessful()){
                        String jwtToken = responseJson.getString("token");
                        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("sessions", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", jwtToken);
                        editor.apply();
                    }
                } catch (IOException | JSONException | RuntimeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void register(String username, String password) {
        String REGISTER_URL = "http://68.183.235.219:8000/register";
        OkHttpClient okHttpClient = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", encrypt(password));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Request request = new Request.Builder()
                .url(REGISTER_URL)
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(),"Request Failed : " + e.getMessage(), Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    String responseBody = response.body().string();
                    JSONObject responseJson = new JSONObject(responseBody);
                    String message = responseJson.getString("message");
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show());
                } catch (IOException | JSONException | RuntimeException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}