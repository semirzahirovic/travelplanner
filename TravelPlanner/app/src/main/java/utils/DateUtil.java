package utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by semirzahirovic on 03/12/15.
 */
public class DateUtil {
    public static String formatDateForParse(Date date) {
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return dateFormat1.format(date);
    }

    public static String formParseFormatToNormal(Date date) {
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd MMM yy");
        return dateFormat1.format(date);
    }

    public static Date addMonths(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    public static String encodeParseDate(Date date) {
        JSONObject dateAsObj = new JSONObject();
        try {
            dateAsObj.put("__type", "Date");
            dateAsObj.put("iso", DateUtil.formatDateForParse(date));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dateAsObj.toString();
    }
}
