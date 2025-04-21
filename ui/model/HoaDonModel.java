package com.tourmanagement.ui.model;

import com.tourmanagement.entity.HoaDon;
import java.math.BigDecimal;
import java.util.Date;

public class HoaDonModel {
    private int maHoaDon;
    private int maDatTour;
    private Date ngayThanhToan;
    private BigDecimal tongTien;
    private BigDecimal daThanhToan;
    private BigDecimal conLai;
    private String phuongThucThanhToan;
    private String trangThai;
    private String ghiChu;
    private int maNV;

    public HoaDonModel() {}

    public HoaDonModel(HoaDon hoaDon) {
        this.maHoaDon = hoaDon.getMaHoaDon();
        this.maDatTour = hoaDon.getMaDatTour();
        this.ngayThanhToan = hoaDon.getNgayThanhToan();
        this.tongTien = hoaDon.getTongTien();
        this.daThanhToan = hoaDon.getDaThanhToan();
        this.conLai = hoaDon.getConLai();
        this.phuongThucThanhToan = hoaDon.getPhuongThucThanhToan();
        this.trangThai = hoaDon.getTrangThai();
        this.ghiChu = hoaDon.getGhiChu();
        this.maNV = hoaDon.getMaNV();
    }

    // Getters và Setters
    public int getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(int maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public int getMaDatTour() {
        return maDatTour;
    }

    public void setMaDatTour(int maDatTour) {
        this.maDatTour = maDatTour;
    }

    public Date getNgayThanhToan() {
        return ngayThanhToan;
    }

    public void setNgayThanhToan(Date ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public BigDecimal getDaThanhToan() {
        return daThanhToan;
    }

    public void setDaThanhToan(BigDecimal daThanhToan) {
        this.daThanhToan = daThanhToan;
    }

    public BigDecimal getConLai() {
        return conLai;
    }

    public void setConLai(BigDecimal conLai) {
        this.conLai = conLai;
    }

    public String getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(String phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }
}