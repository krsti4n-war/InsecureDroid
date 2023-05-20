package lab.insecuredroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import lab.insecuredroid.challenges.ChangePasswordActivity;
import lab.insecuredroid.challenges.WebViewActivity;

public class DeeplinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deeplink);

        Uri uri = getIntent().getData();
        if (uri != null) {
            if (Objects.equals(uri.getScheme(), "lab") && Objects.equals(uri.getHost(), "insecuredroid")) {
                String path = uri.getPath();
                switch (path) {
                    case "/activation": {
                        String key = uri.getQueryParameter("key");
                        if (key != null && Objects.equals(key, getResources().getString(R.string.activation_key))) {
                            Toast.makeText(getApplicationContext(),"Application active, enjoy your premium access", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(),"Invalid key", Toast.LENGTH_LONG).show();
                        }
                        break;
                    }
                    case "/web":
                        String url = uri.getQueryParameter("url");
                        if (url != null) {
                            Intent intent = new Intent(this, WebViewActivity.class);
                            intent.putExtra("url", url);
                            startActivity(intent);
                        }
                        break;
                    case "/webview":
                        String web_url = uri.getQueryParameter("url");
                        if (web_url != null) {
                            String host = Uri.parse(web_url).getHost();
                            if (host != null && host.endsWith("example.com")) {
                                Intent intent = new Intent(this, WebViewActivity.class);
                                intent.putExtra("url", web_url);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "invalid URL", Toast.LENGTH_LONG).show();
                            }
                        }
                        break;
                    case "/change-password": {
                        String username = uri.getQueryParameter("username");
                        if (username != null) {
                            Intent intent = new Intent(this, ChangePasswordActivity.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                        }
                        break;
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(),"Malicious Activity Detected...", Toast.LENGTH_LONG).show();
            }
        }
        finish();
    }
}