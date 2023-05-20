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

import androidx.fragment.app.Fragment;

import lab.insecuredroid.R;

public class WeakCryptography extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weak_cryptography, container, false);

        EditText secretInput = view.findViewById(R.id.secret_input);
        Button saveButton = view.findViewById(R.id.save_button);
        Button hint1Button = view.findViewById(R.id.hint1_button);
        Button hint2Button = view.findViewById(R.id.hint2_button);
        TextView hintTextView = view.findViewById(R.id.hint_text_view);

        saveButton.setOnClickListener(v -> {
            String secret = secretInput.getText().toString();
            String encryptedSecret = encrypt(secret);

            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("secrets", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("secret", encryptedSecret);
            editor.apply();
            Toast.makeText(requireContext(),"Your secret already stored!", Toast.LENGTH_LONG).show();
        });

        hint1Button.setOnClickListener(v -> hintTextView.setText(R.string.weak_cryptography_hint1));
        hint2Button.setOnClickListener(v -> hintTextView.setText(R.string.weak_cryptography_hint2));

        return view;
    }
}