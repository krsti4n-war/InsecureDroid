package lab.insecuredroid.challenges;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import lab.insecuredroid.R;

public class EmulatorDetection extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_emulator_detection, container, false);

        Button emulatorDetect = view.findViewById(R.id.detect_emu_button);
        Button hintButton = view.findViewById(R.id.hint_button);
        TextView hintTextView = view.findViewById(R.id.hint_text_view);

        emulatorDetect.setOnClickListener(v -> {
            if (isEmulator()) {
                Toast.makeText(requireContext(),"Emulator detected!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(requireContext(),"Your device isn't emulator.", Toast.LENGTH_LONG).show();
            }
        });

        hintButton.setOnClickListener(v -> hintTextView.setText(R.string.emulator_detection_hint));

        return view;
    }

    public boolean isEmulator() {
        String buildDetails = (Build.FINGERPRINT + Build.DEVICE + Build.MODEL + Build.BRAND +
                Build.PRODUCT + Build.MANUFACTURER + Build.HARDWARE).toLowerCase();

        return (buildDetails.contains("generic")
                || buildDetails.contains("unknown")
                || buildDetails.contains("emulator")
                || buildDetails.contains("sdk")
                || buildDetails.contains("vbox")
                || buildDetails.contains("genymotion")
                || buildDetails.contains("x86")
                || buildDetails.contains("goldfish")
                || buildDetails.contains("test-keys"));
    }
}