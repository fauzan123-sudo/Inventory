package com.example.inventory.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.inventory.R;
import com.example.inventory.helper.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.inventory.helper.Constans.TAG_HARGA_BARANG;
import static com.example.inventory.helper.Constans.TAG_ID;
import static com.example.inventory.helper.Constans.TAG_JENIS_BARANG;
import static com.example.inventory.helper.Constans.TAG_JUMLAH_BARANG;
import static com.example.inventory.helper.Constans.TAG_MESSAGE;
import static com.example.inventory.helper.Constans.TAG_NAMA_BARANG;
import static com.example.inventory.helper.Constans.TAG_SUCCESS;
import static com.example.inventory.helper.Constans.TAMPIL_BARANG;
import static com.example.inventory.helper.Constans.UPDATE_BARANG;
import static com.example.inventory.helper.Constans.success;
import static com.example.inventory.helper.Constans.tag_json_obj;

public class Detail_Barang extends AppCompatActivity {
    ProgressDialog progressDialog;
    TextView namaBarang, jenisBarang, Tanggal, Barcode, Jumlah, Harga;
    String id;
    Button Kembali;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_barang);

        id           = getIntent().getStringExtra("id");
        tampil();
        namaBarang  = findViewById(R.id.nama_barang);
        jenisBarang = findViewById(R.id.jenis_barang);
        Tanggal     = findViewById(R.id.tanggal);
        Barcode     = findViewById(R.id.barcode);
        Jumlah      = findViewById(R.id.jumlah_barang);
        Harga       = findViewById(R.id.harga_barang);
        Kembali     = findViewById(R.id.kembali);

        Kembali.setOnClickListener(view -> startActivity(new Intent(Detail_Barang.this, Data_Barang.class)));
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
                    Harga.setText(hargax);
                    jenisBarang.setText(jenisx);
                    Jumlah.setText(jumlahx);
                    Barcode.setText(barcode);
                    Tanggal.setText(tanggal);
                } catch (JSONException e) {
                    hideDialog();
                    e.printStackTrace();
                }
            }
        }, error -> {
            hideDialog();
            Toast.makeText(this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
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
}