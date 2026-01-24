package com.hoshino.cti.util;

import java.util.Random;
import java.util.UUID;

public class MathUtil {
    public static UUID getUUIDFromString(String str) {
        int hash = str.hashCode();
        Random random = new Random();
        random.setSeed(hash);
        long l0 = random.nextLong();
        long l1 = random.nextLong();
        return new UUID(l0, l1);
    }
    public static String getUnitInt(int amount){
        int a = (int) Math.log10(amount);
        int b =a/3;
        switch (b){
            case 1->{
                return String.format("%.2f",(float)amount/1E+3)+" k";
            }
            case 2->{
                return String.format("%.2f",(float)amount/1E+6)+" M";
            }
            case 3->{
                return String.format("%.2f",(float)amount/1E+9)+" G";
            }
            default-> {
                return amount + " ";
            }
        }
    }
    public static String getUnitFloat(double amount){
        String unit;
        double bitRaw = Math.log10(Math.abs(amount));
        bitRaw+=bitRaw<=0?-3:0;
        bitRaw /= 3;
        int bits = (int) bitRaw;
        if (bits>5){
            unit = " P";
            amount*= 1E-15F;
        }
        else if (bits<-5){
            unit =" f";
            amount*= 1E+15F;
        }
        else {
            unit = switch (bits){
                case -4 ->" p";
                case -3 ->" n";
                case -2 ->" μ";
                case -1 ->" m";
                case -0 ->" ";
                case 1 ->" k";
                case 2 ->" M";
                case 3 ->" G";
                case 4 ->" T";
                case 5 ->" P";
                default ->" f";
            };
            amount*= (float) Math.pow(1000,-bits);
        }
        return String.format("%.2f",amount) +unit;
    }
    public static String getUnitForFluid(int amount){
        int a = (int) Math.log10(amount);
        int b =a/3;
        switch (b){
            case 1->{
                return String.format("%.2f",(float)amount/1E+3)+" B";
            }
            case 2->{
                return String.format("%.2f",(float)amount/1E+6)+" kB";
            }
            case 3->{
                return String.format("%.2f",(float)amount/1E+9)+" MB";
            }
            default-> {
                return amount + " mB";
            }
        }
    }

    /**
     *
     * @param number 输入的数字
     * @return 保留的小数部分,
     * <br>如-2.3直接得到-0.3
     * <br>2.1直接得到0.1
     */
    public static double getDecimal(double number) {
        return number - (int)number;
    }

    /**
     *
     * @param number 输入的数字
     * @param limit 保留的位数
     * @return 限位后的数
     * <br>如MathUtil.limitsNumber(3.14159,3),则返回3.141
     * <br>不要输入<strong>太过大或者太过小的数值</strong>,会丢精度
     */
    public static double limitsNumber(double number,int limit){
        if(limit<0){
            limit=0;
        }
        double scale=Math.pow(10,limit);
        return Math.round(number * scale)/ scale;
    }
    public static String getEnergyString(int amount){
        return getUnitInt(amount)+"FE";
    }
    /**
     *
     * @param number 输入的数字
     * @param limit 保留百分号后的位数
     * @return 得到的带%符号的字符串
     * <br>如MathUtil.toPercentage(0.78645,1),则返回78.6%
     * <br>不要输入<strong>太过大或者太过小的数值</strong>,会丢精度
     */
    public static String toPercentage(double number,int limit){
        return limitsNumber(number,limit+2) * 100+"%";
    }
}
