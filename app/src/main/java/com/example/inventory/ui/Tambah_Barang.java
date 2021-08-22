package com.example.inventory.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.inventory.R;
import com.example.inventory.helper.AppController;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.example.inventory.helper.Constans.TAG_MESSAGE;
import static com.example.inventory.helper.Constans.TAMBAH_BARANG;
import static com.example.inventory.helper.Constans.success;
import static com.example.inventory.helper.Constans.tag_json_obj;

public class Tambah_Barang extends AppCompatActivity {

    TextInputEditText NamaBarang, JenisBarang, JumlahBarang, HargaBarang;
    public  EditText Tanggal;
    ImageView QR, ImgTanggal;
    public static TextView QRCode;
    String hasilScan;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    Button SImpan;
    public static final String TAG_HASIL_SCAN = "hasilScan";
    private final String TAG = Tambah_Barang.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_barang);

        NamaBarang      = findViewById(R.id.nama_barang);
        HargaBarang     = findViewById(R.id.harga_barang);
        JenisBarang     = findViewById(R.id.jenis_barang);
        JumlahBarang    = findViewById(R.id.jumlah_barang);
        QRCode          = findViewById(R.id.qr);
        QR              = findViewById(R.id.img_qr);
        Tanggal         = findViewById(R.id.tanggal);
        ImgTanggal      = findViewById(R.id.img_tanggal);
        SImpan          = findViewById(R.id.simpan);

        myCalendar = Calendar.getInstance();
        date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        ImgTanggal.setOnClickListener(View-> new DatePickerDialog(Tambah_Barang.this,
                date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        hasilScan = getIntent().getStringExtra(TAG_HASIL_SCAN);
        QRCode.setText(hasilScan);

        QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanFragment exampleDialog = new ScanFragment();
                exampleDialog.show(getSupportFragmentManager(), "example dialog");
            }
        });
        SImpan.setOnClickListener(View->{
            String namaBarangs      = Objects.requireNonNull(NamaBarang.getText()).toString().trim();
            String hargaBarangs     = Objects.requireNonNull(HargaBarang.getText()).toString().trim();
            String jenisBarangs     = Objects.requireNonNull(JenisBarang.getText()).toString().trim();
            String  jumlahBarangs   = Objects.requireNonNull(JumlahBarang.getText()).toString().trim();
            String QRBarangs        = QRCode.getText().toString().trim();
            String tanggalBarangs   = Tanggal.getText().toString().trim();
            TambahData(namaBarangs, hargaBarangs, jenisBarangs, jumlahBarangs, QRBarangs, tanggalBarangs);

        });

    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Tanggal.setText(sdf.format(myCalendar.getTime()));
    }

    private void TambahData(String namaBarangs, String hargaBarangs, String jenisBarangs,
                            String jumlahBarangs, String QRBarangs, String tanggalBarangs) {
        StringRequest strReq = new StringRequest(Request.Method.POST, TAMBAH_BARANG,
                response -> {
                    Log.d(TAG, "Response: " + response);

                    try {
                        JSONObject jObj = new JSONObject(response);
                        success = jObj.getInt("success");

                        if (success == 1) {

                            Toast.makeText(Tambah_Barang.this, "berhasil!!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Tambah_Barang.this, Data_Barang.class));

                        } else {
                            Toast.makeText(Tambah_Barang.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
            Log.e(TAG, "Error: " + error.getMessage());
            Toast.makeText(Tambah_Barang.this, error.getMessage(), Toast.LENGTH_LONG).show();
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("nama_barang", namaBarangs);
                params.put("jenis_barang", jenisBarangs);
                params.put("jumlah_brg", jumlahBarangs);
                params.put("harga_brg", hargaBarangs);
                params.put("barcode", QRBarangs);
                params.put("tanggal", tanggalBarangs);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }


}