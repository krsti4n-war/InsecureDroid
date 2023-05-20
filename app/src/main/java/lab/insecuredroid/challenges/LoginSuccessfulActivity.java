package lab.insecuredroid.challenges;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import lab.insecuredroid.R;

public class LoginSuccessfulActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_successfull);

        Button logout = findViewById(R.id.logout_button);
        logout.setOnClickListener(v -> finish());
    }
}