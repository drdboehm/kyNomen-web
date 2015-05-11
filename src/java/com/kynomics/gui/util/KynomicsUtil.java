/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kynomics.gui.util;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author dboehm
 */
public class KynomicsUtil {

    public static int getAge(Date birthday) {
        Calendar birthCal = Calendar.getInstance();
        if (birthday == null) {
            birthCal.set(1965, 5, 2);
        } else {
            birthCal.setTime(birthday);
        }
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR)
                - birthCal.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR)
                < birthCal.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }
}
