package com.tourmanagement.ui.model;

import com.tourmanagement.entity.ChoO;
import java.math.BigDecimal;

public class ChoOModel {
    private int maChoO;
    private String tenChoO;
    private String loaiChoO;
    private String diaChi;
    private String thanhPho;
    private String quocGia;
    private String soDienThoai;
    private String email;
    private String website;
    private int soSao;
    private BigDecimal giaThue;

    public ChoOModel() {}

    public ChoOModel(ChoO choO) {
        this.maChoO = choO.getMaChoO();
        this.tenChoO = choO.getTenChoO();
        this.loaiChoO = choO.getLoaiChoO();
        this.diaChi = choO.getDiaChi();
        this.thanhPho = choO.getThanhPho();
        this.quocGia = choO.getQuocGia();
        this.soDienThoai = choO.getSoDienThoai();
        this.email = choO.getEmail();
        this.website = choO.getWebsite();
        this.soSao = choO.getSoSao();
        this.giaThue = choO.getGiaThue();
    }

    public int getMaChoO() {
        return maChoO;
    }

    public void setMaChoO(int maChoO) {
        this.maChoO = maChoO;
    }

    public String getTenChoO() {
        return tenChoO;
    }

    public void setTenChoO(String tenChoO) {
        this.tenChoO = tenChoO;
    }

    public String getLoaiChoO() {
        return loaiChoO;
    }

    public void setLoaiChoO(String loaiChoO) {
        this.loaiChoO = loaiChoO;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getSoSao() {
        return soSao;
    }

    public void setSoSao(int soSao) {
        this.soSao = soSao;
    }

    public BigDecimal getGiaThue() {
        return giaThue;
    }

    public void setGiaThue(BigDecimal giaThue) {
        this.giaThue = giaThue;
    }
}