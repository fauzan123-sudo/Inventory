package com.example.inventory.ui;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.example.inventory.R;

public class Scan extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    public static final String TAG_HASIL_SCAN = "hasilScan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            String hasilScan = result.getText();
            Intent intent = new Intent(Scan.this,Stok_Keluar.class);
            intent.putExtra(TAG_HASIL_SCAN, hasilScan);
            finish();
            startActivity(intent);
//            Toast.makeText(Scan.this, ""+ result, Toast.LENGTH_SHORT).show();
        }));
        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
    }

    @Override
    protected void onResume() {
        super.onResume();
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(Scan.this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(Scan.this, new String[]{Manifest.permission.CAMERA}, 123);
            } else {

                mCodeScanner.startPreview();
            }
        }
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

}