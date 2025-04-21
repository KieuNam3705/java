package com.tourmanagement.ui.frame;

import com.tourmanagement.ui.components.*;
import com.tourmanagement.entity.TaiKhoan;
import com.tourmanagement.entity.KhachHang;
import com.tourmanagement.service.ServiceFactory;
import com.tourmanagement.service.interfaces.IKhachHangService;
import com.tourmanagement.service.interfaces.ITourService;
import com.tourmanagement.service.interfaces.IDatTourService;
import com.tourmanagement.entity.Tour;
import com.tourmanagement.entity.DatTour;
import com.tourmanagement.ui.frame.LoginFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class CustomerMainFrame extends JFrame {
    private TaiKhoan account;
    private KhachHang customer;
    
    private ServiceFactory serviceFactory;
    private IKhachHangService khachHangService;
    private ITourService tourService;
    private IDatTourService datTourService;
    
    private JPanel contentPanel;
    private JTabbedPane tabbedPane;
    
    public CustomerMainFrame(TaiKhoan account) {
        this.account = account;
        this.serviceFactory = ServiceFactory.getInstance();
        this.khachHangService = serviceFactory.getKhachHangService();
        this.tourService = serviceFactory.getTourService();
        this.datTourService = serviceFactory.getDatTourService();
        
        // Load customer info
        if (account.getMaKH() != null) {
            this.customer = khachHangService.timTheoMa(account.getMaKH());
        }
        
        setTitle("Hệ Thống Đặt Tour Du Lịch");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        setSize(1200, 800);
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Create header
        JPanel headerPanel = createHeaderPanel();
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Create tabs
        JPanel availableToursPanel = createAvailableToursPanel();
        JPanel myBookingsPanel = createMyBookingsPanel();
        JPanel profilePanel = createProfilePanel();
        
        // Add tabs to tabbed pane
        tabbedPane.addTab("Tours hiện có", availableToursPanel);
        tabbedPane.addTab("Đặt tour của tôi", myBookingsPanel);
        tabbedPane.addTab("Thông tin cá nhân", profilePanel);
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Add main panel to frame
        getContentPane().add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 55, 153));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Title
        JLabel titleLabel = new JLabel("HỆ THỐNG ĐẶT TOUR DU LỊCH");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        // User info
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        
        JLabel welcomeLabel = new JLabel();
        if (customer != null) {
            welcomeLabel.setText("Xin chào, " + customer.getHoTen());
        } else {
            welcomeLabel.setText("Xin chào, Khách hàng");
        }
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        welcomeLabel.setForeground(Color.WHITE);
        
        CustomButton logoutButton = new CustomButton("Đăng Xuất");
        logoutButton.setStyleDanger();
        logoutButton.addActionListener(e -> logout());
        
        userPanel.add(welcomeLabel);
        userPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        userPanel.add(logoutButton);
        
        // Add components to header
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createAvailableToursPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(245, 246, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(245, 246, 250));
        
        SearchPanel tourSearchPanel = new SearchPanel();
        
        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(new Color(245, 246, 250));
        
        JLabel filterLabel = new JLabel("Lọc theo loại tour:");
        String[] tourTypes = {"Tất cả", "Du lịch trong nước", "Du lịch nước ngoài", "Tour nghỉ dưỡng", "Tour khám phá", "Tour mạo hiểm"};
        JComboBox<String> typeFilter = new JComboBox<>(tourTypes);
        
        filterPanel.add(filterLabel);
        filterPanel.add(typeFilter);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 246, 250));
        topPanel.add(tourSearchPanel, BorderLayout.WEST);
        topPanel.add(filterPanel, BorderLayout.EAST);
        
        // Tour list panel
        JPanel tourListPanel = new JPanel(new BorderLayout());
        tourListPanel.setBackground(Color.WHITE);
        tourListPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        
        // Get available tours
        List<Tour> availableTours = tourService.layDanhSachTheoTrangThai("Đang mở");
        
        // Create table
        String[] columnNames = {"Mã Tour", "Tên Tour", "Ngày bắt đầu", "Ngày kết thúc", "Số ngày", "Giá", "Loại Tour"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Add data to table
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (Tour tour : availableTours) {
            Object[] rowData = {
                tour.getMaTour(),
                tour.getTenTour(),
                dateFormat.format(tour.getNgayBatDau()),
                dateFormat.format(tour.getNgayKetThuc()),
                tour.getSoNgay(),
                tour.getGiaTour() + " VNĐ",
                tour.getLoaiTour()
            };
            model.addRow(rowData);
        }
        
        CustomTable tourTable = new CustomTable(model);
        JScrollPane scrollPane = new JScrollPane(tourTable);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        CustomButton viewDetailsButton = new CustomButton("Xem Chi Tiết");
        viewDetailsButton.setStyleSecondary();
        
        CustomButton bookTourButton = new CustomButton("Đặt Tour");
        bookTourButton.setStylePrimary();
        
        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(bookTourButton);
        
        tourListPanel.add(scrollPane, BorderLayout.CENTER);
        tourListPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add action listeners
        viewDetailsButton.addActionListener(e -> {
            int selectedRow = tourTable.getSelectedRow();
            if (selectedRow != -1) {
                int tourId = (int) tourTable.getValueAt(selectedRow, 0);
                showTourDetails(tourId);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn một tour để xem chi tiết.", 
                    "Chưa Chọn Tour", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        bookTourButton.addActionListener(e -> {
            int selectedRow = tourTable.getSelectedRow();
            if (selectedRow != -1) {
                int tourId = (int) tourTable.getValueAt(selectedRow, 0);
                showBookTourDialog(tourId);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn một tour để đặt.", 
                    "Chưa Chọn Tour", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        tourSearchPanel.addSearchListener(e -> {
            String searchText = tourSearchPanel.getSearchText().trim();
            if (searchText.isEmpty()) {
                // Reset table
                updateAvailableTours(availableTours);
            } else {
                // Search tours
                List<Tour> searchResults = tourService.timTheoTen(searchText);
                // Filter for only available tours
                searchResults.removeIf(tour -> !"Đang mở".equals(tour.getTrangThai()));
                updateAvailableTours(searchResults);
            }
        });
        
        typeFilter.addActionListener(e -> {
            String selectedType = (String) typeFilter.getSelectedItem();
            if ("Tất cả".equals(selectedType)) {
                updateAvailableTours(availableTours);
            } else {
                List<Tour> filteredTours = tourService.layDanhSachTheoLoai(selectedType);
                // Filter for only available tours
                filteredTours.removeIf(tour -> !"Đang mở".equals(tour.getTrangThai()));
                updateAvailableTours(filteredTours);
            }
        });
        
        // Add components to panel
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(tourListPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void updateAvailableTours(List<Tour> tours) {
           // Tạo model cho bảng
   String[] columnNames = {"Mã Tour", "Tên Tour", "Ngày bắt đầu", "Ngày kết thúc", "Số ngày", "Giá", "Loại Tour"};
   DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
       @Override
       public boolean isCellEditable(int row, int column) {
           return false;
       }
   };
   
   // Format date
   SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
   
   // Thêm dữ liệu vào model
   for (Tour tour : tours) {
       Object[] rowData = {
           tour.getMaTour(),
           tour.getTenTour(),
           dateFormat.format(tour.getNgayBatDau()),
           dateFormat.format(tour.getNgayKetThuc()),
           tour.getSoNgay(),
           tour.getGiaTour() + " VNĐ",
           tour.getLoaiTour()
       };
       model.addRow(rowData);
   }
   
   // Tìm bảng trong tab Tours hiện có
   Component[] components = tabbedPane.getComponentAt(0).getComponents();
   JPanel tourListPanel = null;
   
   // Tìm tour list panel
   for (Component comp : components) {
       if (comp instanceof JPanel && ((JPanel) comp).getComponentCount() > 0) {
           Component innerComp = ((JPanel) comp).getComponent(0);
           if (innerComp instanceof JScrollPane) {
               tourListPanel = (JPanel) comp;
               break;
           }
       }
   }
   
   if (tourListPanel != null) {
       // Lấy scroll pane chứa bảng
       JScrollPane scrollPane = (JScrollPane) tourListPanel.getComponent(0);
       CustomTable tourTable = (CustomTable) scrollPane.getViewport().getView();
       
       // Cập nhật model cho bảng
       tourTable.setModel(model);
   }
    }
    
    private JPanel createMyBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(245, 246, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        CustomLabel titleLabel = new CustomLabel("Các Tour Đã Đặt");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Bookings panel
        JPanel bookingsPanel = new JPanel(new BorderLayout());
        bookingsPanel.setBackground(Color.WHITE);
        bookingsPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        
        // Get customer bookings
        List<DatTour> bookings = null;
        if (customer != null) {
            bookings = datTourService.layDanhSachTheoKhachHang(customer.getMaKH());
        }
        
        // Create table
        String[] columnNames = {"Mã Đặt Tour", "Tour", "Ngày Đặt", "Số Người", "Tổng Tiền", "Trạng Thái"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Add data to table
        if (bookings != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            for (DatTour booking : bookings) {
                // Get tour name
                Tour tour = tourService.timTheoMa(booking.getMaTour());
                String tourName = (tour != null) ? tour.getTenTour() : "Unknown";
                
                Object[] rowData = {
                    booking.getMaDatTour(),
                    tourName,
                    dateFormat.format(booking.getNgayDat()),
                    booking.getSoLuongNguoi(),
                    booking.getTongTien() + " VNĐ",
                    booking.getTrangThai()
                };
                model.addRow(rowData);
            }
        }
        
        CustomTable bookingTable = new CustomTable(model);
        JScrollPane scrollPane = new JScrollPane(bookingTable);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        
        CustomButton viewDetailsButton = new CustomButton("Xem Chi Tiết");
        viewDetailsButton.setStyleSecondary();
        
        CustomButton cancelBookingButton = new CustomButton("Hủy Đặt Tour");
        cancelBookingButton.setStyleDanger();
        
        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(cancelBookingButton);
        
        bookingsPanel.add(scrollPane, BorderLayout.CENTER);
        bookingsPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add action listeners
        viewDetailsButton.addActionListener(e -> {
            int selectedRow = bookingTable.getSelectedRow();
            if (selectedRow != -1) {
                int bookingId = (int) bookingTable.getValueAt(selectedRow, 0);
                showBookingDetails(bookingId);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn một đặt tour để xem chi tiết.", 
                    "Chưa Chọn Đặt Tour", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        cancelBookingButton.addActionListener(e -> {
            int selectedRow = bookingTable.getSelectedRow();
            if (selectedRow != -1) {
                int bookingId = (int) bookingTable.getValueAt(selectedRow, 0);
                String status = (String) bookingTable.getValueAt(selectedRow, 5);
                
                if ("Đã hủy".equals(status)) {
                    JOptionPane.showMessageDialog(this, 
                        "Đặt tour này đã được hủy trước đó.", 
                        "Không Thể Hủy", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if ("Hoàn thành".equals(status) || "Đang diễn ra".equals(status)) {
                    JOptionPane.showMessageDialog(this, 
                        "Không thể hủy đặt tour đã hoàn thành hoặc đang diễn ra.", 
                        "Không Thể Hủy", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                cancelBooking(bookingId);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn một đặt tour để hủy.", 
                    "Chưa Chọn Đặt Tour", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Add components to panel
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(bookingsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(245, 246, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        CustomLabel titleLabel = new CustomLabel("Thông Tin Cá Nhân");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Profile panel
        RoundedPanel profilePanel = new RoundedPanel(15, Color.WHITE);
        profilePanel.setLayout(new BorderLayout(20, 20));
        profilePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Profile form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Add profile fields
        if (customer != null) {
            // Họ tên
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.EAST;
            formPanel.add(new JLabel("Họ tên:"), gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;
            JTextField hoTenField = new JTextField(customer.getHoTen(), 25);
            hoTenField.setEditable(false);
            formPanel.add(hoTenField, gbc);
            
            // CMND
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.EAST;
            formPanel.add(new JLabel("CMND/CCCD:"), gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.WEST;
            JTextField cmndField = new JTextField(customer.getCmnd(), 20);
            cmndField.setEditable(false);
            formPanel.add(cmndField, gbc);
            
            // Số điện thoại
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.anchor = GridBagConstraints.EAST;
            formPanel.add(new JLabel("Số điện thoại:"), gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 2;
            gbc.anchor = GridBagConstraints.WEST;
            JTextField phoneField = new JTextField(customer.getSoDienThoai(), 20);
            formPanel.add(phoneField, gbc);
            
            // Email
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.anchor = GridBagConstraints.EAST;
            formPanel.add(new JLabel("Email:"), gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 3;
            gbc.anchor = GridBagConstraints.WEST;
            JTextField emailField = new JTextField(customer.getEmail(), 25);
            formPanel.add(emailField, gbc);
            
            // Địa chỉ
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.anchor = GridBagConstraints.EAST;
            formPanel.add(new JLabel("Địa chỉ:"), gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 4;
            gbc.anchor = GridBagConstraints.WEST;
            JTextField addressField = new JTextField(customer.getDiaChi(), 25);
            formPanel.add(addressField, gbc);
            
            // Ngày sinh
            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.anchor = GridBagConstraints.EAST;
            formPanel.add(new JLabel("Ngày sinh:"), gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 5;
            gbc.anchor = GridBagConstraints.WEST;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String birthDate = (customer.getNgaySinh() != null) ? 
                               dateFormat.format(customer.getNgaySinh()) : "";
            JTextField birthDateField = new JTextField(birthDate, 15);
            formPanel.add(birthDateField, gbc);
            
            // Giới tính
            gbc.gridx = 0;
            gbc.gridy = 6;
            gbc.anchor = GridBagConstraints.EAST;
            formPanel.add(new JLabel("Giới tính:"), gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 6;
            gbc.anchor = GridBagConstraints.WEST;
            String[] genders = {"Nam", "Nữ", "Khác"};
            JComboBox<String> genderComboBox = new JComboBox<>(genders);
            if (customer.getGioiTinh() != null) {
                genderComboBox.setSelectedItem(customer.getGioiTinh());
            }
            formPanel.add(genderComboBox, gbc);
            
            // Quốc tịch
            gbc.gridx = 0;
            gbc.gridy = 7;
            gbc.anchor = GridBagConstraints.EAST;
            formPanel.add(new JLabel("Quốc tịch:"), gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 7;
            gbc.anchor = GridBagConstraints.WEST;
            JTextField nationalityField = new JTextField(customer.getQuocTich(), 20);
            formPanel.add(nationalityField, gbc);
            
            // Change password section
            gbc.gridx = 0;
            gbc.gridy = 8;
            gbc.gridwidth = 2;
            JSeparator separator = new JSeparator();
            formPanel.add(separator, gbc);
            
            gbc.gridx = 0;
            gbc.gridy = 9;
            gbc.gridwidth = 2;
            JLabel passwordLabel = new JLabel("Đổi Mật Khẩu", JLabel.CENTER);
            passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
            formPanel.add(passwordLabel, gbc);
            
            gbc.gridwidth = 1;
            
            // Old password
            gbc.gridx = 0;
            gbc.gridy = 10;
            gbc.anchor = GridBagConstraints.EAST;
            formPanel.add(new JLabel("Mật khẩu cũ:"), gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 10;
            gbc.anchor = GridBagConstraints.WEST;
            JPasswordField oldPasswordField = new JPasswordField(20);
            formPanel.add(oldPasswordField, gbc);
            
            // New password
            gbc.gridx = 0;
            gbc.gridy = 11;
            gbc.anchor = GridBagConstraints.EAST;
            formPanel.add(new JLabel("Mật khẩu mới:"), gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 11;
            gbc.anchor = GridBagConstraints.WEST;
            JPasswordField newPasswordField = new JPasswordField(20);
            formPanel.add(newPasswordField, gbc);
            
            // Confirm new password
            gbc.gridx = 0;
            gbc.gridy = 12;
            gbc.anchor = GridBagConstraints.EAST;
            formPanel.add(new JLabel("Xác nhận mật khẩu mới:"), gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 12;
            gbc.anchor = GridBagConstraints.WEST;
            JPasswordField confirmPasswordField = new JPasswordField(20);
            formPanel.add(confirmPasswordField, gbc);
        }
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        
        CustomButton saveButton = new CustomButton("Lưu Thay Đổi");
        saveButton.setStylePrimary();
        
        buttonPanel.add(saveButton);
        
        // Add action listener to save button
        saveButton.addActionListener(e -> {
            // Here you would implement logic to save profile changes
            JOptionPane.showMessageDialog(this, 
                "Chức năng cập nhật thông tin sẽ được triển khai", 
                "Thông Báo", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Add panels to profile panel
        profilePanel.add(formPanel, BorderLayout.CENTER);
        profilePanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add components to main panel
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(profilePanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void showTourDetails(int tourId) {
        // Implement tour details dialog or panel
        JOptionPane.showMessageDialog(this, 
            "Chức năng xem chi tiết tour sẽ được triển khai", 
            "Thông Báo", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showBookTourDialog(int tourId) {
        // Implement booking dialog
        JOptionPane.showMessageDialog(this, 
            "Chức năng đặt tour sẽ được triển khai", 
            "Thông Báo", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showBookingDetails(int bookingId) {
        // Implement booking details dialog or panel
        JOptionPane.showMessageDialog(this, 
            "Chức năng xem chi tiết đặt tour sẽ được triển khai", 
            "Thông Báo", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void cancelBooking(int bookingId) {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn hủy đặt tour này?",
            "Xác nhận hủy",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = datTourService.capNhatTrangThai(bookingId, "Đã hủy");
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Hủy đặt tour thành công!",
                        "Thành Công",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Refresh the bookings tab
                    tabbedPane.setComponentAt(1, createMyBookingsPanel());
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Không thể hủy đặt tour. Vui lòng thử lại sau.",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Đã xảy ra lỗi: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn đăng xuất?",
            "Xác nhận đăng xuất",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        }
    }
}