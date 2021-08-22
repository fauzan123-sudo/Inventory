package com.example.inventory.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.inventory.R;
import com.google.zxing.Result;

public class ExampleDialog extends AppCompatDialogFragment {
    private CodeScanner mCodeScanner;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_scan, null);
        CodeScannerView scannerView = view.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(requireActivity(), scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                String hasil = result.getText().trim();
                Toast.makeText(requireActivity(), "Hasilnya :" + hasil, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setView(view)
                .setTitle("Login")
                .setNegativeButton("cancel", (dialogInterface, i) -> {
                });
        scannerView.setOnClickListener(view1 -> mCodeScanner.startPreview());
//        CodeScannerView scannerView = view1.findViewById(R.id.scanner_view);
//        mCodeScanner                = new CodeScanner(requireActivity(), scannerView);
//        mCodeScanner.setDecodeCallback(new DecodeCallback() {
//            @Override
//            public void onDecoded(@NonNull final Result result) {
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(activity, result.getText(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(requireActivity()
                        , new String[]{Manifest.permission.CAMERA}, 123);
            } else {
                mCodeScanner.startPreview();
            }
            }
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}
