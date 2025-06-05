/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author Admin
 */
public class NumberToWords {

    private static final String[] DON_VI = {
        "", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín"
    };

    private static final String[] HANG = {
        "", "nghìn", "triệu", "tỷ"
    };

    public static String convert(long number) {
        if (number == 0) {
            return "Không đồng";
        }

        StringBuilder result = new StringBuilder();
        int hangIndex = 0;

        do {
            int threeDigits = (int) (number % 1000);
            if (threeDigits != 0) {
                String groupText = convertThreeDigits(threeDigits);
                result.insert(0, groupText + " " + HANG[hangIndex] + " ");
            }
            number /= 1000;
            hangIndex++;
        } while (number > 0);

        // Viết hoa chữ cái đầu, bỏ khoảng trắng dư
        String finalResult = result.toString().trim().replaceAll("\\s+", " ");
        finalResult = finalResult.substring(0, 1).toUpperCase() + finalResult.substring(1) + " đồng chẵn";

        return finalResult;
    }

    private static String convertThreeDigits(int number) {
        int hundreds = number / 100;
        int tens = (number % 100) / 10;
        int units = number % 10;

        StringBuilder result = new StringBuilder();

        if (hundreds > 0) {
            result.append(DON_VI[hundreds]).append(" trăm");
            if (tens == 0 && units > 0) {
                result.append(" linh");
            }
        }

        if (tens > 1) {
            result.append(" ").append(DON_VI[tens]).append(" mươi");
            if (units == 1) {
                result.append(" mốt");
            } else if (units == 5) {
                result.append(" lăm");
            } else if (units > 0) {
                result.append(" ").append(DON_VI[units]);
            }
        } else if (tens == 1) {
            result.append(" mười");
            if (units == 1) {
                result.append(" một");
            } else if (units == 5) {
                result.append(" lăm");
            } else if (units > 0) {
                result.append(" ").append(DON_VI[units]);
            }
        } else if (units > 0 && hundreds == 0) {
            result.append(DON_VI[units]);
        } else if (units > 0) {
            result.append(" ").append(DON_VI[units]);
        }

        return result.toString().trim();
    }
}
