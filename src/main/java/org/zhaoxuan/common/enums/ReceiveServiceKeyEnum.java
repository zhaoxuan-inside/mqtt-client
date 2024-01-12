package org.zhaoxuan.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceKeyEnum {
    REGISTER("Register"),
    SYSTEM_PARAM("SysPara"),
    COLLECT_PARAM("CollectPara"),
    WAVE_DATA("WaveData"),
    CHECK_DATA("CheckData"),
    SENSOR_INFO("SensorInfo"),
    BATTERY_INFO("BatteryInfo"),
    LOG_FILE("LogFile"),
    UPDATE_FILE_LIST("UpdateFileList"),
    UPDATE_FILE("UpdateFile"),
    


    private final String serviceKey;
}
