package utils;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;

public class UpperCaseEditor extends DefaultCellEditor {
    private final JTextField textField;

    public UpperCaseEditor() {
        super(new JTextField());
        textField = (JTextField) getComponent();

        // Chuyển chữ thường thành in hoa khi gõ
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();
                if (Character.isLowerCase(ch)) {
                    e.setKeyChar(Character.toUpperCase(ch));
                }
            }
        });

        // Đảm bảo cả nội dung dán cũng được chuyển thành in hoa
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                textField.setText(textField.getText().toUpperCase());
            }
        });
    }

    @Override
    public boolean stopCellEditing() {
        textField.setText(textField.getText().toUpperCase());
        return super.stopCellEditing();
    }
}
