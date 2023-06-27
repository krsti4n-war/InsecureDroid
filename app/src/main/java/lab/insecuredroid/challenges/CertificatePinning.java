package lab.insecuredroid.challenges;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import lab.insecuredroid.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CertificatePinning extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_certificate_pinning, container, false);

        Button requestButton = view.findViewById(R.id.send_request_button);
        Button hint1Button = view.findViewById(R.id.hint1_button);
        Button hint2Button = view.findViewById(R.id.hint2_button);
        Button hint3Button = view.findViewById(R.id.hint3_button);
        TextView hintTextView = view.findViewById(R.id.hint_text_view);

        requestButton.setOnClickListener(v -> sendRequest() );

        hint1Button.setOnClickListener(v -> hintTextView.setText(R.string.certificate_pinning_hint1));
        hint2Button.setOnClickListener(v -> hintTextView.setText(R.string.certificate_pinning_hint2));
        hint3Button.setOnClickListener(v -> hintTextView.setText(R.string.certificate_pinning_hint3));

        return view;
    }

    private void sendRequest() {
        try {
            InputStream inputStream = requireContext().getResources().openRawResource(R.raw.badssl);
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);

            String pin = CertificatePinner.pin(certificate);
            CertificatePinner certificatePinner = new CertificatePinner.Builder()
                    .add("badssl.com", pin)
                    .build();

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("cert", certificate);
            trustManagerFactory.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustManagerFactory.getTrustManagers()[0])
                    .certificatePinner(certificatePinner)
                    .build();

            Request request = new Request.Builder()
                    .url("https://tls-v1-2.badssl.com:1012/")
                    .build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(),"Request Failed : " + e.getMessage(), Toast.LENGTH_LONG).show());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    if (!response.isSuccessful()) {
                        requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(),"Connection error code : " + response.code(), Toast.LENGTH_LONG).show());
                    } else {
                        requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(),"Connection Success", Toast.LENGTH_LONG).show());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}