package org.zhaoxuan.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaveDataBean {
    private Integer Id;
    private Integer PackCount;
    private Integer PackIndex;
    private Integer ChNo;
    private Integer Dir;
    private Integer Code;
    private Timestamp Time;
    private Integer PlusLength;
    private Integer Freq;
    private Integer RPM;
    private Integer TimeMark;
    private Double Coef;
    private Integer Src;
    private Integer IsSensor;
    private Long CRC;
    private String Content;
}
