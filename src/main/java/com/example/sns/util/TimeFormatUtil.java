package com.example.sns.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component("timeFormatUtil")
public class TimeFormatUtil {
    public String format(LocalDateTime createdAt) {
        if (createdAt == null) {
            return "";
        }
        LocalDateTime now = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(createdAt, now);
        long hours = ChronoUnit.HOURS.between(createdAt, now);
        long days = ChronoUnit.DAYS.between(createdAt, now);
        if (minutes < 1) {
            return "たった今";
        } else if (minutes < 60) {
            return minutes + "分";
        } else if (hours < 24) {
            return hours + "時間";
        } else if (days < 7) {
            return days + "日";
        } else if (createdAt.getYear() == now.getYear()) {
            return createdAt.getMonthValue() + "月" + createdAt.getDayOfMonth() + "日";
        } else {
            return createdAt.getYear() + "年" + createdAt.getMonthValue() + "月" + createdAt.getDayOfMonth() + "日";
        }
    }
}