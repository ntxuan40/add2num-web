package com.xuan.add2num.web.service;

import com.xuan.add2num.MyBigNumber;
import org.springframework.stereotype.Service;

@Service
public class Add2NumService {

    public String add(String stn1, String stn2) {

        MyBigNumber calculator = new MyBigNumber();

        return calculator.sum(stn1, stn2);
    }
}
