package com.example.inventory.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.budiyev.android.codescanner.CodeScanner;
import com.example.inventory.R;
import com.example.inventory.adapter.Adapter;
import com.example.inventory.helper.AppController;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.inventory.helper.Constans.SHARED_PREFS;
import static com.example.inventory.helper.Constans.TAG_HARGA_BARANG;
import static com.example.inventory.helper.Constans.TAG_ID;
import static com.example.inventory.helper.Constans.TAG_JENIS_BARANG;
import static com.example.inventory.helper.Constans.TAG_JUMLAH_BARANG;
import static com.example.inventory.helper.Constans.TAG_MESSAGE;
import static com.example.inventory.helper.Constans.TAG_NAMA_BARANG;
import static com.example.inventory.helper.Constans.TAG_SCAN;
import static com.example.inventory.helper.Constans.TAG_SUCCESS;
import static com.example.inventory.helper.Constans.TAMPIL_BARANG;
import static com.example.inventory.helper.Constans.TEXT;
import static com.example.inventory.helper.Constans.UPDATE_BARANG;
import static com.example.inventory.helper.Constans.success;
import static com.example.inventory.helper.Constans.tag_json_obj;

public class Edit_Barang extends AppCompatActivity {
    ProgressDialog progressDialog;
    Adapter adapter;
    private CodeScanner mCodeScanner;
    AlertDialog.Builder dialog;
    String scan, id, idBaru;
    LayoutInflater inflater;
    View dialogView;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    TextView Qr, Tanggal;
    EditText idx;
    Button Update;
    TextInputEditText namaBarang, jenisBarang, hargaBarang, jumlahbarang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_barang);
        id           = getIntent().getStringExtra(TAG_ID);
        namaBarang   = findViewById(R.id.nama_barang);
        jenisBarang  = findViewById(R.id.jenis_barang);
        hargaBarang  = findViewById(R.id.harga_barang);
        jumlahbarang = findViewById(R.id.jumlah_barang);
        Tanggal      = findViewById(R.id.tanggal);
        Qr           = findViewById(R.id.qr);
        Update       = findViewById(R.id.simpan);
        idx          = findViewById(R.id.idr);
        loadsave();
        Update.setOnClickListener(view -> {
            String updateNamaBarang     = namaBarang.getText().toString().trim();
            String updateJenisBarang    = jenisBarang.getText().toString().trim();
            String updateHargaBarang    = hargaBarang.getText().toString().trim();
            String updateJumlahBarang   = jumlahbarang.getText().toString().trim();
            String updateTanggalBarang  = Tanggal.getText().toString().trim();
            String updateQrBarang       = Qr.getText().toString().trim();

            StringRequest strReq        = new StringRequest(Request.Method.POST, UPDATE_BARANG, response -> {
                try {
                    JSONObject jObj = new JSONObject(response);
                    success         = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        startActivity(new Intent(this, Data_Barang.class));
                    } else {
                        Toast.makeText(this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }, error -> Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show()) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", id);
                    Log.d("idnya", "id: " + id);
                    params.put("nama_barang", updateNamaBarang);
                    params.put("jenis_barang", updateJenisBarang);
                    params.put("harga_brg", updateHargaBarang);
                    params.put("jumlah_brg", updateJumlahBarang);
                    params.put("tanggal", updateTanggalBarang);
                    params.put("barcode", updateQrBarang);

                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
        });

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        final Calendar myCalendar = Calendar.getInstance();

        scan    = getIntent().getStringExtra(TAG_SCAN);
        tampil();

        Qr.setText(scan);

        Qr.setOnClickListener(view->{
//            Intent intent = new Intent(this, SimpleScannerActivity.class);
//            String idc = idx.getText().toString().trim();
//            intent.putExtra("id", idc);
//            finish();
//            startActivity(intent);
//                    startActivity(new Intent(this, SimpleScannerActivity.class));
//            DialogForm();
            startActivity(new Intent(this, SimpleScannerActivity.class));
                }
        );

        Tanggal.setOnClickListener(view->{
            int mYear   = myCalendar.get(Calendar.YEAR);
            int mMonth  = myCalendar.get(Calendar.MONTH);
            int mDay    = myCalendar.get(Calendar.DAY_OF_MONTH);

            showCalendar();
        });

    }

    private void loadsave() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        idBaru = sharedPreferences.getString(TEXT, "");
        idx.setText(idBaru);
    }

    private void DialogForm() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");

//        dialog = new AlertDialog.Builder(this);
//        inflater = getLayoutInflater();
//        dialogView = inflater.inflate(R.layout.activity_scan, null);
//        dialog.setView(dialogView);
//        dialog.setCancelable(true);
//
//        CodeScannerView scannerView = findViewById(R.id.scanner_view);
//        mCodeScanner = new CodeScanner(this, scannerView);
//        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
//            String hasilScan = result.getText();
//            Intent intent = new Intent(this,Tambah_Barang.class);
//            intent.putExtra(TAG_HASIL_SCAN, hasilScan);
//            finish();
//            startActivity(intent);
//            Toast.makeText(this, "hasilnya"+ result, Toast.LENGTH_SHORT).show();
//        }));
//        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
//
//        dialog.setNegativeButton("Batal",
//                (dialog, which) -> dialog.dismiss());
//        dialog.show();

    }

    private void showCalendar() {
        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {

            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);

            Tanggal.setText(dateFormatter.format(newDate.getTime()));
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void tampil() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sebentar ...");
        showDialog();

        JsonArrayRequest jArr = new JsonArrayRequest(TAMPIL_BARANG, response -> {
            for (int i = 0; i < response.length(); i++) {
                hideDialog();
                try {
                    JSONObject jObj = response.getJSONObject(i);

                        String idx      = jObj.getString(TAG_ID);
                        String namax    = jObj.getString(TAG_NAMA_BARANG);
                        String hargax   = jObj.getString(TAG_HARGA_BARANG);
                        String jenisx   = jObj.getString(TAG_JENIS_BARANG);
                        String jumlahx  = jObj.getString(TAG_JUMLAH_BARANG);
                        String barcode  = jObj.getString(TAG_JENIS_BARANG);
                        String tanggal  = jObj.getString(TAG_JUMLAH_BARANG);

                        namaBarang.setText(namax);
                        hargaBarang.setText(hargax);
                        jenisBarang.setText(jenisx);
                        jumlahbarang.setText(jumlahx);
                } catch (JSONException e) {
                    hideDialog();
                    e.printStackTrace();
                }
            }
        }, error -> {
            hideDialog();
            Toast.makeText(Edit_Barang.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
        })

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(jArr, tag_json_obj);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Data_Barang.class));
    }

    }
