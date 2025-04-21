package com.tourmanagement.ui.model;

import com.tourmanagement.entity.TaiKhoan;

public class TaiKhoanModel {
    private int maTaiKhoan;
    private String tenDangNhap;
    private String loaiTaiKhoan;
    private String trangThai;
    private Integer maKH;
    private Integer maNV;
    private String ngayTao;
    private String lanDangNhapCuoi;

    public TaiKhoanModel() {}

    public TaiKhoanModel(TaiKhoan taiKhoan) {
        this.maTaiKhoan = taiKhoan.getMaTaiKhoan();
        this.tenDangNhap = taiKhoan.getTenDangNhap();
        this.loaiTaiKhoan = taiKhoan.getLoaiTaiKhoan();
        this.trangThai = taiKhoan.getTrangThai();
        this.maKH = taiKhoan.getMaKH();
        this.maNV = taiKhoan.getMaNV();
        this.ngayTao = taiKhoan.getNgayTao();
        this.lanDangNhapCuoi = taiKhoan.getLanDangNhapCuoi();
    }

    public int getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public void setMaTaiKhoan(int maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getLoaiTaiKhoan() {
        return loaiTaiKhoan;
    }

    public void setLoaiTaiKhoan(String loaiTaiKhoan) {
        this.loaiTaiKhoan = loaiTaiKhoan;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public Integer getMaKH() {
        return maKH;
    }

    public void setMaKH(Integer maKH) {
        this.maKH = maKH;
    }

    public Integer getMaNV() {
        return maNV;
    }

    public void setMaNV(Integer maNV) {
        this.maNV = maNV;
    }

    public String getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }

    public String getLanDangNhapCuoi() {
        return lanDangNhapCuoi;
    }

    public void setLanDangNhapCuoi(String lanDangNhapCuoi) {
        this.lanDangNhapCuoi = lanDangNhapCuoi;
    }
}