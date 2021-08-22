package com.example.inventory.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventory.R;
import com.example.inventory.model.Data;

import java.util.List;

public class Adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Data> items;

    public Adapter(Activity activity, List<Data> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.item, null);


        TextView nama       =  convertView.findViewById(R.id.nama_barang);
        TextView jenis      =  convertView.findViewById(R.id.jenis_barang);
        TextView jumlah     =  convertView.findViewById(R.id.jumlah_barang);
        ImageView delete    =  convertView.findViewById(R.id.hapus);

        Data data = items.get(position);

        nama.setText(data.getNama_barang());
        jenis.setText(data.getJenis_barang());
        jumlah.setText(data.getJumlah_barang());

        return convertView;
    }

}
