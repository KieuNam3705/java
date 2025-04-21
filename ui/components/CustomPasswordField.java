package com.tourmanagement.ui.components;

import javax.swing.JPasswordField;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Font;

public class CustomPasswordField extends JPasswordField {
    public CustomPasswordField() {
        setupPasswordField();
    }

    public CustomPasswordField(int columns) {
        super(columns);
        setupPasswordField();
    }

    private void setupPasswordField() {
        setFont(new Font("Arial", Font.PLAIN, 14));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    public void setErrorStyle() {
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.RED, 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }
}   