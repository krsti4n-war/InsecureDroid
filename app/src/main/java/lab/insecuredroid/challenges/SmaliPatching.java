package lab.insecuredroid.challenges;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Objects;

import lab.insecuredroid.R;
import lab.insecuredroid.challenges.utils.SHA256HashHelper;

public class SmaliPatching extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_smali_patching, container, false);

        EditText usernameInput = view.findViewById(R.id.username_input);
        EditText passwordInput = view.findViewById(R.id.password_input);
        Button loginButton = view.findViewById(R.id.login_button);

        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(),"Username and Password data must be filled!", Toast.LENGTH_SHORT).show();
                return;
            }

            login(username, password);
        });

        Button hint1Button = view.findViewById(R.id.hint1_button);
        Button hint2Button = view.findViewById(R.id.hint2_button);
        TextView hintTextView = view.findViewById(R.id.hint_text_view);

        hint1Button.setOnClickListener(v -> hintTextView.setText(R.string.smali_patching_hint1));
        hint2Button.setOnClickListener(v -> hintTextView.setText(R.string.smali_patching_hint2));

        return view;
    }

    private void login(String username, String password) {
        if (Objects.equals(username, "admin") &&
                Objects.equals(SHA256HashHelper.hashPassword(password), "bc7e8885a784d1892be500ac31e5be7b37d8a47da278f31b790833ffbdc901e8")){
            Toast.makeText(requireContext(),"Welcome back administrator.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(requireContext(),"You are not administrator!", Toast.LENGTH_LONG).show();
        }
    }
}