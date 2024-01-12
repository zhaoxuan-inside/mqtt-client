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
@TableName(value = "measuring_point_test", schema = "public", autoResultMap = true)
public class MeasuringPointEntity {
    private UUID id;
    private UUID sensorId;
    private String sensorAxial;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
