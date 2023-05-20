package lab.insecuredroid.challenges;

import android.opengl.GLES10;
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

    public Boolean isEmulator() {
        boolean result = false;
        // Check for known emulator device models
        String model = Build.MODEL;
        String manufacturer = Build.MANUFACTURER;
        if (model != null && model.toLowerCase().contains("google_sdk")
                || model.contains("Emulator")
                || model.contains("Android SDK built for x86")
                || manufacturer.contains("Genymotion")
                || (model.contains("Droid4X") && model.contains("x86"))) {
            result = true;
        }
        // Check for known emulator fingerprints
        String fingerprint = Build.FINGERPRINT;
        if (fingerprint != null && (fingerprint.contains("vbox")
                || fingerprint.contains("generic/vbox")
                || fingerprint.contains("generic_x86/vbox")
                || fingerprint.contains("sdk_gphone_x86")
                || fingerprint.contains("google/sdk_gphone_x86")
                || fingerprint.contains("vsemu")
                || fingerprint.contains("virtual")
                || fingerprint.contains("test-keys"))) {
            result = true;
        }
        // Check for known emulator properties
        String property = System.getProperty("ro.kernel.qemu");
        if (property != null && property.equals("1")) {
            result = true;
        }
        // Check for emulator CPU architecture
        String[] supportedAbis = Build.SUPPORTED_ABIS;
        for (String abi : supportedAbis) {
            if (abi.contains("x86") || abi.contains("i686")) {
                result = true;
                break;
            }
        }
        // Check for emulator graphics properties
        String gl_renderer = GLES10.glGetString(GLES10.GL_RENDERER);
        if (gl_renderer != null && gl_renderer.contains("Bluestacks")) {
            result = true;
        }
        // Check for running on a virtual machine
        String vm = Build.HARDWARE;
        if (vm != null && vm.contains("vbox") || vm.contains("qemu") || vm.contains("vmware")) {
            result = true;
        }
        return result;
    }
}