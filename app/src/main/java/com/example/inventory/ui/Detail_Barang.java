package com.example.inventory.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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

import static com.example.inventory.helper.Constans.TAG_HARGA_BARANG;
import static com.example.inventory.helper.Constans.TAG_JENIS_BARANG;
import static com.example.inventory.helper.Constans.TAG_JUMLAH_BARANG;
import static com.example.inventory.helper.Constans.TAG_NAMA_BARANG;
import static com.example.inventory.helper.Constans.TAG_QR_BARANG;
import static com.example.inventory.helper.Constans.TAG_SUCCESS;
import static com.example.inventory.helper.Constans.TAG_TANGGAL_BARANG;
import static com.example.inventory.helper.Constans.UPDATE_BARANG;
import static com.example.inventory.helper.Constans.success;
import static com.example.inventory.helper.Constans.tag_json_obj;

public class Detail_Barang extends AppCompatActivity implements ExampleDialog.ExampleDialogListener{
    EditText namaBarang, jenisBarang, Jumlah, Harga;
    TextView Barcode, Tanggal;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    String id, namaBarangs, jenisBarangs, tanggals, barcodes, jumlahBarangs, hargas;
    Button Kembali, Update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_barang);

        Intent data = getIntent();
        id           = getIntent().getStringExtra("id");

        namaBarang      = findViewById(R.id.nama_barang);
        jenisBarang     = findViewById(R.id.jenis_barang);
        Tanggal         = findViewById(R.id.tanggal);
        Barcode         = findViewById(R.id.barcode);
        Jumlah          = findViewById(R.id.jumlah_barang);
        Harga           = findViewById(R.id.harga_barang);
        Kembali         = findViewById(R.id.kembali);
        namaBarangs     = data.getStringExtra(TAG_NAMA_BARANG);
        jenisBarangs    = data.getStringExtra(TAG_JENIS_BARANG);
        tanggals        = data.getStringExtra(TAG_TANGGAL_BARANG);
        barcodes        = data.getStringExtra(TAG_QR_BARANG);
        jumlahBarangs   = data.getStringExtra(TAG_JUMLAH_BARANG);
        hargas          = data.getStringExtra(TAG_HARGA_BARANG);
        Update          = findViewById(R.id.update);

        namaBarang.setText(namaBarangs);
        jenisBarang.setText(jenisBarangs);
        Tanggal.setText(tanggals);
        Barcode.setText(barcodes);
        Jumlah.setText(jumlahBarangs);
        Harga.setText(hargas);

        myCalendar = Calendar.getInstance();
        date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        Tanggal.setOnClickListener(view -> new DatePickerDialog(Detail_Barang.this,
                date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        Barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }

            public void openDialog() {
                ExampleDialog exampleDialog = new ExampleDialog();
                exampleDialog.show(getSupportFragmentManager(), "example dialog");
            }


        });

        Update.setOnClickListener(view -> {
            final String NamaBarang    = namaBarang.getText().toString().trim();
            final String JenisBarang   = jenisBarang.getText().toString().trim();
            final String HargaBarang   = Harga.getText().toString().trim();
            final String Tanggalnya    = Tanggal.getText().toString().trim();
            final String BarcodeBarang = Barcode.getText().toString().trim();
            final String JumlahBrg     = Jumlah.getText().toString().trim();
            StringRequest strReq       = new StringRequest(Request.Method.POST, UPDATE_BARANG,
                    response -> {
                        Log.e("response", "Response: " + response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                startActivity(new Intent(Detail_Barang.this, Data_Barang.class));
                                Log.e("Successfully Login!", jObj.toString());
                                Toast.makeText(Detail_Barang.this, "Berhasil update!!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Detail_Barang.this, "Gagal Update Data!!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                Log.e("error response", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("nama_barang", NamaBarang);
                    params.put("jenis_barang", JenisBarang);
                    params.put("jumlah_brg", JumlahBrg);
                    params.put("harga_brg", HargaBarang);
                    params.put("barcode", BarcodeBarang);
                    params.put("tanggal", Tanggalnya);
                    params.put("id", id);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
        });
        Kembali.setOnClickListener(view -> startActivity(new Intent(Detail_Barang.this, Data_Barang.class)));
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Tanggal.setText(sdf.format(myCalendar.getTime()));
    }


    @Override
    public void applyTexts(String hasil) {
        Barcode.setText(hasil);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Data_Barang.class));
        finish();
    }
}