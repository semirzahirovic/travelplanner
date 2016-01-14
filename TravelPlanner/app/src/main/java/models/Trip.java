package models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

import utils.DateUtil;

/**
 * Created by semirzahirovic on 02/12/15.
 */
@ParseClassName("Trip")
public class Trip extends ParseObject {

    public String getDestination() {
        return getString("destination");
    }

    public void setDestination(String destination) {
        put("destination", destination);
    }

    public String getComment() {
        return getString("comment");
    }

    public void setComment(String comment) {
        put("comment", comment);
    }

    public void setOwner(ParseUser currentUser) {
        put("owner", currentUser);
    }

    public Date getStartDate() {
        return getDate("startDate");
    }

    public Date getEndDate() {
        return getDate("endDate");
    }


    public void setStartDate(Date date) {

        JSONObject dateAsObj = new JSONObject();
        try {
            dateAsObj.put("__type", "Date");
            dateAsObj.put("iso", DateUtil.formatDateForParse(date));
            put("startDate", dateAsObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setEndDate(Date date) throws ParseException {

        JSONObject dateAsObj = new JSONObject();
        try {
            dateAsObj.put("__type", "Date");
            dateAsObj.put("iso", DateUtil.formatDateForParse(date));
            put("endDate", dateAsObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
