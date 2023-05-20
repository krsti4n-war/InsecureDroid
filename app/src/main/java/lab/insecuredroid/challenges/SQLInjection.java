package lab.insecuredroid.challenges;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class SQLInjection extends Fragment {
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
        View view = inflater.inflate(R.layout.fragment_sql_injection, container, false);

        EditText usernameInput = view.findViewById(R.id.username_input);
        EditText passwordInput = view.findViewById(R.id.password_input);
        Button loginButton = view.findViewById(R.id.login_button);
        Button hintButton = view.findViewById(R.id.hint_button);
        TextView hintTextView = view.findViewById(R.id.hint_text_view);

        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            // Debug the entered credentials...
            Log.d("DEBUG", "username: "+ username + "|" + "password: " + password);

            // Check the login credentials is valid
            Boolean loginSuccessful = dbHelper.vulnerable_login(username, password);
            if (loginSuccessful) {
                Toast.makeText(requireContext(), "Login Success.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(requireActivity(), LoginSuccessfulActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(requireContext(),"Login failed. Please try again.", Toast.LENGTH_LONG).show();
            }
        });

        hintButton.setOnClickListener(v -> hintTextView.setText(R.string.sql_injection_hint));

        return view;
    }
}