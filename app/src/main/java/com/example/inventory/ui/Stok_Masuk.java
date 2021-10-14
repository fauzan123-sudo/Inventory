package com.example.inventory.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.inventory.R;
import com.example.inventory.helper.AppController;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.inventory.helper.Constans.STOK_MASUK;
import static com.example.inventory.helper.Constans.TAG_HARGA_BARANG;
import static com.example.inventory.helper.Constans.TAG_ID;
import static com.example.inventory.helper.Constans.TAG_JENIS_BARANG;
import static com.example.inventory.helper.Constans.TAG_JUMLAH_BARANG;
import static com.example.inventory.helper.Constans.TAG_MESSAGE;
import static com.example.inventory.helper.Constans.TAG_NAMA_BARANG;
import static com.example.inventory.helper.Constans.TAG_QR_BARANG;
import static com.example.inventory.helper.Constans.success;
import static com.example.inventory.helper.Constans.tag_json_obj;

public class Stok_Masuk extends AppCompatActivity{
    Button SImpan;
    EditText jumlahBarang;
    String id, NamaBarangs, JumlahBarangs, JenisBarangs, HargaBarangs, QRs;
    private final String TAG = Stok_Masuk.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stok_masuk);

        SImpan          = findViewById(R.id.simpan);

        id            = getIntent().getStringExtra(TAG_ID);
        NamaBarangs   = getIntent().getStringExtra(TAG_NAMA_BARANG);
        JumlahBarangs = getIntent().getStringExtra(TAG_JUMLAH_BARANG);
        JenisBarangs  = getIntent().getStringExtra(TAG_JENIS_BARANG);
        HargaBarangs  = getIntent().getStringExtra(TAG_HARGA_BARANG);
        QRs           = getIntent().getStringExtra(TAG_QR_BARANG);

        jumlahBarang = findViewById(R.id.jumlah_barang);
        jumlahBarang.setText(JumlahBarangs);
//        plus = findViewById(R.id.plus);
//        minus = findViewById(R.id.minus);
//
//        plus.setOnClickListener(view ->{
//
//            String mynum1=jumlahBarang.getText().toString();
//            int mynum2= 1 ;
//            int sum = Integer.parseInt(mynum1) + mynum2;
//
//            jumlahBarang.setText(String.valueOf(sum));
//        });


        SImpan.setOnClickListener(View->{
            String jumlahBarangs    = Objects.requireNonNull(jumlahBarang.getText()).toString().trim();
            TambahData(jumlahBarangs);
        });
    }

    private void TambahData(
                            String jumlahBarangs) {
        StringRequest strReq = new StringRequest(Request.Method.POST, STOK_MASUK,
                response -> {
                    Log.d(TAG, "Response: " + response);
                    try {
                        JSONObject jObj = new JSONObject(response);
                        success = jObj.getInt("success");
                        if (success == 1) {
                            Toast.makeText(Stok_Masuk.this, "berhasil!!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Stok_Masuk.this, List_Barang.class));

                        } else {
                            Toast.makeText(Stok_Masuk.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
            Log.e(TAG, "Error: " + error.getMessage());
            Toast.makeText(Stok_Masuk.this, error.getMessage(), Toast.LENGTH_LONG).show();
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("id", id);
                params.put(TAG_NAMA_BARANG, NamaBarangs);
                params.put(TAG_JENIS_BARANG, JenisBarangs);
                params.put(TAG_JUMLAH_BARANG, jumlahBarangs);
                params.put(TAG_HARGA_BARANG, HargaBarangs);
                params.put(TAG_QR_BARANG, QRs);
//                params.put("tanggal", tanggalBarangs);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, List_Barang.class));
        finish();


    }
}