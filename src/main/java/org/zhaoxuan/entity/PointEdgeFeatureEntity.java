package org.zhaoxuan.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zhaoxuan.bean.IndexDataExpandBean;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "point_edge_feature_test", schema = "public", autoResultMap = true)
public class PointEdgeFeatureEntity implements Cloneable {
    private UUID id;
    private UUID measuringPointId;
    private Double accelerationRms;
    private Double speedRms;
    private Double temperature;
    private Double rpmRms;
    private Timestamp timestamp;
    private Boolean isDelete;
    private Timestamp createdAt;
    private Timestamp updateAt;

    @Override
    public PointEdgeFeatureEntity clone() {
        try {
            return (PointEdgeFeatureEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
