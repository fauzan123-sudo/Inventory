package com.example.inventory.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.inventory.R;
import com.example.inventory.adapter.AdapterPelanggan;
import com.example.inventory.helper.AppController;
import com.example.inventory.model.ModelPelanggan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.inventory.helper.Constans.DATA_PELANGGAN;

public class Pelanggan extends AppCompatActivity {
    public static RecyclerView mRecyclerview;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mManager;
    List<ModelPelanggan> modelPelanggans;
    ProgressDialog pd;
    FloatingActionButton Tambah;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelanggan);
        pd = new ProgressDialog(this);
        modelPelanggans = new ArrayList<>();
        mRecyclerview = findViewById(R.id.listPelanggan);
        mManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerview.setLayoutManager(mManager);
        mAdapter = new AdapterPelanggan(modelPelanggans, this);
        mRecyclerview.setAdapter(mAdapter);
        setUpSwipe();
        Tambah      = findViewById(R.id.tambah);
        Tambah.setOnClickListener(view->
                startActivity(new Intent(this, Tambah_Pelanggan.class))
                );

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                loadDataPelanggan();
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        }).attachToRecyclerView(mRecyclerview);

        loadDataPelanggan();
    }

    private void setUpSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                AdapterPelanggan adapter = (AdapterPelanggan) mRecyclerview.getAdapter();
                adapter.remove(swipedPosition);
            }

        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerview);

    }

    public void loadDataPelanggan() {
        pd.setCancelable(false);
        pd.setMessage("Sebentar ...");
        showDialog();
        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.POST, DATA_PELANGGAN,null,
                response -> {

                    for(int i = 0 ; i < response.length(); i++)
                    {
                        hideDialog();
                        try {
                            JSONObject data = response.getJSONObject(i);
                            ModelPelanggan mp = new ModelPelanggan();
                            mp.setId(data.getString("id_pelanggan"));
                            mp.setNama_Pelanggan(data.getString("nama_pelanggan"));
                            mp.setNama_Barang(data.getString("nama_barang"));
                            mp.setTanggal(data.getString("tanggal"));
                            mp.setNo_Tlp(data.getString("no_tlp"));
                            mp.setHarga(data.getString("harga_brg"));
                            mp.setJenis(data.getString("jenis_barang"));
                            mp.setJumlah(data.getString("jumlah_brg"));
                            modelPelanggans.add(mp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            hideDialog();
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                },
                error -> {
                    hideDialog();
                    Log.d("volley", "error : " + error.getMessage());
                });

        AppController.getInstance().addToRequestQueue(reqData);
    }

    private void showDialog() {
        if (!pd.isShowing())
            pd.show();
    }

    private void hideDialog() {
        if (pd.isShowing())
            pd.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Home.class));
    }
}