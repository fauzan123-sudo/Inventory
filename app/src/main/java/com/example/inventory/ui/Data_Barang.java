package com.example.inventory.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.inventory.helper.Constans.HAPUS_BARANG;
import static com.example.inventory.helper.Constans.SHARED_PREFS;
import static com.example.inventory.helper.Constans.TAG_HARGA_BARANG;
import static com.example.inventory.helper.Constans.TAG_ID;
import static com.example.inventory.helper.Constans.TAG_JENIS_BARANG;
import static com.example.inventory.helper.Constans.TAG_JUMLAH_BARANG;
import static com.example.inventory.helper.Constans.TAG_MESSAGE;
import static com.example.inventory.helper.Constans.TAG_NAMA_BARANG;
import static com.example.inventory.helper.Constans.TAG_SUCCESS;
import static com.example.inventory.helper.Constans.TAMPIL_BARANG;
import static com.example.inventory.helper.Constans.TEXT;
import static com.example.inventory.helper.Constans.success;
import static com.example.inventory.helper.Constans.tag_json_obj;

public class Data_Barang extends AppCompatActivity{
    ProgressDialog progressDialog;
    FloatingActionButton fab;
    ListView list;
    List<Data> itemList = new ArrayList<>();
    Adapter adapter;
    ProgressBar progressBar;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    private static final String TAG = Data_Barang.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_barang);


        fab         = findViewById(R.id.tambah_barang);
        list        = findViewById(R.id.list);
        progressBar = findViewById(R.id.progressBar2);
        adapter     = new Adapter(Data_Barang.this, itemList);
        list.setAdapter(adapter);
        fab.setOnClickListener(view -> startActivity(new Intent(Data_Barang.this, Tambah_Barang.class)));

        loadBarang();


//        list.setOnItemClickListener((adapterView, view, i, l) -> {
//            final String idx = itemList.get(i).getNama_barang();
//            Log.e(TAG, "onCreate: as" + idx );
//            Log.d(TAG, "onCreate: as"+ idx);
//        });




//        list.setOnItemClickListener((adapterView, view, i, l) -> {
//            final String idx = itemList.get(i).getId_barang();
//            Intent intent = new Intent(Data_Barang.this, Detail_Barang.class);
//            intent.putExtra("id", idx);
//            startActivity(intent);
//        });
        list.setOnItemLongClickListener((parent, view, position, id) -> {
            final String idx = itemList.get(position).getId_barang();
            final CharSequence[] dialogitem = {"Edit", "Delete", "Detail"};
            dialog = new AlertDialog.Builder(Data_Barang.this);
            dialog.setCancelable(true);
            dialog.setItems(dialogitem, (dialog, which) -> {
                switch (which) {
                    case 0:
                        edit(idx);
                        break;
                    case 1:
                        delete(idx);
                        break;
                    case 2:
                        detail(idx);
                        break;
                }
            }).show();
            return false;
        });
    }

    private void detail(String idx) {
        Intent intent = new Intent(Data_Barang.this, Detail_Barang.class);
            intent.putExtra("id", idx);
            startActivity(intent);
    }

    private void delete(String idx) {
        StringRequest strReq = new StringRequest(Request.Method.POST, HAPUS_BARANG, response -> {
            try {
                JSONObject jObj = new JSONObject(response);
                success         = jObj.getInt(TAG_SUCCESS);

                if (success == 1) {
                    Toast.makeText(Data_Barang.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    adapter.notifyDataSetChanged();
                    loadBarang();

                } else {
                    Toast.makeText(this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            Log.e(TAG, "Error: " + error.getMessage());
            Toast.makeText(Data_Barang.this, error.getMessage(), Toast.LENGTH_LONG).show();
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", idx);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void edit(String idx) {
        Intent intent = new Intent(this, Edit_Barang.class);
        intent.putExtra(TAG_ID, idx);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT, idx);
        editor.apply();
        finish();
        startActivity(intent);

//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                UPDATE_BARANG, response -> {
//            Log.d(TAG, "Response: " + response);
//
//            try {
//                JSONObject jObj = new JSONObject(response);
//                success = jObj.getInt(TAG_SUCCESS);
//
//                if (success == 1) {
//                    Log.d("get edit data", jObj.toString());
//                    String idx1     = jObj.getString(TAG_ID);
//                    String namax    = jObj.getString(TAG_NAMA_BARANG);
//                    String jenisx   = jObj.getString(TAG_JENIS_BARANG);
//                    String hargax   = jObj.getString(TAG_HARGA_BARANG);
//                    String jumlahx  = jObj.getString(TAG_JUMLAH_BARANG);
//                    DialogForm(idx1, namax, jenisx, hargax, jumlahx);
//                    adapter.notifyDataSetChanged();
//
//                } else {
//                    Toast.makeText(this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }, error -> {
//            Log.e(TAG, "Error: " + error.getMessage());
//            Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters ke post url
//                Map<String, String> params = new HashMap<>();
//                params.put("id", idx);
//
//                return params;
//            }
//
//        };
//
//        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void loadBarang() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sebentar ...");
        showDialog();
        itemList.clear();
        adapter.notifyDataSetChanged();

        JsonArrayRequest jArr = new JsonArrayRequest(TAMPIL_BARANG, response -> {
            progressBar.setVisibility(View.INVISIBLE);

            for (int i = 0; i < response.length(); i++) {
                hideDialog();
                try {
                    JSONObject obj  = response.getJSONObject(i);
                    Data item       = new Data();

                    item.setId_barang(obj.getString(TAG_ID));
                    item.setNama_barang(obj.getString(TAG_NAMA_BARANG));
                    item.setJenis_barang(obj.getString(TAG_JENIS_BARANG));
                    item.setJumlah_barang(obj.getString(TAG_JUMLAH_BARANG));
                    item.setHarga_barang(obj.getString(TAG_HARGA_BARANG));

                    itemList.add(item);
                } catch (JSONException e) {
                    hideDialog();
                    e.printStackTrace();
                }
            }

            adapter.notifyDataSetChanged();

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

}