package com.example.inventory.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.toolbox.JsonArrayRequest;
import com.example.inventory.R;
import com.example.inventory.adapter.AdapterLaporan;
import com.example.inventory.helper.AppController;
import com.example.inventory.model.ModelLaporan;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.inventory.helper.Constans.NAMA_PELANGGAN;
import static com.example.inventory.helper.Constans.NO_TLP;
import static com.example.inventory.helper.Constans.TAG_AKSI;
import static com.example.inventory.helper.Constans.TAG_HARGA_BARANG;
import static com.example.inventory.helper.Constans.TAG_ID_LAPORAN;
import static com.example.inventory.helper.Constans.TAG_JENIS_BARANG;
import static com.example.inventory.helper.Constans.TAG_JUMLAH_BARANG;
import static com.example.inventory.helper.Constans.TAG_NAMA_BARANG;
import static com.example.inventory.helper.Constans.TAG_QR_BARANG;
import static com.example.inventory.helper.Constans.TAG_TANGGAL_BARANG;
import static com.example.inventory.helper.Constans.TAG_WAKTU;
import static com.example.inventory.helper.Constans.TAMPIL_LAPORAN;

public class Laporan extends AppCompatActivity {
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mManager;
    private RecyclerView.Adapter mAdapter;
    private List<ModelLaporan> itemList = new ArrayList<>();
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);

        swipeContainer = findViewById(R.id.swipeRefresh);
        recyclerView   = findViewById(R.id.recyclerView);
        mManager       = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mManager);
        progressBar    = findViewById(R.id.progressBar2);
        mAdapter       = new AdapterLaporan(itemList, this);
        recyclerView.setAdapter(mAdapter);
        swipeContainer.setOnRefreshListener(this::loadLaporan);

        loadLaporan();
    }

    private void loadLaporan() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sebentar ...");
        showDialog();
        itemList.clear();
        mAdapter.notifyDataSetChanged();

        JsonArrayRequest jArr = new JsonArrayRequest(TAMPIL_LAPORAN, response -> {
            progressBar.setVisibility(View.INVISIBLE);

            for (int i = 0; i < response.length(); i++) {
                hideDialog();
                swipeContainer.setRefreshing(false);
                try {
                    JSONObject obj  = response.getJSONObject(i);
                    ModelLaporan item       = new ModelLaporan();

                    item.setId_laporans(obj.getString(TAG_ID_LAPORAN));
                    item.setNama_barangs(obj.getString(TAG_NAMA_BARANG));
                    item.setAksis(obj.getString(TAG_AKSI));
                    item.setWaktus(obj.getString(TAG_WAKTU));
                    item.setNama_pelanggans(obj.getString(NAMA_PELANGGAN));
                    item.setNo_tlps(obj.getString(NO_TLP));
                    item.setTanggals(obj.getString(TAG_TANGGAL_BARANG));
                    item.setBarcodes(obj.getString(TAG_QR_BARANG));
                    item.setJenis_barangs(obj.getString(TAG_JENIS_BARANG));
                    item.setHarga_brgs(obj.getString(TAG_HARGA_BARANG));
                    item.setJumlah_brgs(obj.getString(TAG_JUMLAH_BARANG));

                    itemList.add(item);
                } catch (JSONException e) {
                    hideDialog();
                    e.printStackTrace();
                }
            }

            mAdapter.notifyDataSetChanged();

        }, error -> {
            Toast.makeText(Laporan.this, "error"+error.getMessage(), Toast.LENGTH_SHORT).show();
            hideDialog();
        });

        AppController.getInstance().addToRequestQueue(jArr);
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
        startActivity(new Intent(this, Home.class));
        finish();
    }
}