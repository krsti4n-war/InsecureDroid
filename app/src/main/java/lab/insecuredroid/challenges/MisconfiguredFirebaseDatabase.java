package lab.insecuredroid.challenges;

import android.content.Intent;
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

public class MisconfiguredFirebaseDatabase extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_misconfigured_firebase_database, container, false);

        EditText usernameInput = view.findViewById(R.id.username_input);
        Button requestOTPButton = view.findViewById(R.id.request_otp_button);
        Button hintButton = view.findViewById(R.id.hint_button);
        TextView hintTextView = view.findViewById(R.id.hint_text_view);

        requestOTPButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            requestOTP(username);
        });

        hintButton.setOnClickListener(v -> hintTextView.setText(R.string.misconfigured_firebase_database_hint));

        return view;
    }

    private void requestOTP(String username) {
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
                requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(),"Request Failed : " + e.getMessage(), Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    String responseBody = response.body().string();
                    JSONObject responseJson = new JSONObject(responseBody);
                    String message = responseJson.getString("message");
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show());

                    Intent intent = new Intent(requireActivity(), VerifyOTPActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                } catch (IOException | JSONException | RuntimeException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}