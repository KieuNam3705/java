package com.tourmanagement.ui.dialogs;

import com.tourmanagement.ui.components.CustomButton;
import com.tourmanagement.ui.components.CustomLabel;
import com.tourmanagement.ui.components.CustomPasswordField;
import com.tourmanagement.ui.components.CustomTextField;
import com.tourmanagement.service.ServiceFactory;
import com.tourmanagement.service.interfaces.ITaiKhoanService;
import com.tourmanagement.entity.TaiKhoan;
import com.tourmanagement.util.PasswordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginDialog extends JDialog {
    private CustomTextField usernameField;
    private CustomPasswordField passwordField;
    private CustomButton loginButton;
    
    private ITaiKhoanService taiKhoanService;
    private TaiKhoan loggedInAccount;
    private boolean loginSuccess = false;
    
    public LoginDialog(Frame parent) {
        super(parent, "User Login", true);
        taiKhoanService = ServiceFactory.getInstance().getTaiKhoanService();
        initComponents();
        pack();
        setSize(450, 500);
        setLocationRelativeTo(getOwner());
    }
    
    private void initComponents() {
        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40));
        mainPanel.setBackground(Color.WHITE);
        
        // Panel avatar
        JPanel avatarPanel = new JPanel(new BorderLayout());
        avatarPanel.setBackground(Color.WHITE);
        
        // Avatar image
        JPanel profileImagePanel = createCircularProfilePanel();
        avatarPanel.add(profileImagePanel, BorderLayout.CENTER);
        
        // Panel tiêu đề
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        
        CustomLabel titleLabel = new CustomLabel("User Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(60, 60, 60));
        titlePanel.add(titleLabel);
        
        // Panel form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Email/Username field with icon
        JPanel emailPanel = createInputFieldWithIcon("✉", "Email Id", false);
        formPanel.add(emailPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Password field with icon
        JPanel passwordPanel = createInputFieldWithIcon("🔒", "Password", true);
        formPanel.add(passwordPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Login button
        loginButton = new CustomButton("Login");
        loginButton.setStylePrimary();
        loginButton.setBackground(new Color(76, 187, 76));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        formPanel.add(loginButton);
        
        // Thêm vào panel chính
        mainPanel.add(avatarPanel, BorderLayout.NORTH);
        mainPanel.add(titlePanel, BorderLayout.CENTER);
        mainPanel.add(formPanel, BorderLayout.SOUTH);
        
        // Thêm vào dialog
        getContentPane().add(mainPanel);
        
        // Thiết lập thuộc tính dialog
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        
        // Thêm listeners
        loginButton.addActionListener(e -> login());
        
        // Set default button
        getRootPane().setDefaultButton(loginButton);
        
        // Thêm key listener cho phím Enter
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login();
                }
            }
        });
    }
    
    // Tạo panel hình tròn cho ảnh profile
    private JPanel createCircularProfilePanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int size = Math.min(getWidth(), getHeight()) - 10;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;
                
                // Vẽ hình tròn làm nền
                g2.setColor(new Color(240, 240, 240));
                g2.fillOval(x, y, size, size);
                
                // Vẽ biểu tượng người dùng
                g2.setColor(new Color(150, 150, 190));
                
                // Đầu
                int headSize = size / 3;
                g2.fillOval(x + size/2 - headSize/2, y + size/4, headSize, headSize);
                
                // Thân
                int bodyWidth = size / 2;
                int bodyHeight = size / 3;
                g2.fillOval(x + size/2 - bodyWidth/2, y + size/2 + headSize/3, bodyWidth, bodyHeight);
                
                g2.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(150, 150);
            }
        };
        
        panel.setOpaque(false);
        return panel;
    }
    
    // Tạo input field có icon
    private JPanel createInputFieldWithIcon(String iconText, String placeholder, boolean isPassword) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        // Label icon
        JLabel iconLabel = new JLabel(iconText);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        iconLabel.setForeground(Color.GRAY);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        
        // Text field
        if (isPassword) {
            passwordField = new CustomPasswordField();
            passwordField.setBorder(null);
            passwordField.setBackground(new Color(245, 245, 245));
            
            // Placeholder text
            passwordField.setText(placeholder);
            passwordField.setEchoChar((char)0);
            passwordField.setForeground(Color.GRAY);
            
            passwordField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (String.valueOf(passwordField.getPassword()).equals(placeholder)) {
                        passwordField.setText("");
                        passwordField.setEchoChar('•');
                        passwordField.setForeground(Color.BLACK);
                    }
                }
                
                @Override
                public void focusLost(FocusEvent e) {
                    if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                        passwordField.setText(placeholder);
                        passwordField.setEchoChar((char)0);
                        passwordField.setForeground(Color.GRAY);
                    }
                }
            });
            
            panel.add(iconLabel, BorderLayout.WEST);
            panel.add(passwordField, BorderLayout.CENTER);
        } else {
            usernameField = new CustomTextField();
            usernameField.setBorder(null);
            usernameField.setBackground(new Color(245, 245, 245));
            
            // Placeholder text
            usernameField.setText(placeholder);
            usernameField.setForeground(Color.GRAY);
            
            usernameField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (usernameField.getText().equals(placeholder)) {
                        usernameField.setText("");
                        usernameField.setForeground(Color.BLACK);
                    }
                }
                
                @Override
                public void focusLost(FocusEvent e) {
                    if (usernameField.getText().isEmpty()) {
                        usernameField.setText(placeholder);
                        usernameField.setForeground(Color.GRAY);
                    }
                }
            });
            
            panel.add(iconLabel, BorderLayout.WEST);
            panel.add(usernameField, BorderLayout.CENTER);
        }
        
        return panel;
    }
    
    private void login() {
        // Lấy thông tin đăng nhập, bỏ qua placeholder text
        String username = usernameField.getText();
        if (username.equals("Email Id")) {
            username = "";
        }
        
        String password = new String(passwordField.getPassword());
        if (password.equals("Password")) {
            password = "";
        }
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập đầy đủ thông tin đăng nhập", 
                "Thông Báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Tìm tài khoản theo tên đăng nhập
            TaiKhoan account = taiKhoanService.timTheoTenDangNhap(username);
            
            if (account == null) {
                JOptionPane.showMessageDialog(this, 
                    "Tên đăng nhập không tồn tại", 
                    "Lỗi Đăng Nhập", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Kiểm tra trạng thái tài khoản
            if (!"Hoạt động".equals(account.getTrangThai())) {
                JOptionPane.showMessageDialog(this, 
                    "Tài khoản đã bị khóa hoặc không hoạt động", 
                    "Lỗi Đăng Nhập", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Kiểm tra mật khẩu
            if (PasswordUtils.verifyPassword(password, account.getMatKhau())) {
                // Đăng nhập thành công
                loggedInAccount = account;
                loginSuccess = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Mật khẩu không chính xác", 
                    "Lỗi Đăng Nhập", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi đăng nhập: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isLoginSuccess() {
        return loginSuccess;
    }
    
    public TaiKhoan getLoggedInAccount() {
        return loggedInAccount;
    }
}