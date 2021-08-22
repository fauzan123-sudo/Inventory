package com.example.inventory.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.inventory.R;
import com.example.inventory.helper.AppController;
import com.example.inventory.model.ModelPelanggan;
import com.example.inventory.ui.Pelanggan;
import com.example.inventory.ui.Tambah_Pelanggan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.inventory.helper.Constans.HAPUS_PELANGGAN;
import static com.example.inventory.helper.Constans.NAMA_PELANGGAN;
import static com.example.inventory.helper.Constans.NO_TLP;
import static com.example.inventory.helper.Constans.TAG_HARGA_BARANG;
import static com.example.inventory.helper.Constans.TAG_ID;
import static com.example.inventory.helper.Constans.TAG_JENIS_BARANG;
import static com.example.inventory.helper.Constans.TAG_JUMLAH_BARANG;
import static com.example.inventory.helper.Constans.TAG_NAMA_BARANG;
import static com.example.inventory.helper.Constans.TAG_TANGGAL_BARANG;
import static com.example.inventory.ui.Pelanggan.mRecyclerview;

public class AdapterPelanggan extends RecyclerView.Adapter<AdapterPelanggan.ViewHolder> {
    private final List<ModelPelanggan> mItems;
    private final Context context;
    private ProgressDialog dialog;

    public AdapterPelanggan(List<ModelPelanggan> modelPelanggans, Context context) {
        this.mItems = modelPelanggans;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pelanggan, parent, false);
        return new AdapterPelanggan.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelPelanggan mp  = mItems.get(position);
        holder.nama_barang.setText(mp.getNama_Barang());
        holder.nama_pelanggan.setText(mp.getNama_Pelanggan());
        holder.tanggal.setText(mp.getTanggal());
        holder.no_tlp.setText(mp.getNo_Tlp());
        holder.hargabrg.setText(mp.getHarga());
        holder.jenis_barang.setText(mp.getJenis());
        holder.jumlah_brg.setText(mp.getJumlah());
        holder.hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog dialog = new ProgressDialog(view.getContext());
                dialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, HAPUS_PELANGGAN,
                        response -> {
                            dialog.hide();
                            dialog.dismiss();
                            Toast.makeText(view.getContext(),"Data Berhasil dihapus ",
                                    Toast.LENGTH_LONG).show();
                            context.startActivity(new Intent(context, Pelanggan.class));

                        }, error -> {
                            dialog.hide();
                            dialog.dismiss();
                        }){
                    protected HashMap<String, String> getParams() {
                        HashMap<String, String> params= new HashMap<>();
                        params.put("id_pelanggan",mp.getId());
                        return params;
                    }
                };
                AppController.getInstance().addToRequestQueue(stringRequest);
            }
        });

        holder.mp = mp;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void remove(int position) {
        if (position < 0 || position >= mItems.size()) {
            return;
        }
        Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
        mItems.remove(position);
        mRecyclerview.removeViewAt(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama_barang, nama_pelanggan, tanggal, no_tlp, hargabrg, jenis_barang, jumlah_brg;
        ModelPelanggan mp;
        ImageView hapus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama_barang     = itemView.findViewById(R.id.nama_barang);
            nama_pelanggan  = itemView.findViewById(R.id.nama_pelanggan);
            tanggal         = itemView.findViewById(R.id.tanggal);
            no_tlp          = itemView.findViewById(R.id.no_tlp);
            hargabrg        = itemView.findViewById(R.id.harga);
            jenis_barang    = itemView.findViewById(R.id.jenis_barang);
            jumlah_brg      = itemView.findViewById(R.id.jumlah_brg);
            hapus           = itemView.findViewById(R.id.hapus);

            itemView.setOnClickListener(view -> {
                Intent update = new Intent(context, Tambah_Pelanggan.class);
                update.putExtra("update",1);
                update.putExtra(TAG_ID, mp.getId());
                update.putExtra(TAG_NAMA_BARANG,mp.getNama_Barang());
                update.putExtra(NAMA_PELANGGAN,mp.getNama_Pelanggan());
                update.putExtra(TAG_TANGGAL_BARANG,mp.getTanggal());
                update.putExtra(NO_TLP,mp.getNo_Tlp());
                update.putExtra(TAG_HARGA_BARANG,mp.getHarga());
                update.putExtra(TAG_JENIS_BARANG,mp.getJenis());
                update.putExtra(TAG_JUMLAH_BARANG,mp.getJumlah());

                context.startActivity(update);
            });
        }
    }
}
