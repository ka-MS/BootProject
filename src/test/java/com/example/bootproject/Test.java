package com.example.bootproject;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Test {
    public static void main(String[] args){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime date = LocalDateTime.of(2023,11,29,0,0);
        System.out.println(ChronoUnit.DAYS.between(now,date));
    }
}
