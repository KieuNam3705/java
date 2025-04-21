package com.tourmanagement.ui.components;

import javax.swing.JTextField;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Font;

public class CustomTextField extends JTextField {
    public CustomTextField() {
        setupTextField();
    }

    public CustomTextField(int columns) {
        super(columns);
        setupTextField();
    }

    private void setupTextField() {
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

    public void setSuccessStyle() {
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GREEN, 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }
}