package org.zhaoxuan.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import reactor.util.annotation.Nullable;

@Getter
@AllArgsConstructor
public enum ReceiveServiceKeyEnum {
    REGISTER("Register"),
    SYSTEM_PARAM("SysPara"),
    COLLECT_PARAM("CollectPara"),
    WAVE_DATA("WaveData"),
    INDEX_DATA("IndexData"),
    CHECK_DATA("CheckData"),
    SENSOR_INFO("SensorInfo"),
    BATTERY_INFO("BatteryInfo"),
    LOG_FILE("LogFile"),
    UPDATE_FILE_LIST("UpdateFileList"),
    UPDATE_FILE("UpdateFile");

    private final String serviceKey;

    @Nullable
    public static ReceiveServiceKeyEnum getByKey(String key) {
        for (ReceiveServiceKeyEnum value : ReceiveServiceKeyEnum.values()) {
            if (value.getServiceKey().equals(key)) {
                return value;
            }
        }
        return null;
    }
}