package com.example.semirzahirovic.travelplanner;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import java.util.Date;

import utils.DateUtil;

/**
 * Created by semirzahirovic on 08/12/15.
 */
public class DateParsingTest extends AndroidTestCase {

    public DateParsingTest() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @SmallTest
    public void testParseDateToNormalConvert() {
        Date date1 = new Date(1449702006889L);
        String string1 = "10 Dec 15";
        assertEquals(DateUtil.formParseFormatToNormal(date1), string1);
    }

    @SmallTest
    public void testNormalToDateConvert() {
        Date date1 = new Date(1449702006889L);
        String string2 = "2015-12-10T00:00:06Z";
        assertEquals(DateUtil.formatDateForParse(date1), string2);
    }
}
