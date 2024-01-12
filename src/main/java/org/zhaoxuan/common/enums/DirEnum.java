package org.zhaoxuan.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.util.annotation.Nullable;

@Slf4j
@Getter
@AllArgsConstructor
public enum DirEnum {
    SINGLE(0, ""),
    Z_AXIS(1, "X"),
    X_AXIS(2, "Y"),
    Y_AXIS(3, "Z");
    private final int code;
    private final String point;

    @Nullable
    public static DirEnum getByCode(int code) {
        for (DirEnum value : DirEnum.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        log.info(LogEnum.INVALID_DIR.getPrint(), code);
        return null;
    }
}
