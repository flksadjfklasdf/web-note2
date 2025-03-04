package com.web.note.util;

public class MathUtil {
    public static int intDivideCeil(int a,int b){
        double result = (double)a / b;
        return  (int)Math.ceil(result);
    }
}
