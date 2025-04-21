        package com.tourmanagement.ui.dialogs;

import com.tourmanagement.ui.components.CustomButton;
import com.tourmanagement.ui.components.CustomLabel;
import com.tourmanagement.ui.components.CustomPasswordField;
import com.tourmanagement.ui.components.CustomTextField;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AddEditAccountDialog extends JDialog {

    // Các thành phần UI
    private CustomTextField tenDangNhapField;
    private CustomPasswordField matKhauField;
    private JComboBox<String> loaiTaiKhoanComboBox;
    private JComboBox<String> trangThaiComboBox;

    // Thêm ComboBox cho Khách hàng và Nhân viên
    private JComboBox<Object> khachHangComboBox; // Sử dụng Object để lưu KhachHang object
    private JComboBox<Object> nhanVienComboBox; // Sử dụng Object để lưu NhanVien object

    private CustomButton saveButton;
    private CustomButton cancelButton;

    private boolean saveSuccess = false;
    private boolean isEditMode;

    public AddEditAccountDialog(Frame parent, boolean isEditMode) {
        super(parent, isEditMode ? "Sửa Tài Khoản" : "Thêm Tài Khoản Mới", true);
        this.isEditMode = isEditMode;

        initComponents();
        // Logic tải dữ liệu (nếu isEditMode) sẽ do Controller thực hiện
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);

        CustomLabel titleLabel = new CustomLabel(isEditMode ? "SỬA THÔNG TIN TÀI KHOẢN" : "THÊM TÀI KHOẢN MỚI");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 55, 153));
        titlePanel.add(titleLabel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tên đăng nhập
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Tên đăng nhập:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        tenDangNhapField = new CustomTextField(20);
        formPanel.add(tenDangNhapField, gbc);

        // Mật khẩu (chỉ hiển thị và cho phép nhập khi thêm, hoặc khi sửa nhưng người dùng muốn đổi)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel(isEditMode ? "Mật khẩu mới (để trống nếu không đổi):" : "Mật khẩu:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        matKhauField = new CustomPasswordField(20);
        formPanel.add(matKhauField, gbc);

        // Loại tài khoản
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Loại tài khoản:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        String[] loaiTaiKhoanOptions = {"Admin", "Nhân viên", "Khách hàng"};
        loaiTaiKhoanComboBox = new JComboBox<>(loaiTaiKhoanOptions);
        formPanel.add(loaiTaiKhoanComboBox, gbc);

        // ComboBox Khách hàng (Ban đầu ẩn)
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel khachHangLabel = new JLabel("Chọn Khách hàng:");
        formPanel.add(khachHangLabel, gbc);
        khachHangLabel.setVisible(false); // Ẩn ban đầu

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        khachHangComboBox = new JComboBox<>();
        khachHangComboBox.setVisible(false); // Ẩn ban đầu
        formPanel.add(khachHangComboBox, gbc);

        // ComboBox Nhân viên (Ban đầu ẩn)
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel nhanVienLabel = new JLabel("Chọn Nhân viên:");
        formPanel.add(nhanVienLabel, gbc);
        nhanVienLabel.setVisible(false); // Ẩn ban đầu

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        nhanVienComboBox = new JComboBox<>();
        nhanVienComboBox.setVisible(false); // Ẩn ban đầu
        formPanel.add(nhanVienComboBox, gbc);


        // Trạng thái
        gbc.gridx = 0;
        gbc.gridy = 5; // Điều chỉnh gridy sau khi thêm KH/NV comboboxes
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Trạng thái:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5; // Điều chỉnh gridy
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        String[] trangThaiOptions = {"Hoạt động", "Khóa"};
        trangThaiComboBox = new JComboBox<>(trangThaiOptions);
        formPanel.add(trangThaiComboBox, gbc);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        saveButton = new CustomButton("Lưu");
        saveButton.setStylePrimary();

        cancelButton = new CustomButton("Hủy");
        cancelButton.setStyleDanger();

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
        pack();
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Add listener to toggle visibility of KH/NV comboboxes
        loaiTaiKhoanComboBox.addActionListener(e -> {
            String selectedType = (String) loaiTaiKhoanComboBox.getSelectedItem();
            boolean isKhachHang = "Khách hàng".equals(selectedType);
            boolean isNhanVien = "Nhân viên".equals(selectedType);

            khachHangLabel.setVisible(isKhachHang);
            khachHangComboBox.setVisible(isKhachHang);

            nhanVienLabel.setVisible(isNhanVien);
            nhanVienComboBox.setVisible(isNhanVien);

            // Re-pack the dialog to adjust layout
            pack();
        });

        // Action listeners for buttons are attached in the Controller
    }

    // --- Getters để Controller lấy dữ liệu từ Dialog ---
    public String getTenDangNhap() {
        return tenDangNhapField.getText();
    }

    public char[] getMatKhau() {
        return matKhauField.getPassword();
    }

    public String getLoaiTaiKhoan() {
        return (String) loaiTaiKhoanComboBox.getSelectedItem();
    }

    public String getTrangThai() {
        return (String) trangThaiComboBox.getSelectedItem();
    }

    // Getters cho ComboBoxes Khách hàng và Nhân viên (trả về object được chọn)
    public Object getSelectedKhachHang() {
         Object selectedItem = khachHangComboBox.getSelectedItem();
         // Trả về null nếu mục "Chọn khách hàng" được chọn hoặc không có gì
         if (selectedItem instanceof String && ((String)selectedItem).startsWith("Chọn Khách hàng")) {
             return null;
         }
         return selectedItem; // Trả về KhachHang object
    }

    public Object getSelectedNhanVien() {
         Object selectedItem = nhanVienComboBox.getSelectedItem();
          // Trả về null nếu mục "Chọn nhân viên" được chọn hoặc không có gì
         if (selectedItem instanceof String && ((String)selectedItem).startsWith("Chọn Nhân viên")) {
             return null;
         }
         return selectedItem; // Trả về NhanVien object
    }


    // --- Setters để Controller tải dữ liệu vào Dialog (chế độ sửa) ---
    public void setTenDangNhap(String tenDangNhap) {
        tenDangNhapField.setText(tenDangNhap);
    }

    // Không cung cấp setter cho mật khẩu vì lý do bảo mật
    // public void setMatKhau(String matKhau) { matKhauField.setText(matKhau); }

    public void setLoaiTaiKhoan(String loaiTaiKhoan) {
        loaiTaiKhoanComboBox.setSelectedItem(loaiTaiKhoan);
        // Manually trigger the action listener after setting the type
        for (java.awt.event.ActionListener listener : loaiTaiKhoanComboBox.getActionListeners()) {
            listener.actionPerformed(new java.awt.event.ActionEvent(loaiTaiKhoanComboBox, java.awt.event.ActionEvent.ACTION_PERFORMED, null));
        }
    }

    public void setTrangThai(String trangThai) {
        trangThaiComboBox.setSelectedItem(trangThai);
    }

    // Setters để Controller điền dữ liệu vào ComboBoxes
    public void populateKhachHangComboBox(List<?> khachHangList) {
        DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>();
        model.addElement("Chọn Khách hàng..."); // Thêm mục mặc định
        for (Object kh : khachHangList) {
            model.addElement(kh);
        }
        khachHangComboBox.setModel(model);
        // Sử dụng Custom Renderer nếu muốn hiển thị chỉ tên
         khachHangComboBox.setRenderer(new DefaultListCellRenderer() {
             @Override
             public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                 super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                 if (value instanceof com.tourmanagement.entity.KhachHang) { // Đảm bảo import KhachHang hoặc dùng interface/Object chung
                     setText(((com.tourmanagement.entity.KhachHang) value).getHoTen());
                 } else if (value instanceof String) {
                     setText((String)value); // Cho mục mặc định
                 }
                 return this;
             }
         });
    }

    public void populateNhanVienComboBox(List<?> nhanVienList) {
        DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>();
        model.addElement("Chọn Nhân viên..."); // Thêm mục mặc định
         for (Object nv : nhanVienList) {
            model.addElement(nv);
        }
        nhanVienComboBox.setModel(model);
         // Sử dụng Custom Renderer nếu muốn hiển thị chỉ tên
         nhanVienComboBox.setRenderer(new DefaultListCellRenderer() {
             @Override
             public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                 super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                 if (value instanceof com.tourmanagement.entity.NhanVien) { // Đảm bảo import NhanVien hoặc dùng interface/Object chung
                     setText(((com.tourmanagement.entity.NhanVien) value).getHoTen());
                 } else if (value instanceof String) {
                      setText((String)value); // Cho mục mặc định
                 }
                 return this;
             }
         });
    }

    // Setter để chọn Khách hàng/Nhân viên khi sửa
    public void setSelectedKhachHang(int maKH) {
        DefaultComboBoxModel<?> model = (DefaultComboBoxModel<?>) khachHangComboBox.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            Object item = model.getElementAt(i);
            if (item instanceof com.tourmanagement.entity.KhachHang) {
                if (((com.tourmanagement.entity.KhachHang) item).getMaKH() == maKH) {
                    khachHangComboBox.setSelectedItem(item);
                    return;
                }
            }
        }
         khachHangComboBox.setSelectedIndex(0); // Chọn mục mặc định nếu không tìm thấy
    }

     public void setSelectedNhanVien(int maNV) {
        DefaultComboBoxModel<?> model = (DefaultComboBoxModel<?>) nhanVienComboBox.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            Object item = model.getElementAt(i);
             if (item instanceof com.tourmanagement.entity.NhanVien) {
                if (((com.tourmanagement.entity.NhanVien) item).getMaNV() == maNV) {
                    nhanVienComboBox.setSelectedItem(item);
                    return;
                }
            }
        }
         nhanVienComboBox.setSelectedIndex(0); // Chọn mục mặc định nếu không tìm thấy
    }


    // --- Getters cho các nút để Controller gắn Listener ---
    public CustomButton getSaveButton() {
        return saveButton;
    }

    public CustomButton getCancelButton() {
        return cancelButton;
    }

    // --- Getter/Setter cho trạng thái lưu ---
    public boolean isSaveSuccess() {
        return saveSuccess;
    }

    public void setSaveSuccess(boolean saveSuccess) {
        this.saveSuccess = saveSuccess;
    }
}