package com.example.salarymgt.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jiang Wensi on 16/12/2020
 */
@Slf4j
public class DateUtil {
    public static Date parse(String date) throws ParseException {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            log.info(e.getMessage());
        }
        try {
            return new SimpleDateFormat("dd-MMM-yy").parse(date);
        } catch (ParseException e) {
            log.error(e.getMessage(),e);
            throw e;
        }

    }
}
