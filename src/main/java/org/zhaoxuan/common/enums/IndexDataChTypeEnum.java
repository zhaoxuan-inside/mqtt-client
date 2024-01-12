package org.zhaoxuan.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
public enum IndexDataChTypeEnum {
    SHAKE(0),
    ROTATE_SPEED(1),
    TECHNICS(2),
    TEMPERATURE(3),
    AUDIO(4),
    RTU(5);
    private final int type;

    public static IndexDataChTypeEnum getByType(int type) {
        for (IndexDataChTypeEnum value : IndexDataChTypeEnum.values()) {
            if (value.getType() == type) {
                return value;
            }
        }
        log.info(LogEnum.INVALID_CH_TYPE.getPrint(), type);
        return null;
    }
}
