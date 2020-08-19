package com.dylan.demo.service;

import com.dylan.demo.DemoApplicationTests;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Bussy
 * Creation Date: 2020/8/19
 * Copyright@Bussy
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
class CheckinServiceTest extends DemoApplicationTests {
    @Autowired
    CheckinService checkinService;

    @Test
    void testIncomeCalculator() {
        int seconds = 14324;
        int numberOfItem = 149;
        double income = checkinService.calculateIncome(seconds, numberOfItem);
        System.out.println(income);
    }
}