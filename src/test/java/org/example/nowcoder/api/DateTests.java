package org.example.nowcoder.api;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTests {

    @Test
    public void test() throws Exception {

        String fromStr = "2023-08-15";

        String endStr = "2023-09-12";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date from = simpleDateFormat.parse(fromStr);
        Date end = simpleDateFormat.parse(endStr);


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(from);
        while (!calendar.getTime().after(end)) {
            System.out.println(simpleDateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

    }

    @Test
    public void testDates() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse("2014-08-15 00:00:00");
        long time = date.getTime();
        Date epoch = new Date(time);
        System.out.println("epoch = " + epoch);
        System.out.println("time = " + time);

        String format = simpleDateFormat.format(epoch);
        System.out.println(format);
    }
}
