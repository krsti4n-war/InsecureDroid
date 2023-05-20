package lab.insecuredroid.challenges;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.scottyab.rootbeer.RootBeer;

import lab.insecuredroid.R;

public class RootDetection extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_root_detection, container, false);

        Button rootDetect = view.findViewById(R.id.detect_root_button);
        Button hint1Button = view.findViewById(R.id.hint1_button);
        Button hint2Button = view.findViewById(R.id.hint2_button);
        TextView hintTextView = view.findViewById(R.id.hint_text_view);

        rootDetect.setOnClickListener(v -> {
            RootBeer rootBeer = new RootBeer(requireActivity());
            if (rootBeer.isRooted()) {
                Toast.makeText(requireContext(), "ROOT is detected!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(requireContext(),"Device not rooted.", Toast.LENGTH_LONG).show();
            }
        });

        hint1Button.setOnClickListener(v -> hintTextView.setText(R.string.root_detection_hint1));
        hint2Button.setOnClickListener(v -> hintTextView.setText(R.string.root_detection_hint2));

        return view;
    }
}