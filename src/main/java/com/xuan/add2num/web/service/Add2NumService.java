package com.xuan.add2num.web.service;

import org.springframework.stereotype.Service;

@Service
public class Add2NumService {

    public String add(String stn1, String stn2) {

        Add2Num calculator = new Add2Num();

        return calculator.add(stn1, stn2);
    }
}
