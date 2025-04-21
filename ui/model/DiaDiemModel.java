package com.tourmanagement.ui.model;

import com.tourmanagement.entity.DiaDiem;

public class DiaDiemModel {
    private int maDiaDiem;
    private String tenDiaDiem;
    private String moTa;
    private String diaChi;
    private String thanhPho;
    private String quocGia;
    private String dacDiem;

    public DiaDiemModel() {}

    public DiaDiemModel(DiaDiem diaDiem) {
        this.maDiaDiem = diaDiem.getMaDiaDiem();
        this.tenDiaDiem = diaDiem.getTenDiaDiem();
        this.moTa = diaDiem.getMoTa();
        this.diaChi = diaDiem.getDiaChi();
        this.thanhPho = diaDiem.getThanhPho();
        this.quocGia = diaDiem.getQuocGia();
        this.dacDiem = diaDiem.getDacDiem();
    }

    public int getMaDiaDiem() {
        return maDiaDiem;
    }

    public void setMaDiaDiem(int maDiaDiem) {
        this.maDiaDiem = maDiaDiem;
    }

    public String getTenDiaDiem() {
        return tenDiaDiem;
    }

    public void setTenDiaDiem(String tenDiaDiem) {
        this.tenDiaDiem = tenDiaDiem;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getThanhPho() {
        return thanhPho;
    }

    public void setThanhPho(String thanhPho) {
        this.thanhPho = thanhPho;
    }

    public String getQuocGia() {
        return quocGia;
    }

    public void setQuocGia(String quocGia) {
        this.quocGia = quocGia;
    }

    public String getDacDiem() {
        return dacDiem;
    }

    public void setDacDiem(String dacDiem) {
        this.dacDiem = dacDiem;
    }
}