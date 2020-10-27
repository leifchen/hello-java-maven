package com.chen;

import java.time.*;
import java.time.temporal.ChronoField;

/**
 * LocalDate & LocalTime & LocalDateTime
 * <p>
 * @Author LeifChen
 * @Date 2020-10-27
 */
public class LocalDateTimeTest {

    public static void main(String[] args) {
        LocalDate localDate = LocalDate.now();
        LocalDate localDate1 = LocalDate.of(2020, 11, 11);
        System.out.println("当前日期：" + localDate);
        System.out.println("指定日期：" + localDate1);

        int year = localDate.getYear();
        int year1 = localDate.get(ChronoField.YEAR);
        System.out.println("年份：" + year);
        System.out.println("年份：" + year1);

        Month month = localDate.getMonth();
        int month1 = localDate.get(ChronoField.MONTH_OF_YEAR);
        System.out.println("月份：" + month);
        System.out.println("月份：" + month1);

        int day = localDate.getDayOfMonth();
        int day1 = localDate.get(ChronoField.DAY_OF_MONTH);
        System.out.println("天数：" + day);
        System.out.println("天数：" + day1);

        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        int dayOfWeek1 = localDate.get(ChronoField.DAY_OF_WEEK);
        System.out.println("周几：" + dayOfWeek);
        System.out.println("周几：" + dayOfWeek1);

        LocalTime localTime = LocalTime.now();
        LocalTime localTime1 = LocalTime.of(10, 15, 40);
        System.out.println("当前时间：" + localTime);
        System.out.println("指定时间：" + localTime1);

        int hour = localTime.getHour();
        int hour1 = localTime.get(ChronoField.HOUR_OF_DAY);
        System.out.println("时：" + hour);
        System.out.println("时：" + hour1);

        int minute = localTime.getMinute();
        int minute1 = localTime.get(ChronoField.MINUTE_OF_HOUR);
        System.out.println("分：" + minute);
        System.out.println("分：" + minute1);

        int second = localTime.getSecond();
        int second1 = localTime.get(ChronoField.SECOND_OF_MINUTE);
        System.out.println("秒：" + second);
        System.out.println("秒：" + second1);

        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTime1 = LocalDateTime.of(2020, Month.OCTOBER, 10, 10, 15, 40);
        LocalDateTime localDateTime2 = LocalDateTime.of(localDate, localTime);
        LocalDateTime localDateTime3 = localDate.atTime(localTime);
        LocalDateTime localDateTime4 = localTime.atDate(localDate);
        System.out.println("当前日期时间：" + localDateTime);
        System.out.println("指定日期时间1：" + localDateTime1);
        System.out.println("指定日期时间2：" + localDateTime2);
        System.out.println("指定日期时间3：" + localDateTime3);
        System.out.println("指定日期时间4：" + localDateTime4);

        LocalDate localDate2 = localDateTime.toLocalDate();
        System.out.println("转换日期："+localDate2);
        LocalTime localTime2 = localDateTime.toLocalTime();
        System.out.println("转换时间："+localTime2);

        Instant instant = Instant.now();
        long currentSecond = instant.getEpochSecond();
        long currentMilli = instant.toEpochMilli();
        System.out.println("当前秒数："+currentSecond);
        System.out.println("当前毫秒数："+currentMilli);
    }
}
