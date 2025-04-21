package com.tourmanagement.ui.components;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.Color;
import java.awt.Font;

public class CustomTable extends JTable {
    public CustomTable() {
        setupTable();
    }

    public CustomTable(DefaultTableModel model) {
        super(model);
        setupTable();
    }

    private void setupTable() {
        // Thiết lập header
        JTableHeader header = getTableHeader();
        header.setBackground(new Color(240, 240, 240));
        header.setFont(new Font("Arial", Font.BOLD, 14));

        // Thiết lập bảng
        setFont(new Font("Arial", Font.PLAIN, 13));
        setRowHeight(30);
        setSelectionBackground(new Color(200, 220, 255));
    }

    // Phương thức để thêm dữ liệu từ Model
    public void loadData(Object[][] data, String[] columnNames) {
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa trực tiếp trên bảng
            }
        };
        setModel(model);
    }
}