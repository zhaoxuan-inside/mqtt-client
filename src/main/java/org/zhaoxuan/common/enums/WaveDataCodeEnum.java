package org.zhaoxuan.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.util.annotation.Nullable;

@Slf4j
@Getter
@AllArgsConstructor
public enum WaveDataCodeEnum {
    AUDIO_WAVE(0),
    LENGTH_WAVE(1),
    HIGH_FREQ_ACC(11),
    LOW_FREQ_ACC(12),
    VELOCITY_WAVE(15);
    private final int code;

    @Nullable
    public static WaveDataCodeEnum getByCode(int code) {
        for (WaveDataCodeEnum value : WaveDataCodeEnum.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        log.info(LogEnum.INVALID_CODE.getPrint(), code);
        return null;
    }

}
