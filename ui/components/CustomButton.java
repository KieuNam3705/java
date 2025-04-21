package com.tourmanagement.ui.components;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;

public class CustomButton extends JButton {
    public CustomButton(String text) {
        super(text);
        setupButton();
    }

    private void setupButton() {
        setFont(new Font("Arial", Font.BOLD, 14));
        setBackground(Color.BLUE);
        setForeground(Color.WHITE);
        setBorderPainted(false);
        setFocusPainted(false);
    }

    public void setStylePrimary() {
        setBackground(new Color(0, 123, 255));
    }

    public void setStyleSecondary() {
        setBackground(new Color(108, 117, 125));
    }

    public void setStyleDanger() {
        setBackground(new Color(220, 53, 69));
    }
}