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
@TableName(value = "sensor_test", schema = "public", autoResultMap = true)
public class SensorEntity {
    private UUID id;
    private String serialNumber;
    private String chNo;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
