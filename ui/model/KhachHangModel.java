package com.tourmanagement.ui.model;

import com.tourmanagement.entity.KhachHang;
import java.util.Date;

public class KhachHangModel {
    private int maKH;
    private String hoTen;
    private String cmnd;
    private String soDienThoai;
    private String email;
    private String diaChi;
    private Date ngaySinh;
    private String gioiTinh;
    private String quocTich;

    public KhachHangModel() {}

    public KhachHangModel(KhachHang khachHang) {
        this.maKH = khachHang.getMaKH();
        this.hoTen = khachHang.getHoTen();
        this.cmnd = khachHang.getCmnd();
        this.soDienThoai = khachHang.getSoDienThoai();
        this.email = khachHang.getEmail();
        this.diaChi = khachHang.getDiaChi();
        this.ngaySinh = khachHang.getNgaySinh();
        this.gioiTinh = khachHang.getGioiTinh();
        this.quocTich = khachHang.getQuocTich();
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getCmnd() {
        return cmnd;
    }

    public void setCmnd(String cmnd) {
        this.cmnd = cmnd;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getQuocTich() {
        return quocTich;
    }

    public void setQuocTich(String quocTich) {
        this.quocTich = quocTich;
    }
}