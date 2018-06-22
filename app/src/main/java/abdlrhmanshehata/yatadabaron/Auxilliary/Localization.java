package abdlrhmanshehata.yatadabaron.Auxilliary;

import android.content.res.Resources;

import java.text.NumberFormat;
import java.util.Locale;

public final class Localization {
    static NumberFormat numberFormat = NumberFormat.getInstance(new Locale("ar","EG"));

    public static String getString(Resources resources, int stringId, Object... formatArgs) {
        if (formatArgs == null || formatArgs.length == 0) {
            return resources.getString(stringId, formatArgs);
        }

        Object[] formattedArgs = new Object[formatArgs.length];
        for (int i = 0; i < formatArgs.length; i++) {
            formattedArgs[i] = (formatArgs[i] instanceof Number) ?
                    numberFormat.format(formatArgs[i]) :
                    formatArgs[i];
        }
        return resources.getString(stringId, formattedArgs);
    }
    public static String getArabicNumber(int x) {
        return numberFormat.format(x);
    }
}