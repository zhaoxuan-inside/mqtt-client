package org.zhaoxuan.common.utils;

public class DataAnalyseUtil {

    public static Double[] analyseContent(byte[] content, Double coef) {
        Double[] result = new Double[content.length / 2];
        for (int idx = 0; idx < content.length; idx = idx + 2) {
            result[idx / 2] = generateSignIntegerAsc(content, idx) * coef;
        }
        return result;
    }

    private static Integer generateUnsignedIntegerAsc(byte[] bytes, int idx) {
        int resule = 0;
        for (int i = 0; i < 2; i++) {
            resule <<= 8;
            resule |= (bytes[idx + i] & 0xff);
        }
        return resule;
    }

    private static int generateSignIntegerAsc(byte[] bytes, int idx) {
        int intValue = (bytes[idx] & 0xFF) << 8 | (bytes[idx + 1] & 0xFF);
        return (intValue > 0x7FFF) ? (intValue - 0x10000) : intValue;
    }

}
