package org.zhaoxuan.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "point_vibrate_test", schema = "public", autoResultMap = true)
public class PointVibrateEntity {
    private UUID id;
    private UUID measuringPointVibrateId;
    private String acceleration;
    private Integer samplingRate;
    private Integer samplingNumber;
    private String accelerationLow;
    private Integer samplingRateLow;
    private Integer samplingNumberLow;
    private String velocity;
    private Integer samplingRateVelocity;
    private Integer samplingNumberVelocity;
    private String accelerationUnprocessed;
    private Integer rpm;
    private Timestamp timestamp;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
