package com.tourmanagement.ui.dialogs;

import com.tourmanagement.ui.components.CustomButton;
import com.tourmanagement.ui.components.CustomLabel;
import com.tourmanagement.ui.components.CustomTextField;
import com.tourmanagement.service.ServiceFactory;
import com.tourmanagement.service.interfaces.IDatTourService;
import com.tourmanagement.service.interfaces.ITourService;
import com.tourmanagement.service.interfaces.IKhachHangService;
import com.tourmanagement.service.interfaces.INhanVienService;
import com.tourmanagement.entity.DatTour;
import com.tourmanagement.entity.Tour;
import com.tourmanagement.entity.KhachHang;
import com.tourmanagement.entity.NhanVien;
import com.tourmanagement.exception.BusinessLogicException;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class AddEditBookingDialog extends JDialog {
    private IDatTourService datTourService;
    private ITourService tourService;
    private IKhachHangService khachHangService;
    private INhanVienService nhanVienService;
    
    private DatTour datTour;
    private boolean isEditMode;
    private boolean saveSuccess = false;
    
    // UI Components
    private JComboBox<CustomerComboItem> khachHangComboBox;
    private JComboBox<TourComboItem> tourComboBox;
    private JSpinner soLuongNguoiSpinner;
    private JLabel tongTienLabel;
    private JComboBox<String> trangThaiComboBox;
    private JTextArea ghiChuArea;
    private JTextArea yeuCauDacBietArea;
    private JComboBox<StaffComboItem> nhanVienComboBox;
    
    public AddEditBookingDialog(Frame parent, DatTour datTour, int currentStaffId) {
        super(parent, datTour == null ? "Thêm Đặt Tour Mới" : "Sửa Đặt Tour", true);
        
        this.datTourService = ServiceFactory.getInstance().getDatTourService();
        this.tourService = ServiceFactory.getInstance().getTourService();
        this.khachHangService = ServiceFactory.getInstance().getKhachHangService();
        this.nhanVienService = ServiceFactory.getInstance().getNhanVienService();
        
        this.datTour = datTour;
        this.isEditMode = (datTour != null);
        
        initComponents(currentStaffId);
        if (isEditMode) {
            loadBookingData();
        }
    }
    
    private void initComponents(int currentStaffId) {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        
        CustomLabel titleLabel = new CustomLabel(isEditMode ? "SỬA THÔNG TIN ĐẶT TOUR" : "THÊM ĐẶT TOUR MỚI");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 55, 153));
        
        titlePanel.add(titleLabel);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Khách hàng
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Khách hàng:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        // Load danh sách khách hàng
        List<KhachHang> khachHangs = khachHangService.layDanhSach();
        CustomerComboItem[] khachHangItems = new CustomerComboItem[khachHangs.size()];
        for (int i = 0; i < khachHangs.size(); i++) {
            KhachHang kh = khachHangs.get(i);
            khachHangItems[i] = new CustomerComboItem(kh.getMaKH(), kh.getHoTen(), kh.getSoDienThoai());
        }
        khachHangComboBox = new JComboBox<>(khachHangItems);
        formPanel.add(khachHangComboBox, gbc);
        
        // Tour
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Tour:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        // Load danh sách tour
        List<Tour> tours = tourService.layDanhSach();
        List<TourComboItem> tourItems = new ArrayList<>();
        for (Tour tour : tours) {
            // Chỉ hiển thị tour đang mở đặt
            if ("Đang mở đặt".equals(tour.getTrangThai())) {
                tourItems.add(new TourComboItem(
                    tour.getMaTour(), 
                    tour.getTenTour(), 
                    tour.getNgayBatDau(), 
                    tour.getNgayKetThuc(), 
                    tour.getGiaTour(), 
                    tour.getSoLuongKhachToiDa()
                ));
            }
        }
        tourComboBox = new JComboBox<>(tourItems.toArray(new TourComboItem[0]));
        tourComboBox.addActionListener(e -> updateTotalPrice());
        formPanel.add(tourComboBox, gbc);
        
        // Số lượng người
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Số lượng người:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        SpinnerNumberModel soLuongModel = new SpinnerNumberModel(1, 1, 100, 1); // Default 1, min 1, max 100
        soLuongNguoiSpinner = new JSpinner(soLuongModel);
        soLuongNguoiSpinner.addChangeListener(e -> updateTotalPrice());
        formPanel.add(soLuongNguoiSpinner, gbc);
        
        // Tổng tiền
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Tổng tiền:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        tongTienLabel = new JLabel("0 VNĐ");
        tongTienLabel.setFont(new Font("Arial", Font.BOLD, 14));
        tongTienLabel.setForeground(new Color(30, 55, 153));
        formPanel.add(tongTienLabel, gbc);
        
        // Trạng thái
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Trạng thái:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        String[] trangThaiOptions = {"Chờ xác nhận", "Đã xác nhận", "Đã hủy"};
        trangThaiComboBox = new JComboBox<>(trangThaiOptions);
        formPanel.add(trangThaiComboBox, gbc);
        
        // Ghi chú
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Ghi chú:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        ghiChuArea = new JTextArea(3, 20);
        ghiChuArea.setLineWrap(true);
        ghiChuArea.setWrapStyleWord(true);
        JScrollPane ghiChuScrollPane = new JScrollPane(ghiChuArea);
        formPanel.add(ghiChuScrollPane, gbc);
        
        // Yêu cầu đặc biệt
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(new JLabel("Yêu cầu đặc biệt:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        yeuCauDacBietArea = new JTextArea(3, 20);
        yeuCauDacBietArea.setLineWrap(true);
        yeuCauDacBietArea.setWrapStyleWord(true);
        JScrollPane yeuCauScrollPane = new JScrollPane(yeuCauDacBietArea);
        formPanel.add(yeuCauScrollPane, gbc);
        
        // Nhân viên xử lý
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(new JLabel("Nhân viên xử lý:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        // Load danh sách nhân viên
        List<NhanVien> nhanViens = nhanVienService.layDanhSach();
        List<StaffComboItem> staffItems = new ArrayList<>();
        for (NhanVien nv : nhanViens) {
            staffItems.add(new StaffComboItem(nv.getMaNV(), nv.getHoTen(), nv.getChucVu()));
        }
        nhanVienComboBox = new JComboBox<>(staffItems.toArray(new StaffComboItem[0]));
        
        // Set current staff as default
        for (int i = 0; i < nhanVienComboBox.getItemCount(); i++) {
            if (nhanVienComboBox.getItemAt(i).getId() == currentStaffId) {
                nhanVienComboBox.setSelectedIndex(i);
                break;
            }
        }
        
        formPanel.add(nhanVienComboBox, gbc);
        
        // Ngày đặt
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Ngày đặt:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        JLabel ngayDatLabel = new JLabel(dateFormat.format(new Date()));
        formPanel.add(ngayDatLabel, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        CustomButton saveButton = new CustomButton("Lưu");
        saveButton.setStylePrimary();
        
        CustomButton cancelButton = new CustomButton("Hủy");
        cancelButton.setStyleDanger();
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        // Add to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add to dialog
        getContentPane().add(mainPanel);
        
        // Set dialog properties
        pack();
        setLocationRelativeTo(getOwner());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        
        // Update total price initially
        updateTotalPrice();
        
        // Add action listeners
        saveButton.addActionListener(e -> saveBooking());
        
        cancelButton.addActionListener(e -> dispose());
    }
    
    private void updateTotalPrice() {
        TourComboItem selectedTour = (TourComboItem) tourComboBox.getSelectedItem();
        if (selectedTour != null) {
            int soLuong = (Integer) soLuongNguoiSpinner.getValue();
            BigDecimal giaTour = selectedTour.getGiaTour();
            BigDecimal tongTien = giaTour.multiply(BigDecimal.valueOf(soLuong));
            
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            tongTienLabel.setText(currencyFormat.format(tongTien));
        }
    }
    
    private void loadBookingData() {
        if (datTour != null) {
            // Set khách hàng
            for (int i = 0; i < khachHangComboBox.getItemCount(); i++) {
                if (khachHangComboBox.getItemAt(i).getId() == datTour.getMaKH()) {
                    khachHangComboBox.setSelectedIndex(i);
                    break;
                }
            }
            
            // Set tour
            for (int i = 0; i < tourComboBox.getItemCount(); i++) {
                if (tourComboBox.getItemAt(i).getId() == datTour.getMaTour()) {
                    tourComboBox.setSelectedIndex(i);
                    break;
                }
            }
            
            // Nếu là chỉnh sửa, thêm tour hiện tại vào combobox nếu nó không có trong danh sách
            if (isEditMode && tourComboBox.getSelectedIndex() == -1) {
                Tour tour = tourService.timTheoMa(datTour.getMaTour());
                if (tour != null) {
                    TourComboItem item = new TourComboItem(
                        tour.getMaTour(),
                        tour.getTenTour(),
                        tour.getNgayBatDau(),
                        tour.getNgayKetThuc(),
                        tour.getGiaTour(),
                        tour.getSoLuongKhachToiDa()
                    );
                    ((DefaultComboBoxModel<TourComboItem>) tourComboBox.getModel()).addElement(item);
                    tourComboBox.setSelectedItem(item);
                }
            }
            
            // Set số lượng người
            soLuongNguoiSpinner.setValue(datTour.getSoLuongNguoi());
            
            // Set trạng thái
            trangThaiComboBox.setSelectedItem(datTour.getTrangThai());
            
            // Set ghi chú và yêu cầu đặc biệt
            if (datTour.getGhiChu() != null) {
                ghiChuArea.setText(datTour.getGhiChu());
            }
            
            if (datTour.getYeuCauDacBiet() != null) {
                yeuCauDacBietArea.setText(datTour.getYeuCauDacBiet());
            }
            
            // Set nhân viên
            for (int i = 0; i < nhanVienComboBox.getItemCount(); i++) {
                if (nhanVienComboBox.getItemAt(i).getId() == datTour.getMaNV()) {
                    nhanVienComboBox.setSelectedIndex(i);
                    break;
                }
            }
            
            // Update tổng tiền
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            tongTienLabel.setText(currencyFormat.format(datTour.getTongTien()));
        }
    }
    
    private void saveBooking() {
        try {
            // Validate input
            CustomerComboItem selectedCustomer = (CustomerComboItem) khachHangComboBox.getSelectedItem();
            if (selectedCustomer == null) {
                throw new BusinessLogicException("Vui lòng chọn khách hàng");
            }
            
            TourComboItem selectedTour = (TourComboItem) tourComboBox.getSelectedItem();
            if (selectedTour == null) {
                throw new BusinessLogicException("Vui lòng chọn tour");
            }
            
            int soLuongNguoi = (Integer) soLuongNguoiSpinner.getValue();
            if (soLuongNguoi <= 0) {
                throw new BusinessLogicException("Số lượng người phải lớn hơn 0");
            }
            
            StaffComboItem selectedStaff = (StaffComboItem) nhanVienComboBox.getSelectedItem();
            if (selectedStaff == null) {
                throw new BusinessLogicException("Vui lòng chọn nhân viên xử lý");
            }
            
            String trangThai = (String) trangThaiComboBox.getSelectedItem();
            String ghiChu = ghiChuArea.getText().trim();
            String yeuCauDacBiet = yeuCauDacBietArea.getText().trim();
            
            // Tính tổng tiền
            BigDecimal giaTour = selectedTour.getGiaTour();
            BigDecimal tongTien = giaTour.multiply(BigDecimal.valueOf(soLuongNguoi));
            
            if (isEditMode) {
                // Update existing booking
                datTour.setMaKH(selectedCustomer.getId());
                datTour.setMaTour(selectedTour.getId());
                datTour.setSoLuongNguoi(soLuongNguoi);
                datTour.setTongTien(tongTien);
                datTour.setTrangThai(trangThai);
                datTour.setGhiChu(ghiChu);
                datTour.setYeuCauDacBiet(yeuCauDacBiet);
                datTour.setMaNV(selectedStaff.getId());
                
                boolean success = datTourService.sua(datTour);
                if (success) {
                    saveSuccess = true;
                    JOptionPane.showMessageDialog(this, 
                        "Cập nhật thông tin đặt tour thành công", 
                        "Thành Công", 
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Không thể cập nhật thông tin đặt tour", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Create new booking
                DatTour newBooking = new DatTour();
                newBooking.setMaKH(selectedCustomer.getId());
                newBooking.setMaTour(selectedTour.getId());
                newBooking.setNgayDat(new Date());
                newBooking.setSoLuongNguoi(soLuongNguoi);
                newBooking.setTongTien(tongTien);
                newBooking.setTrangThai(trangThai);
                newBooking.setGhiChu(ghiChu);
                newBooking.setYeuCauDacBiet(yeuCauDacBiet);
                newBooking.setMaNV(selectedStaff.getId());
                
                boolean success = datTourService.them(newBooking);
                if (success) {
                    saveSuccess = true;
                    JOptionPane.showMessageDialog(this, 
                        "Thêm đặt tour mới thành công", 
                        "Thành Công", 
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Không thể thêm đặt tour mới", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (BusinessLogicException e) {
            JOptionPane.showMessageDialog(this, 
                e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Đã xảy ra lỗi: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isSaveSuccess() {
        return saveSuccess;
    }
    
    // Helper classes for combo boxes
    private static class CustomerComboItem {
        private int id;
        private String name;
        private String phone;
        
        public CustomerComboItem(int id, String name, String phone) {
            this.id = id;
            this.name = name;
            this.phone = phone;
        }
        
        public int getId() {
            return id;
        }
        
        @Override
        public String toString() {
            return name + " (" + phone + ")";
        }
    }
    
    private static class TourComboItem {
        private int id;
        private String name;
        private Date startDate;
        private Date endDate;
        private BigDecimal giaTour;
        private int maxGuests;
        
        public TourComboItem(int id, String name, Date startDate, Date endDate, BigDecimal giaTour, int maxGuests) {
            this.id = id;
            this.name = name;
            this.startDate = startDate;
            this.endDate = endDate;
            this.giaTour = giaTour;
            this.maxGuests = maxGuests;
        }
        
        public int getId() {
            return id;
        }
        
        public BigDecimal getGiaTour() {
            return giaTour;
        }
        
        @Override
        public String toString() {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            return name + " (" + df.format(startDate) + " - " + df.format(endDate) + ")";
        }
    }
    
    private static class StaffComboItem {
        private int id;
        private String name;
        private String position;
        
        public StaffComboItem(int id, String name, String position) {
            this.id = id;
            this.name = name;
            this.position = position;
        }
        
        public int getId() {
            return id;
        }
        
        @Override
        public String toString() {
            return name + " - " + position;
        }
    }
}