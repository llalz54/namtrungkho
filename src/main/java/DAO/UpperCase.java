
package DAO;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;



public class UpperCase extends DefaultCellEditor {
    private JTextField textField;

    public UpperCase() {
        super(new JTextField());
        textField = (JTextField) getComponent();
        textField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                int pos = textField.getCaretPosition();
                textField.setText(textField.getText().toUpperCase());
                textField.setCaretPosition(pos);
            }
        });
    }

    @Override
    public Object getCellEditorValue() {
        return textField.getText().toUpperCase(); // đảm bảo luôn trả về chữ hoa
    }
}
