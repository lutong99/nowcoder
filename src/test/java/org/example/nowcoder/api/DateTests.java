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
}
