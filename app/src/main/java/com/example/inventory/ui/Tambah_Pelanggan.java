package com.example.inventory.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.inventory.R;
import com.example.inventory.helper.AppController;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.inventory.helper.Constans.NAMA_PELANGGAN;
import static com.example.inventory.helper.Constans.NO_TLP;
import static com.example.inventory.helper.Constans.TAG_HARGA_BARANG;
import static com.example.inventory.helper.Constans.TAG_ID;
import static com.example.inventory.helper.Constans.TAG_JENIS_BARANG;
import static com.example.inventory.helper.Constans.TAG_JUMLAH_BARANG;
import static com.example.inventory.helper.Constans.TAG_NAMA_BARANG;
import static com.example.inventory.helper.Constans.TAG_PELANGGAN;
import static com.example.inventory.helper.Constans.TAG_SUCCESS;
import static com.example.inventory.helper.Constans.TAG_TANGGAL_BARANG;
import static com.example.inventory.helper.Constans.TAMBAH_PELANGGAN;
import static com.example.inventory.helper.Constans.UPDATE_DATA_PELANGGAN;

public class Tambah_Pelanggan extends AppCompatActivity {
    String id;
    ProgressDialog pd;
    TextInputEditText NamaPelanggan, NamaBarang, Tanggal, NoTlp, Harga, Jenis, Jumlah;
    Button Simpan, Batal;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pelanggan);

        Intent data = getIntent();
        final int update        = data.getIntExtra("update",0);
        String namaPelanggan    = data.getStringExtra(NAMA_PELANGGAN);
        String namaBarang       = data.getStringExtra(TAG_NAMA_BARANG);
        String tanggal          = data.getStringExtra(TAG_TANGGAL_BARANG);
        String noTlp            = data.getStringExtra(NO_TLP);
        String harga            = data.getStringExtra(TAG_HARGA_BARANG);
        String jenis            = data.getStringExtra(TAG_JENIS_BARANG);
        String jumlah           = data.getStringExtra(TAG_JUMLAH_BARANG);
        id                      = data.getStringExtra(TAG_ID);

        NamaPelanggan   = findViewById(R.id.nama_pelanggan);
        NamaBarang      = findViewById(R.id.nama_barang);
        Tanggal         = findViewById(R.id.tanggal);
        NoTlp           = findViewById(R.id.no_tlp);
        Harga           = findViewById(R.id.harga);
        Jenis           = findViewById(R.id.jenis_barang);
        Jumlah          = findViewById(R.id.jumlah_barang);
        Simpan          = findViewById(R.id.simpan);
        Batal           = findViewById(R.id.batal);
        pd = new ProgressDialog(this);

        Batal.setOnClickListener(view->{
            startActivity(new Intent(this, Pelanggan.class));
        });

        if (update == 1){
            Simpan.setText("Update");
            NamaPelanggan.setText(namaPelanggan);
            NamaBarang.setText(namaBarang);
            Tanggal.setText(tanggal);
            NoTlp.setText(noTlp);
            Harga.setText(harga);
            Jenis.setText(jenis);
            Jumlah.setText(jumlah);
        }


        Simpan.setOnClickListener(view->{
            if(update == 1)
            {
                Update_data();
            }else {
                simpanData();
            }
        });
    }

    private void Update_data() {
        pd.setCancelable(false);
        pd.setMessage("Sebentar ...");
        showDialog();

        StringRequest sendData = new StringRequest(Request.Method.POST, UPDATE_DATA_PELANGGAN,
                response -> {
                    hideDialog();
                    try {
                        JSONObject res = new JSONObject(response);
                        int success = res.getInt(TAG_SUCCESS);
                        if (success == 1){
                            startActivity( new Intent(this,Pelanggan.class));
                        }else{
                            Toast.makeText(this, "ada error . . ", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                },
                (Response.ErrorListener) error -> {
                    hideDialog();
                    Toast.makeText(this, "pesan : Gagal Insert Data", Toast.LENGTH_SHORT).show();
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> map = new HashMap<>();
                map.put(NAMA_PELANGGAN,     NamaPelanggan.getText().toString().trim());
                map.put(TAG_NAMA_BARANG,    NamaBarang.getText().toString().trim());
                map.put(TAG_TANGGAL_BARANG, Tanggal.getText().toString().trim());
                map.put(NO_TLP,             NoTlp.getText().toString().trim());
                map.put(TAG_HARGA_BARANG,   Harga.getText().toString().trim());
                map.put(TAG_JENIS_BARANG,   Jenis.getText().toString().trim());
                map.put(TAG_JUMLAH_BARANG,  Jumlah.getText().toString().trim());
                map.put("id_pelanggan", id);

                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(sendData);

    }

    private void simpanData(){
        pd.setCancelable(false);
        pd.setMessage("Sebentar ...");
        showDialog();

        StringRequest sendData = new StringRequest(Request.Method.POST, TAMBAH_PELANGGAN,
                response -> {
                    hideDialog();
                    try {
                        JSONObject res = new JSONObject(response);
                        int success = res.getInt(TAG_SUCCESS);
                        if (success == 1){
                            startActivity( new Intent(this,Pelanggan.class));
                        }else{
                            Toast.makeText(this, "ada error . . ", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    
                },
                (Response.ErrorListener) error -> {
                   hideDialog();
                    Toast.makeText(this, "pesan : Gagal Insert Data", Toast.LENGTH_SHORT).show();
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> map = new HashMap<>();
                map.put(NAMA_PELANGGAN,     NamaPelanggan.getText().toString().trim());
                map.put(TAG_NAMA_BARANG,    NamaBarang.getText().toString().trim());
                map.put(TAG_TANGGAL_BARANG, Tanggal.getText().toString().trim());
                map.put(NO_TLP,             NoTlp.getText().toString().trim());
                map.put(TAG_HARGA_BARANG,   Harga.getText().toString().trim());
                map.put(TAG_JENIS_BARANG,   Jenis.getText().toString().trim());
                map.put(TAG_JUMLAH_BARANG,  Jumlah.getText().toString().trim());

                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(sendData);
    }

    private void showDialog() {
        if (!pd.isShowing())
            pd.show();
    }

    private void hideDialog() {
        if (pd.isShowing())
            pd.dismiss();
    }
}