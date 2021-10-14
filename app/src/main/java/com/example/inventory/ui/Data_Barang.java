package com.example.inventory.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.inventory.R;
import com.example.inventory.adapter.Adapter;
import com.example.inventory.helper.AppController;
import com.example.inventory.model.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.inventory.helper.Constans.CARI_BARANG;
import static com.example.inventory.helper.Constans.TAG_HARGA_BARANG;
import static com.example.inventory.helper.Constans.TAG_ID;
import static com.example.inventory.helper.Constans.TAG_JENIS_BARANG;
import static com.example.inventory.helper.Constans.TAG_JUMLAH_BARANG;
import static com.example.inventory.helper.Constans.TAG_MESSAGE;
import static com.example.inventory.helper.Constans.TAG_NAMA_BARANG;
import static com.example.inventory.helper.Constans.TAG_QR_BARANG;
import static com.example.inventory.helper.Constans.TAG_TANGGAL_BARANG;
import static com.example.inventory.helper.Constans.TAMPIL_BARANG;
import static com.example.inventory.helper.Constans.tag_json_obj;

public class Data_Barang extends AppCompatActivity implements SearchView.OnQueryTextListener, ExampleDialog.ExampleDialogListener{
    ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeContainer;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mManager;
    RecyclerView.Adapter mAdapter;
    List<Data> itemList = new ArrayList<>();
    ProgressBar progressBar;

    private static final String TAG = Data_Barang.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_barang);

        swipeContainer = findViewById(R.id.swipeRefresh);
        fab            = findViewById(R.id.tambah_barang);
        recyclerView   = findViewById(R.id.list);
        mManager       = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mManager);
        progressBar    = findViewById(R.id.progressBar2);
        mAdapter       = new Adapter(this, itemList);
        recyclerView.setAdapter(mAdapter);
        fab.setOnClickListener(view -> startActivity(new Intent(Data_Barang.this, Tambah_Barang.class)));

        swipeContainer.setOnRefreshListener(this::loadBarang);

        loadBarang();

    }

    private void loadBarang() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sebentar ...");
        showDialog();
        itemList.clear();
        mAdapter.notifyDataSetChanged();

        JsonArrayRequest jArr = new JsonArrayRequest(TAMPIL_BARANG, response -> {
            progressBar.setVisibility(View.INVISIBLE);

            for (int i = 0; i < response.length(); i++) {
                hideDialog();
                swipeContainer.setRefreshing(false);
                try {
                    JSONObject obj  = response.getJSONObject(i);
                    Data item       = new Data();

                    item.setId_barang(obj.getString(TAG_ID));
                    item.setNama_barang(obj.getString(TAG_NAMA_BARANG));
                    item.setJenis_barang(obj.getString(TAG_JENIS_BARANG));
                    item.setJumlah_barang(obj.getString(TAG_JUMLAH_BARANG));
                    item.setHarga_barang(obj.getString(TAG_HARGA_BARANG));
                    item.setQr_barang(obj.getString(TAG_QR_BARANG));
                    item.setTanggal_barang(obj.getString(TAG_TANGGAL_BARANG));

                    itemList.add(item);
                } catch (JSONException e) {
                    hideDialog();
                    e.printStackTrace();
                }
            }

            mAdapter.notifyDataSetChanged();

        }, error -> {
            Toast.makeText(Data_Barang.this, "error"+error.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        cariData(query);
        return false;

    }

    private void cariData(String keyword) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, CARI_BARANG,
                response -> {
                    Log.e("Response: ", response);
                    progressDialog.hide();

                    try {
                        JSONObject jObj = new JSONObject(response);

                        int value = jObj.getInt("value");

                        if (value == 1) {
                            itemList.clear();
                            mAdapter.notifyDataSetChanged();

                            String getObject = jObj.getString("results");
                            JSONArray jsonArray = new JSONArray(getObject);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                Data data = new Data();

//                                data.setId_barang(obj.getString(TAG_ID));
                                data.setNama_barang(obj.getString(TAG_NAMA_BARANG));

                                itemList.add(data);
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }, error -> {
                    VolleyLog.e(TAG, "Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("keyword", keyword);

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final MenuItem itemQr = menu.findItem(R.id.action_qr);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint(getString(R.string.type_name));
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(this);

        itemQr.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                openDialog();
                return false;
            }

            private void openDialog() {
                ExampleDialog exampleDialog = new ExampleDialog();
                exampleDialog.show(getSupportFragmentManager(), "example dialog");
            }



        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void applyTexts(String hasil) {

    }
}