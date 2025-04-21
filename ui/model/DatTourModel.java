package com.tourmanagement.ui.model;

import com.tourmanagement.entity.DatTour;
import java.math.BigDecimal;
import java.util.Date;

public class DatTourModel {
    private int maDatTour;
    private int maKH;
    private int maTour;
    private Date ngayDat;
    private int soLuongNguoi;
    private BigDecimal tongTien;
    private String trangThai;
    private String ghiChu;
    private String yeuCauDacBiet;
    private int maNV;

    public DatTourModel() {}

    public DatTourModel(DatTour datTour) {
        this.maDatTour = datTour.getMaDatTour();
        this.maKH = datTour.getMaKH();
        this.maTour = datTour.getMaTour();
        this.ngayDat = datTour.getNgayDat();
        this.soLuongNguoi = datTour.getSoLuongNguoi();
        this.tongTien = datTour.getTongTien();
        this.trangThai = datTour.getTrangThai();
        this.ghiChu = datTour.getGhiChu();
        this.yeuCauDacBiet = datTour.getYeuCauDacBiet();
        this.maNV = datTour.getMaNV();
    }

    public int getMaDatTour() {
        return maDatTour;
    }

    public void setMaDatTour(int maDatTour) {
        this.maDatTour = maDatTour;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public int getMaTour() {
        return maTour;
    }

    public void setMaTour(int maTour) {
        this.maTour = maTour;
    }

    public Date getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(Date ngayDat) {
        this.ngayDat = ngayDat;
    }

    public int getSoLuongNguoi() {
        return soLuongNguoi;
    }

    public void setSoLuongNguoi(int soLuongNguoi) {
        this.soLuongNguoi = soLuongNguoi;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
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

    public String getYeuCauDacBiet() {
        return yeuCauDacBiet;
    }

    public void setYeuCauDacBiet(String yeuCauDacBiet) {
        this.yeuCauDacBiet = yeuCauDacBiet;
    }

    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }
}