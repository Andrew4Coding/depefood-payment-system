package assignments.assignment4.Utils;

import java.text.NumberFormat;
import java.util.Locale;

public class ThousandFormatter {
    public static String formatWithThousandSeparator(double number) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        return formatter.format(number);
    }
}
