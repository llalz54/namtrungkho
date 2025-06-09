package utils;

public class StringHelper {
    /**
     * Trả về chuỗi đã trim, hoặc chuỗi rỗng nếu null.
     */
    public static String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }

    /**
     * Kiểm tra chuỗi có null hoặc toàn khoảng trắng không.
     */
    public static boolean isNullOrBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    /**
     * Viết hoa chữ cái đầu tiên (ví dụ: "honda" -> "Honda")
     */
    public static String capitalize(String s) {
        if (isNullOrBlank(s)) {
            return "";
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
