package com.tourmanagement.ui.frame;

import com.tourmanagement.ui.components.*;
import com.tourmanagement.service.ServiceFactory;
import com.tourmanagement.service.interfaces.IKhachHangService;
import com.tourmanagement.service.interfaces.ITaiKhoanService;
import com.tourmanagement.entity.KhachHang;
import com.tourmanagement.entity.TaiKhoan;
import com.tourmanagement.exception.BusinessLogicException;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterFrame extends JFrame {
    private IKhachHangService khachHangService;
    private ITaiKhoanService taiKhoanService;
    
    private CustomTextField hoTenField;
    private CustomTextField cmndField;
    private CustomTextField soDienThoaiField;
    private CustomTextField emailField;
    private CustomTextField diaChiField;
    private JFormattedTextField ngaySinhField;
    private JComboBox<String> gioiTinhComboBox;
    private CustomTextField quocTichField;
    private CustomTextField tenDangNhapField;
    private CustomPasswordField matKhauField;
    private CustomPasswordField xacNhanMatKhauField;
    
    public RegisterFrame() {
        this.khachHangService = ServiceFactory.getInstance().getKhachHangService();
        this.taiKhoanService = ServiceFactory.getInstance().getTaiKhoanService();
        
        setTitle("Đăng Ký Tài Khoản");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        setSize(800, 700);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void initComponents() {
        // Main panel with background
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 242, 245));
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 55, 153));
        
        CustomLabel titleLabel = new CustomLabel("ĐĂNG KÝ TÀI KHOẢN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Content panel (Registration form)
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(240, 242, 245));
        
        // Registration form panel
        RoundedPanel formPanel = new RoundedPanel(15, Color.WHITE);
        formPanel.setLayout(new BorderLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Form title
        CustomLabel formTitleLabel = new CustomLabel("Thông tin đăng ký");
        formTitleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        formTitleLabel.setForeground(new Color(30, 55, 153));
        
        // Form fields panel
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Họ tên
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        fieldsPanel.add(new JLabel("Họ tên:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        hoTenField = new CustomTextField(25);
        fieldsPanel.add(hoTenField, gbc);
        
        // CMND/CCCD
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        fieldsPanel.add(new JLabel("CMND/CCCD:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        cmndField = new CustomTextField(15);
        fieldsPanel.add(cmndField, gbc);
        
        // Số điện thoại
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        fieldsPanel.add(new JLabel("Số điện thoại:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        soDienThoaiField = new CustomTextField(15);
        fieldsPanel.add(soDienThoaiField, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        fieldsPanel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        emailField = new CustomTextField(25);
        fieldsPanel.add(emailField, gbc);
        
        // Địa chỉ
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        fieldsPanel.add(new JLabel("Địa chỉ:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        diaChiField = new CustomTextField(25);
        fieldsPanel.add(diaChiField, gbc);
        
        // Ngày sinh
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        fieldsPanel.add(new JLabel("Ngày sinh:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        ngaySinhField = new JFormattedTextField(dateFormat);
        ngaySinhField.setColumns(10);
        fieldsPanel.add(ngaySinhField, gbc);
        
        // Giới tính
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        fieldsPanel.add(new JLabel("Giới tính:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        String[] gioiTinhOptions = {"Nam", "Nữ", "Khác"};
        gioiTinhComboBox = new JComboBox<>(gioiTinhOptions);
        fieldsPanel.add(gioiTinhComboBox, gbc);
        
        // Quốc tịch
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        fieldsPanel.add(new JLabel("Quốc tịch:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        quocTichField = new CustomTextField(15);
        quocTichField.setText("Việt Nam"); // Default value
        fieldsPanel.add(quocTichField, gbc);
        
        // Account section separator
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 8, 15, 8);
        JSeparator separator = new JSeparator();
        fieldsPanel.add(separator, gbc);
        
        // Account section label
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 8, 15, 8);
        JLabel accountLabel = new JLabel("Thông tin tài khoản", JLabel.LEFT);
        accountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        accountLabel.setForeground(new Color(30, 55, 153));
        fieldsPanel.add(accountLabel, gbc);
        
        // Reset insets and gridwidth
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridwidth = 1;
        
        // Tên đăng nhập
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        fieldsPanel.add(new JLabel("Tên đăng nhập:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        tenDangNhapField = new CustomTextField(20);
        fieldsPanel.add(tenDangNhapField, gbc);
        
        // Mật khẩu
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        fieldsPanel.add(new JLabel("Mật khẩu:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        matKhauField = new CustomPasswordField(20);
        fieldsPanel.add(matKhauField, gbc);
        
        // Xác nhận mật khẩu
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        fieldsPanel.add(new JLabel("Xác nhận mật khẩu:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        xacNhanMatKhauField = new CustomPasswordField(20);
        fieldsPanel.add(xacNhanMatKhauField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        CustomButton registerButton = new CustomButton("Đăng Ký");
        registerButton.setStylePrimary();
        registerButton.setPreferredSize(new Dimension(120, 40));
        
        CustomButton cancelButton = new CustomButton("Quay Lại");
        cancelButton.setStyleSecondary();
        cancelButton.setPreferredSize(new Dimension(120, 40));
        
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        
        // Add components to form panel
        formPanel.add(formTitleLabel, BorderLayout.NORTH);
        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add form panel to content panel
        contentPanel.add(formPanel);
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Add main panel to frame
        getContentPane().add(mainPanel);
        
        // Add action listeners
        registerButton.addActionListener(e -> registerAccount());
        cancelButton.addActionListener(e -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            this.dispose();
        });
    }
    
    private void registerAccount() {
        try {
            // Validate form inputs
            validateFormInputs();
            
            // Create customer object
            KhachHang khachHang = new KhachHang();
            khachHang.setHoTen(hoTenField.getText().trim());
            khachHang.setCmnd(cmndField.getText().trim());
            khachHang.setSoDienThoai(soDienThoaiField.getText().trim());
            khachHang.setEmail(emailField.getText().trim());
            khachHang.setDiaChi(diaChiField.getText().trim());
            
            // Parse date
            try {
                if (!ngaySinhField.getText().trim().isEmpty()) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date ngaySinh = dateFormat.parse(ngaySinhField.getText());
                    khachHang.setNgaySinh(ngaySinh);
                }
            } catch (ParseException ex) {
                throw new BusinessLogicException("Định dạng ngày sinh không hợp lệ");
            }
            
            khachHang.setGioiTinh((String) gioiTinhComboBox.getSelectedItem());
            khachHang.setQuocTich(quocTichField.getText().trim());
            khachHang.setNgayDangKy(new Date());
            
            // Add customer to database
            boolean customerSuccess = khachHangService.them(khachHang);
            if (!customerSuccess) {
                throw new BusinessLogicException("Không thể đăng ký thông tin khách hàng");
            }
            
            // Get the newly added customer
            KhachHang newCustomer = khachHangService.timTheoCMND(cmndField.getText().trim());
            if (newCustomer == null) {
                throw new BusinessLogicException("Lỗi khi lấy thông tin khách hàng vừa đăng ký");
            }
            
            // Create account
            TaiKhoan taiKhoan = new TaiKhoan();
            taiKhoan.setTenDangNhap(tenDangNhapField.getText().trim());
            taiKhoan.setMatKhau(new String(matKhauField.getPassword()));
            taiKhoan.setLoaiTaiKhoan("Khách hàng");
            taiKhoan.setTrangThai("Hoạt động");
            taiKhoan.setMaKH(newCustomer.getMaKH());
            
            // Add account to database
            boolean accountSuccess = taiKhoanService.them(taiKhoan);
            if (!accountSuccess) {
                throw new BusinessLogicException("Không thể tạo tài khoản");
            }
            
            // Show success message
            JOptionPane.showMessageDialog(this,
                "Đăng ký tài khoản thành công!\nVui lòng đăng nhập để tiếp tục.",
                "Đăng Ký Thành Công",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Go back to login frame
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            this.dispose();
            
        } catch (BusinessLogicException ex) {
            JOptionPane.showMessageDialog(this,
                "Lỗi: " + ex.getMessage(),
                "Lỗi Đăng Ký",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Đã xảy ra lỗi: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void validateFormInputs() throws BusinessLogicException {
        // Validate required fields
        if (hoTenField.getText().trim().isEmpty()) {
            throw new BusinessLogicException("Họ tên không được để trống");
        }
        
        String cmnd = cmndField.getText().trim();
        if (cmnd.isEmpty()) {
            throw new BusinessLogicException("CMND/CCCD không được để trống");
        }
        if (!cmnd.matches("^(\\d{9}|\\d{12})$")) {
            throw new BusinessLogicException("CMND/CCCD phải là dãy 9 hoặc 12 chữ số");
        }
        
        // Check if CMND already exists
        if (khachHangService.kiemTraCMNDTonTai(cmnd)) {
            throw new BusinessLogicException("CMND/CCCD đã tồn tại trong hệ thống");
        }
        
        String soDienThoai = soDienThoaiField.getText().trim();
        if (soDienThoai.isEmpty()) {
            throw new BusinessLogicException("Số điện thoại không được để trống");
        }
        if (!soDienThoai.matches("^0\\d{9,10}$")) {
            throw new BusinessLogicException("Số điện thoại không hợp lệ");
        }
        
        String email = emailField.getText().trim();
        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new BusinessLogicException("Định dạng email không hợp lệ");
        }
        
        // Validate account information
        String tenDangNhap = tenDangNhapField.getText().trim();
        if (tenDangNhap.isEmpty()) {
            throw new BusinessLogicException("Tên đăng nhập không được để trống");
        }
        
        // Check if username already exists
        if (taiKhoanService.timTheoTenDangNhap(tenDangNhap) != null) {
            throw new BusinessLogicException("Tên đăng nhập đã tồn tại");
        }
        
        String matKhau = new String(matKhauField.getPassword());
        if (matKhau.isEmpty()) {
            throw new BusinessLogicException("Mật khẩu không được để trống");
        }
        if (matKhau.length() < 6) {
            throw new BusinessLogicException("Mật khẩu phải có ít nhất 6 ký tự");
        }
        
        String xacNhanMatKhau = new String(xacNhanMatKhauField.getPassword());
        if (!matKhau.equals(xacNhanMatKhau)) {
            throw new BusinessLogicException("Mật khẩu và xác nhận mật khẩu không khớp");
        }
    }
}