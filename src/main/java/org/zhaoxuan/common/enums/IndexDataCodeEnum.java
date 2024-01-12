package org.zhaoxuan.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.util.annotation.Nullable;

@Slf4j
@Getter
@AllArgsConstructor
public enum IndexDataCodeEnum {
    DEFAULT(0),
    TEMPERATURE(100),
    LOW_FREQ_VALUE(101),
    HIGH_FREQ_VALUE(112),
    VELOCITY(107);
    private final int code;

    @Nullable
    public static IndexDataCodeEnum getByCode(int code) {
        for (IndexDataCodeEnum value : IndexDataCodeEnum.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        log.warn(LogEnum.INVALID_CODE.getPrint(), code);
        return null;
    }
}
