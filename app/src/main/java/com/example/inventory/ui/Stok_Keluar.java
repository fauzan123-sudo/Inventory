package com.example.inventory.ui;

import androidx.appcompat.app.AppCompatActivity;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.inventory.R;
import com.example.inventory.helper.AppController;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


import static com.example.inventory.helper.Constans.SIMPAN_STOK_KELUAR;
import static com.example.inventory.helper.Constans.TAG_HARGA_BARANG;
import static com.example.inventory.helper.Constans.TAG_HASIL_SCAN;
import static com.example.inventory.helper.Constans.TAG_ID;
import static com.example.inventory.helper.Constans.TAG_JENIS_BARANG;
import static com.example.inventory.helper.Constans.TAG_JUMLAH_BARANG;
import static com.example.inventory.helper.Constans.TAG_MESSAGE;
import static com.example.inventory.helper.Constans.TAG_NAMA_BARANG;
import static com.example.inventory.helper.Constans.TAG_NAMA_PELANGGAN;
import static com.example.inventory.helper.Constans.TAG_SUCCESS;
import static com.example.inventory.helper.Constans.TAG_TANGGAL_BARANG;
import static com.example.inventory.helper.Constans.TAMBAH_BARANG;
import static com.example.inventory.helper.Constans.scanQrBarang;
import static com.example.inventory.helper.Constans.success;
import static com.example.inventory.helper.Constans.tag_json_obj;

public class Stok_Keluar extends AppCompatActivity{
    String id,nama_barang, hargaBarang, jenisBarang, jumlahBarang, tanggalBarang, barcode;
    ProgressDialog progressDialog;
    TextView Barcode, TanggalBarang;
    EditText NamaPelanggan, NoTpl,  NamaBarang, HargaBarang, JenisBarang, JumlahBarang;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    Button Kembali, Simpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stok_keluar);

        Intent data = getIntent();
        barcode = data.getStringExtra(TAG_HASIL_SCAN);

        NamaPelanggan   = findViewById(R.id.nama_pelanggan);
        NoTpl           = findViewById(R.id.no_tlp);
        NamaBarang      = findViewById(R.id.nama_barang);
        HargaBarang     = findViewById(R.id.harga_barang);
        JenisBarang     = findViewById(R.id.jenis_barang);
        JumlahBarang    = findViewById(R.id.jumlah_barang);
        TanggalBarang   = findViewById(R.id.tanggal);
        Barcode         = findViewById(R.id.barcode);
        Kembali         = findViewById(R.id.kembali);
        Simpan          = findViewById(R.id.simpan);

        myCalendar = Calendar.getInstance();
        date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        Kembali.setOnClickListener(view -> startActivity(new Intent(Stok_Keluar.this, Home.class)));
        Simpan.setOnClickListener(view -> {
            SimpanStokKeluar();
        });

        TanggalBarang.setOnClickListener(view -> new DatePickerDialog(Stok_Keluar.this,
                date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        PindaiBarang();

    }

    private void SimpanStokKeluar() {
        String NamaPelanggans = NamaPelanggan.getText().toString().trim();
        String NamaBarangs    = NamaBarang.getText().toString().trim();
        String HargaBarangs   = HargaBarang.getText().toString().trim();
        String JenisBarangs   = JenisBarang.getText().toString().trim();
        String JumlahBarangs  = JumlahBarang.getText().toString().trim();
        String Barcodes       = Barcode.getText().toString().trim();
        String TanggalBarangs = TanggalBarang.getText().toString().trim();
        String NoTlps         = NoTpl.getText().toString().trim();

        StringRequest strReq = new StringRequest(Request.Method.POST, SIMPAN_STOK_KELUAR,
                response -> {
                    Log.d("response", "Response: " + response);
                    try {
                        JSONObject jObj = new JSONObject(response);
                        success = jObj.getInt("success");
                        if (success == 1) {
                            Toast.makeText(Stok_Keluar.this, "berhasil!!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Stok_Keluar.this, Data_Barang.class));

                        } else {
                            Toast.makeText(Stok_Keluar.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
            Log.e("error", "Error: " + error.getMessage());
            Toast.makeText(Stok_Keluar.this, error.getMessage(), Toast.LENGTH_LONG).show();
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("nama_pelanggan", NamaPelanggans);
                params.put("nama_barang", NamaBarangs);
                params.put("jenis_barang", JenisBarangs);
                params.put("jumlah_brg", JumlahBarangs);
                params.put("harga_brg", HargaBarangs);
                params.put("barcode", Barcodes);
                params.put("tanggal", TanggalBarangs);
                params.put("id", id);
                params.put("no_tlp", NoTlps);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        TanggalBarang.setText(sdf.format(myCalendar.getTime()));
    }

    private void PindaiBarang() {
        String Barcodenya = barcode;

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, scanQrBarang,
                response -> {
                    Log.e("response", "Login Response: " + response);
                    hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);
                        success         = jObj.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            nama_barang     = jObj.getString(TAG_NAMA_BARANG);
                            hargaBarang     = jObj.getString(TAG_HARGA_BARANG);
                            jenisBarang     = jObj.getString(TAG_JENIS_BARANG);
                            jumlahBarang    = jObj.getString(TAG_JUMLAH_BARANG);
                            tanggalBarang   = jObj.getString(TAG_TANGGAL_BARANG);
                            id              = jObj.getString(TAG_ID);

                            NamaBarang.setText(nama_barang);
                            HargaBarang.setText(hargaBarang);
                            JenisBarang.setText(jenisBarang);
                            JumlahBarang.setText(jumlahBarang);
                            TanggalBarang.setText(tanggalBarang);
                            Barcode.setText(barcode);

                            Log.e("Successfully Login!", jObj.toString());

                        } else {
                            Toast.makeText(this, "hasil scan tidak ada", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Stok_Keluar.this, GagalScan.class));

                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "disini"+ e, Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }, error -> {
            Log.e("error", "Login Error: " + error.getMessage());
            Toast.makeText(getApplicationContext(),
                    error.getMessage(), Toast.LENGTH_LONG).show();

            hideDialog();

        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("barcode", barcode);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}