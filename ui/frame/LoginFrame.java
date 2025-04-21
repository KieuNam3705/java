
package com.tourmanagement.ui.frame;

import com.tourmanagement.ui.components.*;
import com.tourmanagement.ui.dialogs.LoginDialog;
import com.tourmanagement.entity.TaiKhoan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JPanel mainPanel;
    private CustomButton loginButton;
    private CustomButton registerButton;
    
    public LoginFrame() {
        setTitle("Hệ Thống Quản Lý Tour Du Lịch");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        setSize(800, 500);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void initComponents() {
        // Main panel with background
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 242, 245));
        
        // Logo and title panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 55, 153));
        
        CustomLabel titleLabel = new CustomLabel("HỆ THỐNG QUẢN LÝ TOUR DU LỊCH");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Content panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(240, 242, 245));
        
        // Welcome panel
        RoundedPanel welcomePanel = new RoundedPanel(15, Color.WHITE);
        welcomePanel.setLayout(new BorderLayout());
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        CustomLabel welcomeLabel = new CustomLabel("Chào mừng đến với Hệ thống Quản lý Tour");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(new Color(30, 55, 153));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
        descriptionPanel.setBackground(Color.WHITE);
        descriptionPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        
        JLabel descLabel1 = new JLabel("Hệ thống quản lý tour du lịch toàn diện");
        descLabel1.setFont(new Font("Arial", Font.PLAIN, 16));
        descLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel descLabel2 = new JLabel("Vui lòng đăng nhập để tiếp tục");
        descLabel2.setFont(new Font("Arial", Font.PLAIN, 16));
        descLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        descriptionPanel.add(descLabel1);
        descriptionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        descriptionPanel.add(descLabel2);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        
        loginButton = new CustomButton("Đăng Nhập");
        loginButton.setStylePrimary();
        loginButton.setPreferredSize(new Dimension(150, 40));
        
        registerButton = new CustomButton("Đăng Ký");
        registerButton.setStyleSecondary();
        registerButton.setPreferredSize(new Dimension(150, 40));
        
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPanel.add(registerButton);
        
        welcomePanel.add(welcomeLabel, BorderLayout.NORTH);
        welcomePanel.add(descriptionPanel, BorderLayout.CENTER);
        welcomePanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add welcome panel to content panel
        contentPanel.add(welcomePanel);
        
        // Add panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Add main panel to frame
        getContentPane().add(mainPanel);
        
        // Add listeners
        loginButton.addActionListener(e -> showLoginDialog());
        registerButton.addActionListener(e -> showRegisterFrame());
    }
    
    private void showLoginDialog() {
        LoginDialog dialog = new LoginDialog(this);
        dialog.setVisible(true);
        
        if (dialog.isLoginSuccess()) {
            TaiKhoan account = dialog.getLoggedInAccount();
            openAppropriateFrame(account);
        }
    }
    
    private void showRegisterFrame() {
        RegisterFrame registerFrame = new RegisterFrame();
        registerFrame.setVisible(true);
        this.setVisible(false);
    }
    
    private void openAppropriateFrame(TaiKhoan account) {
        this.setVisible(false);
        
        switch (account.getLoaiTaiKhoan()) {
            case "Admin":
                AdminMainFrame adminFrame = new AdminMainFrame(account);
                adminFrame.setVisible(true);
                break;
            case "Nhân viên":
                StaffMainFrame staffFrame = new StaffMainFrame(account);
                staffFrame.setVisible(true);
                break;
            case "Khách hàng":
                CustomerMainFrame customerFrame = new CustomerMainFrame(account);
                customerFrame.setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(this,
                    "Loại tài khoản không hợp lệ.",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
                this.setVisible(true);
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
        });
    }
}