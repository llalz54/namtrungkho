package utils;

public class IntHelper {
    /**
     * Chuyển đổi chuỗi thành số nguyên.
     * Trả về 0 nếu chuỗi rỗng, null hoặc không hợp lệ.
     */
    public static int parseIntSafe(String s) {
        if (s == null || s.trim().isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Kiểm tra chuỗi có thể chuyển sang int hay không.
     */
    public static boolean isInteger(String s) {
        if (s == null || s.trim().isEmpty()) return false;
        try {
            Integer.parseInt(s.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
