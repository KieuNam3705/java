package com.tourmanagement.ui.main;

import com.tourmanagement.ui.frame.LoginFrame;
import com.tourmanagement.service.ServiceFactory;
import com.tourmanagement.dao.DAOFactory;
import com.tourmanagement.util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Lớp khởi động chính cho ứng dụng quản lý tour du lịch
 */
public class MainApp {
    /**
     * Phương thức main để chạy ứng dụng
     * @param args Tham số dòng lệnh
     */
    public static void main(String[] args) {
        // Đặt look and feel của hệ điều hành
        setSystemLookAndFeel();
        
        // Kiểm tra kết nối cơ sở dữ liệu
        if (!checkDatabaseConnection()) {
            JOptionPane.showMessageDialog(
                null,
                "Không thể kết nối đến cơ sở dữ liệu. Ứng dụng sẽ đóng.",
                "Lỗi Kết Nối Cơ Sở Dữ Liệu",
                JOptionPane.ERROR_MESSAGE
            );
            System.exit(1);
        }
        
        // Khởi tạo các factory
        initializeFactories();
        
        // Hiển thị màn hình đăng nhập
        SwingUtilities.invokeLater(() -> {
            displaySplashScreen();
            LoginFrame loginFrame = new LoginFrame();
            
            loginFrame.setVisible(true);
        });
    }
    
    /**
     * Đặt look and feel hệ thống
     */
    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Không thể đặt look and feel: " + e.getMessage());
        }
    }
    
    /**
     * Kiểm tra kết nối đến cơ sở dữ liệu
     * @return true nếu kết nối thành công, false nếu có lỗi
     */
    private static boolean checkDatabaseConnection() {
        try {
            Connection connection = DBConnection.getConnection();
            if (connection != null && !connection.isClosed()) {
                System.out.println("Kết nối cơ sở dữ liệu thành công!");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Khởi tạo các factory cần thiết cho ứng dụng
     */
    private static void initializeFactories() {
        try {
            // Khởi tạo DAOFactory
            DAOFactory daoFactory = DAOFactory.getInstance();
            
            // Khởi tạo ServiceFactory
            ServiceFactory serviceFactory = ServiceFactory.getInstance();
            
            System.out.println("Đã khởi tạo các factory thành công.");
        } catch (Exception e) {
            System.err.println("Lỗi khởi tạo factory: " + e.getMessage());
            JOptionPane.showMessageDialog(
                null,
                "Lỗi khởi tạo các thành phần ứng dụng: " + e.getMessage(),
                "Lỗi Khởi Tạo",
                JOptionPane.ERROR_MESSAGE
            );
            System.exit(1);
        }
    }
    
    /**
     * Hiển thị màn hình khởi động (splash screen)
     */
    private static void displaySplashScreen() {
        JWindow splashScreen = new JWindow();
        splashScreen.setSize(500, 300);
        splashScreen.setLocationRelativeTo(null);
        
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createLineBorder(new Color(30, 55, 153), 3));
        
        // Panel chứa logo và tiêu đề
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("HỆ THỐNG QUẢN LÝ TOUR DU LỊCH");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(30, 55, 153));
        
        JLabel versionLabel = new JLabel("Phiên bản 1.0");
        versionLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        
        // Layout for center panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        centerPanel.add(titleLabel, gbc);
        
        gbc.gridy = 1;
        centerPanel.add(versionLabel, gbc);
        
        // Loading progress bar
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString("Đang khởi động...");
        
        // Panel footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.add(new JLabel("© 2025 Tour Management System"));
        
        // Add panels to content
        content.add(centerPanel, BorderLayout.CENTER);
        content.add(progressBar, BorderLayout.SOUTH);
        content.add(footerPanel, BorderLayout.NORTH);
        
        splashScreen.setContentPane(content);
        splashScreen.setVisible(true);
        
        // Simulate loading time and close splash after delay
        new Timer(2500, e -> {
            splashScreen.dispose();
        }).start();
    }
}