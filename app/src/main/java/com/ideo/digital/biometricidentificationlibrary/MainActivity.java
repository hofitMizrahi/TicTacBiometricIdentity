package com.ideo.digital.biometricidentificationlibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.ideo.digital.biometricidentificationlib.BiometricLogin;
import com.ideo.digital.biometricidentificationlib.IBiometricLoginResult;

public class MainActivity extends AppCompatActivity implements IBiometricLoginResult {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.startAuthBiometric);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBiometric();
            }
        });
    }

    private void startBiometric() {

        BiometricLogin b = new BiometricLogin.Builder(this)
                .setDescription("description")
                .setSubtitle("subbbb title")
                .setConfirmationRequired(false)
                .setCancel("close")
                .setTitle("jdiojdijdoi")
                .build();

        b.authenticate(this);
    }

    @Override
    public void biometricResultStatus(boolean isSuccess, String status) {
        Log.i("HofitTestLogin", "status = " + "biometricResultStatus() = " + status);
    }
}