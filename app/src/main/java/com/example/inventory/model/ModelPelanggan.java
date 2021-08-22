package com.example.inventory.model;

public class ModelPelanggan {
    String id,nama_Pelanggan, nama_Barang, tanggal, no_Tlp, harga, jenis, jumlah;

    public ModelPelanggan() {
    }

    public ModelPelanggan(String id,String nama_Pelanggan,
                          String nama_Barang, String tanggal,
                          String no_Tlp, String harga, String jenis, String jumlah) {

        this.id = id;
        this.nama_Pelanggan = nama_Pelanggan;
        this.nama_Barang = nama_Barang;
        this.tanggal = tanggal;
        this.no_Tlp = no_Tlp;
        this.harga = harga;
        this.jenis = jenis;
        this.jumlah = jumlah;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_Pelanggan() {
        return nama_Pelanggan;
    }

    public void setNama_Pelanggan(String nama_Pelanggan) {
        this.nama_Pelanggan = nama_Pelanggan;
    }

    public String getNama_Barang() {
        return nama_Barang;
    }

    public void setNama_Barang(String nama_Barang) {
        this.nama_Barang = nama_Barang;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNo_Tlp() {
        return no_Tlp;
    }

    public void setNo_Tlp(String no_Tlp) {
        this.no_Tlp = no_Tlp;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }
}
