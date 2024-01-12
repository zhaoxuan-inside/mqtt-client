package org.zhaoxuan.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import reactor.util.annotation.Nullable;

@Getter
@AllArgsConstructor
public enum IndexDataEnum {
    DEFAULT(0),
    TEMPERATURE(100),
    LOW_FREQ_VALUE(101),
    HIGH_FREQ_VALUE(112),
    VELOCITY(107);
    private final int code;

    @Nullable
    public IndexDataEnum getByCode(int code) {
        for (IndexDataEnum value : IndexDataEnum.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
}
