package com.tourmanagement.ui.model;

import com.tourmanagement.entity.Tour;
import java.math.BigDecimal;
import java.util.Date;

public class TourModel {
    private int maTour;
    private String tenTour;
    private String moTa;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private int soNgay;
    private int soLuongKhachToiDa;
    private BigDecimal giaTour;
    private String loaiTour;
    private String trangThai;
    private String hinhAnh;

    public TourModel() {}

    public TourModel(Tour tour) {
        this.maTour = tour.getMaTour();
        this.tenTour = tour.getTenTour();
        this.moTa = tour.getMoTa();
        this.ngayBatDau = tour.getNgayBatDau();
        this.ngayKetThuc = tour.getNgayKetThuc();
        this.soNgay = tour.getSoNgay();
        this.soLuongKhachToiDa = tour.getSoLuongKhachToiDa();
        this.giaTour = tour.getGiaTour();
        this.loaiTour = tour.getLoaiTour();
        this.trangThai = tour.getTrangThai();
        this.hinhAnh = tour.getHinhAnh();
    }

    // Getters and Setters
    public int getMaTour() {
        return maTour;
    }

    public void setMaTour(int maTour) {
        this.maTour = maTour;
    }

    public String getTenTour() {
        return tenTour;
    }

    public void setTenTour(String tenTour) {
        this.tenTour = tenTour;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public int getSoNgay() {
        return soNgay;
    }

    public void setSoNgay(int soNgay) {
        this.soNgay = soNgay;
    }

    public int getSoLuongKhachToiDa() {
        return soLuongKhachToiDa;
    }

    public void setSoLuongKhachToiDa(int soLuongKhachToiDa) {
        this.soLuongKhachToiDa = soLuongKhachToiDa;
    }

    public BigDecimal getGiaTour() {
        return giaTour;
    }

    public void setGiaTour(BigDecimal giaTour) {
        this.giaTour = giaTour;
    }

    public String getLoaiTour() {
        return loaiTour;
    }

    public void setLoaiTour(String loaiTour) {
        this.loaiTour = loaiTour;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}