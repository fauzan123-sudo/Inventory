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

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.inventory.R;
import com.example.inventory.helper.AppController;
import com.example.inventory.model.Data;
import com.example.inventory.ui.Data_Barang;
import com.example.inventory.ui.Detail_Barang;
import com.example.inventory.ui.Pelanggan;

import java.util.HashMap;
import java.util.List;

import static com.example.inventory.helper.Constans.HAPUS_BARANG;
import static com.example.inventory.helper.Constans.HAPUS_PELANGGAN;
import static com.example.inventory.helper.Constans.TAG_HARGA_BARANG;
import static com.example.inventory.helper.Constans.TAG_ID;
import static com.example.inventory.helper.Constans.TAG_JENIS_BARANG;
import static com.example.inventory.helper.Constans.TAG_JUMLAH_BARANG;
import static com.example.inventory.helper.Constans.TAG_NAMA_BARANG;
import static com.example.inventory.helper.Constans.TAG_QR_BARANG;
import static com.example.inventory.helper.Constans.TAG_TANGGAL_BARANG;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private final Context context;
    private final List<Data> data;

    public Adapter(Context context, List<Data> datas) {
        this.context = context;
        this.data = datas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new Adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Data dataa = data.get(position);
        holder.nama.setText(dataa.getNama_barang());
        holder.jenis.setText(dataa.getJenis_barang());
        holder.jumlah.setText(dataa.getJumlah_barang());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog dialog = new ProgressDialog(view.getContext());
                dialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, HAPUS_BARANG,
                        response -> {
                            dialog.hide();
                            dialog.dismiss();
                            Toast.makeText(view.getContext(),"Data Berhasil dihapus ",
                                    Toast.LENGTH_LONG).show();
                            context.startActivity(new Intent(context, Data_Barang.class));

                        }, error -> {
                    dialog.hide();
                    dialog.dismiss();
                }){
                    protected HashMap<String, String> getParams() {
                        HashMap<String, String> params= new HashMap<>();
                        params.put("id",dataa.getId_barang());
                        return params;
                    }
                };
                AppController.getInstance().addToRequestQueue(stringRequest);
            }
        });



        holder.data = dataa;

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nama, jenis,jumlah;
        ImageView delete;
        Data data;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        nama       =  itemView.findViewById(R.id.nama_barang);
        jenis      =  itemView.findViewById(R.id.jenis_barang);
        jumlah     =  itemView.findViewById(R.id.jumlah_barang);
        delete     =  itemView.findViewById(R.id.hapus);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detail = new Intent(context, Detail_Barang.class);
                detail.putExtra(TAG_NAMA_BARANG, data.getNama_barang());
                detail.putExtra(TAG_JENIS_BARANG, data.getJenis_barang());
                detail.putExtra(TAG_HARGA_BARANG, data.getHarga_barang());
                detail.putExtra(TAG_QR_BARANG, data.getQr_barang());
                detail.putExtra(TAG_JUMLAH_BARANG, data.getJumlah_barang());
                detail.putExtra(TAG_TANGGAL_BARANG, data.getTanggal_barang());
                detail.putExtra(TAG_ID, data.getId_barang());
                context.startActivity(detail);
            }
        });
        }
    }

//        TextView nama       =  convertView.findViewById(R.id.nama_barang);
//        TextView jenis      =  convertView.findViewById(R.id.jenis_barang);
//        TextView jumlah     =  convertView.findViewById(R.id.jumlah_barang);
//        ImageView delete    =  convertView.findViewById(R.id.hapus);
//
//        Data data = items.get(position);
//
//        nama.setText(data.getNama_barang());
//        jenis.setText(data.getJenis_barang());
//        jumlah.setText(data.getJumlah_barang());
//
//        return convertView;
    }

