package com.tourmanagement.ui.components;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;

public class CustomLabel extends JLabel {
    public CustomLabel(String text) {
        super(text);
        setupLabel();
    }

    private void setupLabel() {
        setFont(new Font("Arial", Font.PLAIN, 14));
    }

    public void setHeaderStyle() {
        setFont(new Font("Arial", Font.BOLD, 18));
        setForeground(new Color(0, 0, 128)); // Màu xanh đậm
    }

    public void setSubheaderStyle() {
        setFont(new Font("Arial", Font.BOLD, 16));
        setForeground(new Color(70, 70, 70));
    }
}