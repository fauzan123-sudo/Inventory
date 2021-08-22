package com.example.inventory.model;

public class Data {
    private String id_barang, nama_barang, jenis_barang, jumlah_barang, harga_barang;

    public Data() {
    }
    public Data(String id_barang, String nama_barang, String jenis_barang, String jumlah_barang, String harga_barang) {
        this.id_barang      = id_barang;
        this.nama_barang    = nama_barang;
        this.jenis_barang   = jenis_barang;
        this.jumlah_barang  = jumlah_barang;
        this.harga_barang   = harga_barang;

    }

    public String getHarga_barang() {
        return harga_barang;
    }

    public void setHarga_barang(String harga_barang) {
        this.harga_barang = harga_barang;
    }

    public String getId_barang() {
        return id_barang;
    }

    public void setId_barang(String id_barang) {
        this.id_barang = id_barang;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getJenis_barang() {
        return jenis_barang;
    }

    public void setJenis_barang(String jenis_barang) {
        this.jenis_barang = jenis_barang;
    }

    public String getJumlah_barang() {
        return jumlah_barang;
    }

    public void setJumlah_barang(String jumlah_barang) {
        this.jumlah_barang = jumlah_barang;
    }
}
