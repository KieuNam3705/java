package com.tourmanagement.ui.controller;

import com.tourmanagement.entity.*;
import com.tourmanagement.service.ServiceFactory;
import com.tourmanagement.service.interfaces.*;
import com.tourmanagement.exception.BusinessLogicException;
import com.tourmanagement.ui.dialogs.AddEditTourDialog;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddEditTourDialogController {
    private AddEditTourDialog view;
    private Tour tour;
    private ITourService tourService;
    private boolean saveSuccess = false;
    private boolean isEditMode;

    public AddEditTourDialogController(AddEditTourDialog view, Tour tour) {
        this.view = view;
        this.tour = tour;
        this.tourService = ServiceFactory.getInstance().getTourService();
        this.isEditMode = (tour != null);
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public boolean isSaveSuccess() {
        return saveSuccess;
    }

    public void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn hình ảnh");
        fileChooser.setFileFilter(new FileNameExtensionFilter(
            "Image files", "jpg", "jpeg", "png", "gif", "bmp"));
        
        int result = fileChooser.showOpenDialog(view);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Lấy đường dẫn tuyệt đối đến thư mục project
                String projectPath = new File("").getAbsolutePath();
                
                // Tạo thư mục image nếu chưa tồn tại
                File imageDir = new File(projectPath, "image");
                if (!imageDir.exists()) {
                    imageDir.mkdir();
                }
                
                // Tạo tên file mới tránh trùng lặp
                String fileName = "tour_" + System.currentTimeMillis() + "_" + selectedFile.getName();
                File destFile = new File(imageDir, fileName);
                
                // Copy file
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                
                // Cập nhật đường dẫn tương đối vào field
                view.getHinhAnhField().setText("image/" + fileName);
                
                JOptionPane.showMessageDialog(view, 
                    "Đã lưu hình ảnh thành công vào: " + destFile.getAbsolutePath(), 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(view, 
                    "Lỗi lưu hình ảnh: " + ex.getMessage(), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void loadTourData() {
        if (tour != null) {
            // Tải dữ liệu cơ bản
            view.getTenTourField().setText(tour.getTenTour());
            view.getMoTaArea().setText(tour.getMoTa());

            // Set dates
            view.getNgayBatDauField().setValue(tour.getNgayBatDau());
            view.getNgayKetThucField().setValue(tour.getNgayKetThuc());

            // Set spinner values
            view.getSoNgaySpinner().setValue(tour.getSoNgay());
            view.getSoLuongKhachSpinner().setValue(tour.getSoLuongKhachToiDa());

            // Set price
            view.getGiaTourField().setText(tour.getGiaTour().toString());

            // Set selected items in combo boxes
            view.getLoaiTourComboBox().setSelectedItem(tour.getLoaiTour());
            view.getTrangThaiComboBox().setSelectedItem(tour.getTrangThai());

            // Set image URL
            if (tour.getHinhAnh() != null) {
                view.getHinhAnhField().setText(tour.getHinhAnh());
            }

            // Tải dữ liệu mối quan hệ
            int maTour = tour.getMaTour();

            try {
                // Tải danh sách địa điểm
                IDiaDiemService diaDiemService = ServiceFactory.getInstance().getDiaDiemService();
                List<DiaDiem> danhSachDiaDiem = diaDiemService.layDanhSachTheoTour(maTour);
                view.getSelectedDiaDiemModel().clear();
                for (DiaDiem diaDiem : danhSachDiaDiem) {
                    view.getSelectedDiaDiemModel().addElement(diaDiem);
                }

                // Tải danh sách phương tiện
                IPhuongTienService phuongTienService = ServiceFactory.getInstance().getPhuongTienService();
                List<PhuongTien> danhSachPhuongTien = phuongTienService.layDanhSachTheoTour(maTour);
                view.getSelectedPhuongTienModel().clear();
                for (PhuongTien phuongTien : danhSachPhuongTien) {
                    view.getSelectedPhuongTienModel().addElement(phuongTien);
                }

                // Tải danh sách chỗ ở
                IChoOService choOService = ServiceFactory.getInstance().getChoOService();
                List<ChoO> danhSachChoO = choOService.layDanhSachTheoTour(maTour);
                view.getSelectedChoOModel().clear();
                for (ChoO choO : danhSachChoO) {
                    view.getSelectedChoOModel().addElement(choO);
                }

                // Tải danh sách hướng dẫn viên
                INhanVienService nhanVienService = ServiceFactory.getInstance().getNhanVienService();
                List<NhanVien> danhSachHDV = nhanVienService.layDanhSachTheoTour(maTour);
                view.getSelectedHuongDanVienModel().clear();
                for (NhanVien nhanVien : danhSachHDV) {
                    view.getSelectedHuongDanVienModel().addElement(nhanVien);
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, 
                    "Lỗi khi tải dữ liệu chi tiết tour: " + e.getMessage(), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

 
    public void saveTour() {
        try {
            // Validate và lấy dữ liệu từ form
            String tenTour = view.getTenTourField().getText().trim();
            if (tenTour.isEmpty()) {
                throw new BusinessLogicException("Tên tour không được để trống");
            }

            String moTa = view.getMoTaArea().getText().trim();
            if (moTa.isEmpty()) {
                throw new BusinessLogicException("Mô tả tour không được để trống");
            }

            // Validate và parse dates
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date ngayBatDau = null;
            Date ngayKetThuc = null;

            try {
                ngayBatDau = dateFormat.parse(view.getNgayBatDauField().getText());
                ngayKetThuc = dateFormat.parse(view.getNgayKetThucField().getText());
            } catch (ParseException e) {
                throw new BusinessLogicException("Định dạng ngày không hợp lệ. Vui lòng nhập theo định dạng dd/MM/yyyy");
            }

            if (ngayBatDau.after(ngayKetThuc)) {
                throw new BusinessLogicException("Ngày bắt đầu không thể sau ngày kết thúc");
            }

            // Lấy giá trị spinner
            int soNgay = (Integer) view.getSoNgaySpinner().getValue();
            int soLuongKhachToiDa = (Integer) view.getSoLuongKhachSpinner().getValue();

            // Parse price
            BigDecimal giaTour;
            try {
                giaTour = new BigDecimal(view.getGiaTourField().getText().replace(",", ""));
            } catch (NumberFormatException e) {
                throw new BusinessLogicException("Giá tour không hợp lệ");
            }

            if (giaTour.compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessLogicException("Giá tour phải lớn hơn 0");
            }

            // Lấy giá trị từ combo box
            String loaiTour = (String) view.getLoaiTourComboBox().getSelectedItem();
            String trangThai = (String) view.getTrangThaiComboBox().getSelectedItem();

            // Lấy đường dẫn hình ảnh
            String hinhAnh = view.getHinhAnhField().getText().trim();

            // Tạo hoặc cập nhật tour
            int maTour;
            boolean successBasic;

            if (isEditMode) {
                // Cập nhật tour hiện tại
                tour.setTenTour(tenTour);
                tour.setMoTa(moTa);
                tour.setNgayBatDau(ngayBatDau);
                tour.setNgayKetThuc(ngayKetThuc);
                tour.setSoNgay(soNgay);
                tour.setSoLuongKhachToiDa(soLuongKhachToiDa);
                tour.setGiaTour(giaTour);
                tour.setLoaiTour(loaiTour);
                tour.setTrangThai(trangThai);
                tour.setHinhAnh(hinhAnh);

                successBasic = tourService.sua(tour);
                maTour = tour.getMaTour();
            } else {
                // Tạo tour mới
                Tour newTour = new Tour();
                newTour.setTenTour(tenTour);
                newTour.setMoTa(moTa);
                newTour.setNgayBatDau(ngayBatDau);
                newTour.setNgayKetThuc(ngayKetThuc);
                newTour.setSoNgay(soNgay);
                newTour.setSoLuongKhachToiDa(soLuongKhachToiDa);
                newTour.setGiaTour(giaTour);
                newTour.setLoaiTour(loaiTour);
                newTour.setLoaiTour(trangThai);
                newTour.setLoaiTour(hinhAnh);
                successBasic = tourService.them(newTour);
                // Lấy mã tour vừa thêm
                if (successBasic) {
                    List<Tour> tourList = tourService.timTheoTen(tenTour);
                    if (!tourList.isEmpty()) {
                        tour = tourList.get(0);
                        maTour = tour.getMaTour();
                    } else {
                        throw new BusinessLogicException("Không thể lấy mã tour vừa tạo");
                    }
                } else {
                    throw new BusinessLogicException("Không thể tạo tour mới");
                }
            }

            // Nếu thao tác cơ bản thành công, tiếp tục lưu các mối quan hệ
            if (successBasic) {
                // Lấy service quản lý lịch trình
                IQuanLyLichTrinhService quanLyLichTrinhService = ServiceFactory.getInstance().getQuanLyLichTrinhService();

                try {
                    // Xóa các mối quan hệ cũ nếu đang ở chế độ chỉnh sửa
                    if (isEditMode) {
                        // Xóa các địa điểm cũ
                        List<TourDiaDiem> oldDiaDiems = quanLyLichTrinhService.layDanhSachDiemDen(maTour);
                        for (TourDiaDiem oldItem : oldDiaDiems) {
                            quanLyLichTrinhService.xoaDiemDen(maTour, oldItem.getMaDiaDiem());
                        }

                        // Xóa các phương tiện cũ
                        List<TourPhuongTien> oldPhuongTiens = quanLyLichTrinhService.layDanhSachPhuongTien(maTour);
                        for (TourPhuongTien oldItem : oldPhuongTiens) {
                            quanLyLichTrinhService.xoaPhuongTien(maTour, oldItem.getMaPhuongTien());
                        }

                        // Xóa các chỗ ở cũ
                        List<TourChoO> oldChoOs = quanLyLichTrinhService.layDanhSachChoO(maTour);
                        for (TourChoO oldItem : oldChoOs) {
                            quanLyLichTrinhService.xoaChoO(maTour, oldItem.getMaChoO());
                        }

                        // Xóa các nhân viên cũ
                        List<TourNhanVien> oldNhanViens = quanLyLichTrinhService.layDanhSachNhanVien(maTour);
                        for (TourNhanVien oldItem : oldNhanViens) {
                            quanLyLichTrinhService.xoaNhanVien(maTour, oldItem.getMaNV());
                        }
                    }

                    // Thêm các địa điểm mới
                    for (int i = 0; i < view.getSelectedDiaDiemModel().size(); i++) {
                        DiaDiem diaDiem = view.getSelectedDiaDiemModel().getElementAt(i);
                        quanLyLichTrinhService.themDiemDen(maTour, diaDiem.getMaDiaDiem(), i + 1, 0);
                    }

                    // Thêm các phương tiện mới
                    for (int i = 0; i < view.getSelectedPhuongTienModel().size(); i++) {
                        PhuongTien phuongTien = view.getSelectedPhuongTienModel().getElementAt(i);
                        quanLyLichTrinhService.themPhuongTien(maTour, phuongTien.getMaPhuongTien(),
                                ngayBatDau, ngayKetThuc);
                    }

                    // Thêm các chỗ ở mới
                    for (int i = 0; i < view.getSelectedChoOModel().size(); i++) {
                        ChoO choO = view.getSelectedChoOModel().getElementAt(i);
                        quanLyLichTrinhService.themChoO(maTour, choO.getMaChoO(),
                                ngayBatDau, ngayKetThuc, 1, "Phòng Standard", "");
                    }

                    // Thêm các hướng dẫn viên mới
                    for (int i = 0; i < view.getSelectedHuongDanVienModel().size(); i++) {
                        NhanVien nhanVien = view.getSelectedHuongDanVienModel().getElementAt(i);
                        quanLyLichTrinhService.themNhanVien(maTour, nhanVien.getMaNV(), "Hướng dẫn viên");
                    }

                    // Tính tổng giá mới (giá tour cơ bản + giá phương tiện + giá chỗ ở)
                    BigDecimal tongGia = giaTour; // Giá tour cơ bản

                    // Cộng giá phương tiện
                    for (int i = 0; i < view.getSelectedPhuongTienModel().size(); i++) {
                        PhuongTien phuongTien = view.getSelectedPhuongTienModel().getElementAt(i);
                        tongGia = tongGia.add(BigDecimal.valueOf(phuongTien.getGiaThue()));
                    }

                    // Cộng giá chỗ ở
                    for (int i = 0; i < view.getSelectedChoOModel().size(); i++) {
                        ChoO choO = view.getSelectedChoOModel().getElementAt(i);
                        tongGia = tongGia.add(choO.getGiaThue());
                    }

                    // Cập nhật lại tổng giá cho tour
                    tour.setGiaTour(tongGia);
                    tourService.sua(tour);

                    // Đánh dấu lưu thành công
                    saveSuccess = true;

                    // Hiển thị thông báo
                    JOptionPane.showMessageDialog(view, 
                        "Tổng giá tour đã được cập nhật: " + tongGia + " VNĐ\n" +
                        (isEditMode ? "Cập nhật tour thành công" : "Thêm tour mới thành công"), 
                        "Thành Công", 
                        JOptionPane.INFORMATION_MESSAGE);

                    // Đóng dialog
                    view.dispose();

                } catch (BusinessLogicException e) {
                    JOptionPane.showMessageDialog(view, 
                        "Lỗi khi lưu chi tiết tour: " + e.getMessage(), 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(view, 
                    isEditMode ? "Không thể cập nhật tour" : "Không thể thêm tour mới", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (BusinessLogicException e) {
            JOptionPane.showMessageDialog(view, 
                e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, 
                "Đã xảy ra lỗi: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
            