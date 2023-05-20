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

import androidx.fragment.app.Fragment;

import lab.insecuredroid.R;

public class NativeLibrary extends Fragment {

    static {
        System.loadLibrary("native_lib");
    }
    public static native Boolean checkPass(Context context, String text);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_native_library, container, false);

        EditText passwordInput = view.findViewById(R.id.password_input);
        Button submitButton = view.findViewById(R.id.submit_button);
        Button hint1Button = view.findViewById(R.id.hint1_button);
        Button hint2Button = view.findViewById(R.id.hint2_button);
        TextView hintTextView = view.findViewById(R.id.hint_text_view);

        submitButton.setOnClickListener(v -> {
            String password = passwordInput.getText().toString();
            if (password.isEmpty()) {
                Toast.makeText(requireContext(),"Please enter the password!", Toast.LENGTH_SHORT).show();
                return;
            }

            Boolean result = checkPass(requireContext(), password);
            if (result != null && result) {
                Toast.makeText(requireContext(), "Congrats you crack the password!", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(requireContext(), "Password doesn't match", Toast.LENGTH_LONG).show();
            }
        });

        hint1Button.setOnClickListener(v -> hintTextView.setText(R.string.weak_cryptography_hint1));
        hint2Button.setOnClickListener(v -> hintTextView.setText(R.string.weak_cryptography_hint2));

        return view;
    }
}