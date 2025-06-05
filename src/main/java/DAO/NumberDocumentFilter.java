
package DAO;
import javax.swing.text.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class NumberDocumentFilter extends DocumentFilter {
    private final NumberFormat numberFormat;

    public NumberDocumentFilter() {
        numberFormat = NumberFormat.getIntegerInstance(Locale.GERMAN); // Sử dụng Locale.US để có dấu chấm
        numberFormat.setGroupingUsed(true); // Bật phân cách hàng nghìn
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) 
            throws BadLocationException {
        replace(fb, offset, 0, string, attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
            throws BadLocationException {
        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
        String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
        
        try {
            if (!newText.isEmpty()) {
                // Xóa tất cả dấu phân cách không phải số
                String plainText = newText.replaceAll("[^0-9]", "");
                if (!plainText.isEmpty()) {
                    long number = Long.parseLong(plainText);
                    
                    // Định dạng lại với dấu phân cách
                    String formatted = numberFormat.format(number);
                    super.replace(fb, 0, fb.getDocument().getLength(), formatted, attrs);
                } else {
                    super.replace(fb, 0, fb.getDocument().getLength(), "", attrs);
                }
            } else {
                super.replace(fb, 0, fb.getDocument().getLength(), "", attrs);
            }
        } catch (NumberFormatException e) {
            // Không làm gì nếu nhập không phải số
        }
    }
}