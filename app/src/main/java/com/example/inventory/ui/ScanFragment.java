package com.example.inventory.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.example.inventory.R;


public class ScanFragment extends AppCompatDialogFragment {
    private CodeScanner mCodeScanner;
    private ExampleDialog.ExampleDialogListener listener;
    private String Hasil;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity activity     = requireActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater     = requireActivity().getLayoutInflater();
        View view                   = inflater.inflate(R.layout.activity_scan, null);
        CodeScannerView scannerView = view.findViewById(R.id.scanner_view);
        mCodeScanner                = new CodeScanner(activity, scannerView);
        mCodeScanner.setDecodeCallback(result -> activity.runOnUiThread(() ->
                Hasil = result.getText()));

        scannerView.setOnClickListener(view1 -> mCodeScanner.startPreview());
        builder.setView(view)
                .setTitle("Scan")
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    listener.applyTexts(Hasil);
                })
                .setNegativeButton("Batal", (dialogInterface, i) -> {
                });

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.CAMERA}, 123);
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ExampleDialog.ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }
    public interface ExampleDialogListener {
        void applyTexts(String hasil);
    }
}
