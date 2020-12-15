package com.example.salarymgt.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Jiang Wensi on 16/12/2020
 */
@Slf4j
public class NumUtil {
    private static final int scale = 2;

    public static BigDecimal bigDecimal(String value) {
        try {
            return new BigDecimal(value).setScale(scale, RoundingMode.HALF_EVEN);
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    public static BigDecimal bigDecimal(Double value) {
        try {
            return new BigDecimal(value).setScale(scale, RoundingMode.HALF_EVEN);
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    public static BigDecimal bigDecimal(Integer value) {
        try {
            return new BigDecimal(value).setScale(scale, RoundingMode.HALF_EVEN);
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }
}
