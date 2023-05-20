package lab.insecuredroid.challenges;

import android.content.Context;
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

import lab.insecuredroid.R;
import lab.insecuredroid.challenges.utils.DBHelper;

public class InsecureContentProvider extends Fragment {
    private DBHelper dbHelper;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dbHelper = new DBHelper(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dbHelper.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_insecure_content_provider, container, false);

        EditText usernameInput = view.findViewById(R.id.username_input);
        EditText passwordInput = view.findViewById(R.id.password_input);
        Button loginButton = view.findViewById(R.id.login_button);
        Button registerButton = view.findViewById(R.id.register_button);
        Button hint1Button = view.findViewById(R.id.hint1_button);
        Button hint2Button = view.findViewById(R.id.hint2_button);
        TextView hintTextView = view.findViewById(R.id.hint_text_view);

        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            Boolean loginSuccessful = dbHelper.login(username, password);
            if (loginSuccessful) {
                Toast.makeText(requireContext(), "Login Success.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(requireContext(),"Login failed. Please try again.", Toast.LENGTH_LONG).show();
            }
        });

        registerButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            Boolean registerSuccessful = dbHelper.register(username, password);
            if (registerSuccessful) {
                Toast.makeText(requireContext(), "Register Success.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(requireContext(),"Register failed. Please try again.", Toast.LENGTH_LONG).show();
            }
        });

        hint1Button.setOnClickListener(v -> hintTextView.setText(R.string.insecure_provider_hint1));
        hint2Button.setOnClickListener(v -> hintTextView.setText(R.string.insecure_provider_hint2));

        return view;
    }
}