package com.example.inventory.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.inventory.R;

import static com.example.inventory.helper.Constans.TAG_ID_USER;
import static com.example.inventory.helper.Constans.TAG_USERNAME;
import static com.example.inventory.helper.Constans.my_shared_preferences;

public class Home extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    Button TambahBarang, StokMasuk, StokKeluar, Laporan, Tentang, Pelanggan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

        getIntent().getStringExtra(TAG_ID_USER);
        getIntent().getStringExtra(TAG_USERNAME);

        Tentang = findViewById(R.id.tentang);
        StokKeluar = findViewById(R.id.stok_Keluar);
        StokKeluar.setOnClickListener(view -> startActivity(new Intent(Home.this, Scan.class)));
        StokMasuk = findViewById(R.id.stok_Masuk);
        StokMasuk.setOnClickListener(view -> startActivity(new Intent(Home.this, List_Barang.class)));
        Laporan= findViewById(R.id.laporan);
        Laporan.setOnClickListener(view -> startActivity(new Intent(Home.this, Laporan.class)));
        Pelanggan   = findViewById(R.id.pelanggan);

        Pelanggan.setOnClickListener(view -> startActivity(new Intent(this, Pelanggan.class)));


        Tentang.setOnClickListener(view -> startActivity(new Intent(this, Tentang.class)));

    }

    public void Data_Barang(View view) {
        startActivity(new Intent(Home.this, Data_Barang.class));
    }
}