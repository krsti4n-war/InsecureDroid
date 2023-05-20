package lab.insecuredroid.challenges;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import lab.insecuredroid.R;

public class VulnerableWebView extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vulnerable_web_view, container, false);

        EditText URLInput = view.findViewById(R.id.url_input);
        Button loadURLButton = view.findViewById(R.id.load_url_button);
        Button hint1Button = view.findViewById(R.id.hint1_button);
        Button hint2Button = view.findViewById(R.id.hint2_button);
        TextView hintTextView = view.findViewById(R.id.hint_text_view);

        loadURLButton.setOnClickListener(v -> {
            String url = URLInput.getText().toString();
            Intent intent = new Intent(requireActivity(), WebViewActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
        });

        hint1Button.setOnClickListener(v -> hintTextView.setText(R.string.vulnerable_web_view_hint1));
        hint2Button.setOnClickListener(v -> hintTextView.setText(R.string.vulnerable_web_view_hint2));

        return view;
    }
}