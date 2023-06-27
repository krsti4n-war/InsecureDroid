package lab.insecuredroid.challenges;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

        Button hintButton = view.findViewById(R.id.hint_button);
        TextView hintTextView = view.findViewById(R.id.hint_text_view);

        hintButton.setOnClickListener(v -> hintTextView.setText(R.string.insecure_content_provider_hint));

        return view;
    }
}