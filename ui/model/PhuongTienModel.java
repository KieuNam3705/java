package com.tourmanagement.ui.model;

import com.tourmanagement.entity.PhuongTien;

public class PhuongTienModel {
    private int maPhuongTien;
    private String loaiPhuongTien;
    private String tenPhuongTien;
    private String bienSo;
    private int soChoNgoi;
    private String congTy;
    private double giaThue;
    private String trangThai;

    public PhuongTienModel() {}

    public PhuongTienModel(PhuongTien phuongTien) {
        this.maPhuongTien = phuongTien.getMaPhuongTien();
        this.loaiPhuongTien = phuongTien.getLoaiPhuongTien();
        this.tenPhuongTien = phuongTien.getTenPhuongTien();
        this.bienSo = phuongTien.getBienSo();
        this.soChoNgoi = phuongTien.getSoChoNgoi();
        this.congTy = phuongTien.getCongTy();
        this.giaThue = phuongTien.getGiaThue();
        this.trangThai = phuongTien.getTrangThai();
    }

    public int getMaPhuongTien() {
        return maPhuongTien;
    }

    public void setMaPhuongTien(int maPhuongTien) {
        this.maPhuongTien = maPhuongTien;
    }

    public String getLoaiPhuongTien() {
        return loaiPhuongTien;
    }

    public void setLoaiPhuongTien(String loaiPhuongTien) {
        this.loaiPhuongTien = loaiPhuongTien;
    }

    public String getTenPhuongTien() {
        return tenPhuongTien;
    }

    public void setTenPhuongTien(String tenPhuongTien) {
        this.tenPhuongTien = tenPhuongTien;
    }

    public String getBienSo() {
        return bienSo;
    }

    public void setBienSo(String bienSo) {
        this.bienSo = bienSo;
    }

    public int getSoChoNgoi() {
        return soChoNgoi;
    }

    public void setSoChoNgoi(int soChoNgoi) {
        this.soChoNgoi = soChoNgoi;
    }

    public String getCongTy() {
        return congTy;
    }

    public void setCongTy(String congTy) {
        this.congTy = congTy;
    }

    public double getGiaThue() {
        return giaThue;
    }

    public void setGiaThue(double giaThue) {
        this.giaThue = giaThue;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}