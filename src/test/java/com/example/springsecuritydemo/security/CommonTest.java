package com.example.springsecuritydemo.security;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CommonTest {

    @Test
    void testParseDate() throws ParseException {
        String stringToParse = "2022-01-14T05:42:21.601000Z";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS000'Z'");
        //sdf.setTimeZone(TimeZone.getTimeZone("Z"));
        Date parse = sdf.parse(stringToParse);
        System.out.println(parse.toString());
        System.out.println(parse.getTime());
        //
        //LocalDateTime parse = LocalDateTime.parse(stringToParse, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'000'Z"));
        //System.out.println(parse);
    }
}
