package org.zhaoxuan.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseEnum {
    SUCCESS(1),
    FAILURE(0),
    CRC_CHECK_FAILURE(-1),
    NOT_FOUND_CHANNEL_MEASURE_POINT(-2),
    JSON_ANALYSE_FAILURE(-3);
    private final int code;

    public static ResponseEnum getByCode(int code) {
        for (ResponseEnum value : ResponseEnum.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
}
