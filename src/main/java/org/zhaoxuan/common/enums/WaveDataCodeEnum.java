package org.zhaoxuan.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import reactor.util.annotation.Nullable;

@Getter
@AllArgsConstructor
public enum WaveDataCodeEnum {
    AUDIO_WAVE(0),
    LENGTH_WAVE(1),
    HIGH_FREQ_ACC(11),
    LOW_FREQ_ACC(12),
    VELOCITY_WAVE(15);
    private final Integer code;

    @Nullable
    public static WaveDataCodeEnum getByCode(Integer code) {
        for (WaveDataCodeEnum value : WaveDataCodeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
