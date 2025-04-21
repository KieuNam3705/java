package com.tourmanagement.ui.dialogs;

import com.tourmanagement.entity.*;
import com.tourmanagement.ui.components.CustomButton;
import com.tourmanagement.ui.components.CustomLabel;
import com.tourmanagement.ui.components.CustomTextField;
import com.tourmanagement.service.ServiceFactory;
import com.tourmanagement.service.interfaces.ITourService;
import com.tourmanagement.service.interfaces.*;
import com.tourmanagement.ui.controller.AddEditTourDialogController;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddEditTourDialog extends JDialog {
    // Controller để xử lý logic
    private AddEditTourDialogController controller;
    
    // UI Components
    private CustomTextField tenTourField;
    private JTextArea moTaArea;
    private JFormattedTextField ngayBatDauField;
    private JFormattedTextField ngayKetThucField;
    private JSpinner soNgaySpinner;
    private JSpinner soLuongKhachSpinner;
    private CustomTextField giaTourField;
    private JComboBox<String> loaiTourComboBox;
    private JComboBox<String> trangThaiComboBox;
    private CustomTextField hinhAnhField;
    
    // Các ComboBox và List cho các thành phần phụ
    private JComboBox<DiaDiem> diaDiemComboBox;
    private JComboBox<NhanVien> huongDanVienComboBox;
    private JComboBox<ChoO> choOComboBox;
    private JComboBox<PhuongTien> phuongTienComboBox;
    
    // Các DefaultListModel
    private DefaultListModel<DiaDiem> selectedDiaDiemModel = new DefaultListModel<>();
    private DefaultListModel<PhuongTien> selectedPhuongTienModel = new DefaultListModel<>();
    private DefaultListModel<ChoO> selectedChoOModel = new DefaultListModel<>();
    private DefaultListModel<NhanVien> selectedHuongDanVienModel = new DefaultListModel<>();
    
    // Các JList
    private JList<DiaDiem> selectedDiaDiemList;
    private JList<PhuongTien> selectedPhuongTienList;
    private JList<ChoO> selectedChoOList;
    private JList<NhanVien> selectedHuongDanVienList;
    
    // Constructor
    public AddEditTourDialog(Frame parent, Tour tour) {
        super(parent, tour == null ? "Thêm Tour Mới" : "Sửa Tour", true);
        
        // Khởi tạo controller
        controller = new AddEditTourDialogController(this, tour);
        
        // Thiết lập giao diện
        initComponents();
        
        // Nếu là chế độ chỉnh sửa, tải dữ liệu
        if (controller.isEditMode()) {
            loadTourData();
        }
    }
    
    // Phương thức khởi tạo giao diện
    private void initComponents() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        
        CustomLabel titleLabel = new CustomLabel(controller.isEditMode() ? "SỬA THÔNG TIN TOUR" : "THÊM TOUR MỚI");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 55, 153));
        
        titlePanel.add(titleLabel);
        
        // Form panel
        JPanel formPanel = createFormPanel();
        
        // Tab panel
        JTabbedPane tabbedPane = createTabbedPanel();
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        
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
    }
    
    // Tạo form panel
    private JPanel createFormPanel() {
        
        // (Bao gồm các trường nhập liệu như tên tour, mô tả, ngày, giá, v.v.)
        // Ví dụ như trong file gốc
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Tên tour
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Tên tour:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        tenTourField = new CustomTextField(25);
        formPanel.add(tenTourField, gbc);
        
       // Mô tả
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Mô tả:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 2;
        moTaArea = new JTextArea(5, 25);
        moTaArea.setLineWrap(true);
        moTaArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(moTaArea);
        formPanel.add(scrollPane, gbc);
        
        // Reset grid height
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Ngày bắt đầu
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Ngày bắt đầu:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        ngayBatDauField = new JFormattedTextField(dateFormat);
        ngayBatDauField.setColumns(10);
        ngayBatDauField.setValue(new Date()); // Default to current date
        formPanel.add(ngayBatDauField, gbc);
        
        // Ngày kết thúc
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Ngày kết thúc:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        ngayKetThucField = new JFormattedTextField(dateFormat);
        ngayKetThucField.setColumns(10);
        ngayKetThucField.setValue(new Date()); // Default to current date
        formPanel.add(ngayKetThucField, gbc);
        
        // Số ngày
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Số ngày:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        SpinnerNumberModel soNgayModel = new SpinnerNumberModel(1, 1, 30, 1); // min 1, max 30, step 1
        soNgaySpinner = new JSpinner(soNgayModel);
        formPanel.add(soNgaySpinner, gbc);
        
        // Số lượng khách tối đa
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Số lượng khách tối đa:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        SpinnerNumberModel soKhachModel = new SpinnerNumberModel(10, 1, 100, 1); // Default 10, min 1, max 100
        soLuongKhachSpinner = new JSpinner(soKhachModel);
        formPanel.add(soLuongKhachSpinner, gbc);
        
        // Giá tour
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Giá tour:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        giaTourField = new CustomTextField(15);
        formPanel.add(giaTourField, gbc);
        
        // Loại tour
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Loại tour:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        String[] loaiTourOptions = {"Du lịch trong nước", "Du lịch nước ngoài", "Tour nghỉ dưỡng", "Tour khám phá", "Tour mạo hiểm"};
        loaiTourComboBox = new JComboBox<>(loaiTourOptions);
        formPanel.add(loaiTourComboBox, gbc);
        
        // Trạng thái
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Trạng thái:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        String[] trangThaiOptions = {"Đang mở đặt", "Đã khóa đặt", "Đang diễn ra", "Đã kết thúc", "Đã hủy"};
        trangThaiComboBox = new JComboBox<>(trangThaiOptions);
        formPanel.add(trangThaiComboBox, gbc);
        
        
       // Hình ảnh
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Hình ảnh (URL):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        
        
        return formPanel;
    }
    
    // Tạo tab panel
    private JTabbedPane createTabbedPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Tab địa điểm
        tabbedPane.addTab("Địa điểm", createDiaDiemPanel());
        
        // Tab phương tiện
        tabbedPane.addTab("Phương tiện", createPhuongTienPanel());
        
        // Tab chỗ ở
        tabbedPane.addTab("Chỗ ở", createChoOPanel());
        
        // Tab hướng dẫn viên
        tabbedPane.addTab("Hướng dẫn viên", createHDVPanel());
        
        return tabbedPane;
    }
    
    // Tạo panel địa điểm
    private JPanel createDiaDiemPanel() {
        JPanel diaDiemPanel = new JPanel(new BorderLayout(5, 5));
        diaDiemPanel.setBackground(Color.WHITE);

        // Panel chọn địa điểm
        JPanel diaDiemSelectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        diaDiemSelectionPanel.setBackground(Color.WHITE);

        // Lấy danh sách địa điểm từ service
        IDiaDiemService diaDiemService = ServiceFactory.getInstance().getDiaDiemService();
        List<DiaDiem> danhSachDiaDiem = diaDiemService.layDanhSach();
        diaDiemComboBox = new JComboBox<>(danhSachDiaDiem.toArray(new DiaDiem[0]));
        diaDiemComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DiaDiem) {
                    setText(((DiaDiem) value).getTenDiaDiem());
                }
                return this;
            }
        });

        CustomButton addDiaDiemButton = new CustomButton("Thêm");
        addDiaDiemButton.setStylePrimary();
        addDiaDiemButton.addActionListener(e -> {
            DiaDiem selected = (DiaDiem) diaDiemComboBox.getSelectedItem();
            if (selected != null && !isDiaDiemInModel(selected)) {
                selectedDiaDiemModel.addElement(selected);
            }
        });

        diaDiemSelectionPanel.add(new JLabel("Chọn địa điểm: "));
        diaDiemSelectionPanel.add(diaDiemComboBox);
        diaDiemSelectionPanel.add(addDiaDiemButton);

        // Danh sách địa điểm đã chọn
        selectedDiaDiemList = new JList<>(selectedDiaDiemModel);
        selectedDiaDiemList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DiaDiem) {
                    setText(((DiaDiem) value).getTenDiaDiem());
                }
                return this;
            }
        });

        CustomButton removeDiaDiemButton = new CustomButton("Xóa");
        removeDiaDiemButton.setStyleDanger();
        removeDiaDiemButton.addActionListener(e -> {
            int selectedIndex = selectedDiaDiemList.getSelectedIndex();
            if (selectedIndex != -1) {
                selectedDiaDiemModel.remove(selectedIndex);
            }
        });

        JPanel diaDiemListPanel = new JPanel(new BorderLayout(5, 5));
        diaDiemListPanel.setBackground(Color.WHITE);
        diaDiemListPanel.setBorder(BorderFactory.createTitledBorder("Địa điểm đã chọn"));
        diaDiemListPanel.add(new JScrollPane(selectedDiaDiemList), BorderLayout.CENTER);
        diaDiemListPanel.add(removeDiaDiemButton, BorderLayout.SOUTH);

        diaDiemPanel.add(diaDiemSelectionPanel, BorderLayout.NORTH);
        diaDiemPanel.add(diaDiemListPanel, BorderLayout.CENTER);

        return diaDiemPanel;
    }
    
    // Tạo panel phương tiện (tương tự như panel địa điểm)
    private JPanel createPhuongTienPanel() {
        // Tương tự như createDiaDiemPanel nhưng cho phương tiện
        JPanel phuongTienPanel = new JPanel(new BorderLayout(5, 5));
        phuongTienPanel.setBackground(Color.WHITE);

        // ... (code tương tự như createDiaDiemPanel)

        return phuongTienPanel;
    }
    
    // Tạo panel chỗ ở (tương tự như panel địa điểm)
    private JPanel createChoOPanel() {
        // Tương tự như createDiaDiemPanel nhưng cho chỗ ở
        return new JPanel();
    }
    
    // Tạo panel hướng dẫn viên (tương tự như panel địa điểm)
    private JPanel createHDVPanel() {
        // Tương tự như createDiaDiemPanel nhưng cho hướng dẫn viên
        return new JPanel();
    }
    
    // Tạo panel nút bấm
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        CustomButton saveButton = new CustomButton("Lưu");
        saveButton.setStylePrimary();
        
        CustomButton cancelButton = new CustomButton("Hủy");
        cancelButton.setStyleDanger();
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        // Thêm sự kiện
        saveButton.addActionListener(e -> controller.saveTour());
        cancelButton.addActionListener(e -> dispose());
        
        return buttonPanel;
    }
    
    // Tải dữ liệu tour khi ở chế độ chỉnh sửa
    private void loadTourData() {
        controller.loadTourData();
    }
    
    // Chọn hình ảnh
    private void selectImage() {
        controller.selectImage();
    }
    
    // Kiểm tra địa điểm đã tồn tại trong model
    private boolean isDiaDiemInModel(DiaDiem diaDiem) {
        for (int i = 0; i < selectedDiaDiemModel.size(); i++) {
            if (selectedDiaDiemModel.getElementAt(i).getMaDiaDiem() == diaDiem.getMaDiaDiem()) {
                return true;
            }
        }
        return false;
    }
    
    // Kiểm tra phương tiện đã tồn tại trong model
    private boolean isPhuongTienInModel(PhuongTien phuongTien) {
        for (int i = 0; i < selectedPhuongTienModel.size(); i++) {
            if (selectedPhuongTienModel.getElementAt(i).getMaPhuongTien() == phuongTien.getMaPhuongTien()) {
                return true;
            }
        }
        return false;
    }
    
    // Kiểm tra chỗ ở đã tồn tại trong model
    private boolean isChoOInModel(ChoO choO) {
        for (int i = 0; i < selectedChoOModel.size(); i++) {
            if (selectedChoOModel.getElementAt(i).getMaChoO() == choO.getMaChoO()) {
                return true;
            }
        }
        return false;
    }
    
    // Kiểm tra hướng dẫn viên đã tồn tại trong model
    private boolean isHDVInModel(NhanVien nhanVien) {
        for (int i = 0; i < selectedHuongDanVienModel.size(); i++) {
            if (selectedHuongDanVienModel.getElementAt(i).getMaNV() == nhanVien.getMaNV()) {
                return true;
            }
        }
        return false;
    }
    // Các getter cho các thành phần UI

    public CustomTextField getTenTourField() {
        return tenTourField;
    }

    public JTextArea getMoTaArea() {
        return moTaArea;
    }

    public JFormattedTextField getNgayBatDauField() {
        return ngayBatDauField;
    }

    public JFormattedTextField getNgayKetThucField() {
        return ngayKetThucField;
    }

    public JSpinner getSoNgaySpinner() {
        return soNgaySpinner;
    }

    public JSpinner getSoLuongKhachSpinner() {
        return soLuongKhachSpinner;
    }

    public CustomTextField getGiaTourField() {
        return giaTourField;
    }

    public JComboBox<String> getLoaiTourComboBox() {
        return loaiTourComboBox;
    }

    public JComboBox<String> getTrangThaiComboBox() {
        return trangThaiComboBox;
    }

    public CustomTextField getHinhAnhField() {
        return hinhAnhField;
    }

    // Getter cho các ComboBox và thành phần phụ
    public JComboBox<DiaDiem> getDiaDiemComboBox() {
        return diaDiemComboBox;
    }

    public JComboBox<NhanVien> getHuongDanVienComboBox() {
        return huongDanVienComboBox;
    }

    public JComboBox<ChoO> getChoOComboBox() {
        return choOComboBox;
    }

    public JComboBox<PhuongTien> getPhuongTienComboBox() {
        return phuongTienComboBox;
    }

    // Getter cho các DefaultListModel
    public DefaultListModel<DiaDiem> getSelectedDiaDiemModel() {
        return selectedDiaDiemModel;
    }

    public DefaultListModel<PhuongTien> getSelectedPhuongTienModel() {
        return selectedPhuongTienModel;
    }

    public DefaultListModel<ChoO> getSelectedChoOModel() {
        return selectedChoOModel;
    }

    public DefaultListModel<NhanVien> getSelectedHuongDanVienModel() {
        return selectedHuongDanVienModel;
    }

    // Getter cho các JList
    public JList<DiaDiem> getSelectedDiaDiemList() {
        return selectedDiaDiemList;
    }

    public JList<PhuongTien> getSelectedPhuongTienList() {
        return selectedPhuongTienList;
    }

    public JList<ChoO> getSelectedChoOList() {
        return selectedChoOList;
    }

    public JList<NhanVien> getSelectedHuongDanVienList() {
        return selectedHuongDanVienList;
    }

    // Phương thức kiểm tra lưu thành công
    public boolean isSaveSuccess() {
        return controller.isSaveSuccess();
    }
}