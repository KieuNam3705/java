package com.tourmanagement.ui.model;

import com.tourmanagement.entity.NhanVien;
import java.math.BigDecimal;
import java.util.Date;

public class NhanVienModel {
    private int maNV;
    private String hoTen;
    private String cmnd;
    private String soDienThoai;
    private String email;
    private String diaChi;
    private Date ngaySinh;
    private String gioiTinh;
    private String chucVu;
    private Date ngayVaoLam;
    private BigDecimal luong;

    public NhanVienModel() {}

    public NhanVienModel(NhanVien nhanVien) {
        this.maNV = nhanVien.getMaNV();
        this.hoTen = nhanVien.getHoTen();
        this.cmnd = nhanVien.getCmnd();
        this.soDienThoai = nhanVien.getSoDienThoai();
        this.email = nhanVien.getEmail();
        this.diaChi = nhanVien.getDiaChi();
        this.ngaySinh = nhanVien.getNgaySinh();
        this.gioiTinh = nhanVien.getGioiTinh();
        this.chucVu = nhanVien.getChucVu();
        this.ngayVaoLam = nhanVien.getNgayVaoLam();
        this.luong = nhanVien.getLuong();
    }

    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
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

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public Date getNgayVaoLam() {
        return ngayVaoLam;
    }

    public void setNgayVaoLam(Date ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    public BigDecimal getLuong() {
        return luong;
    }

    public void setLuong(BigDecimal luong) {
        this.luong = luong;
    }
}