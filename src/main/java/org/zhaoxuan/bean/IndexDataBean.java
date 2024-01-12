package org.zhaoxuan.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexDataBean {
    private Integer ChNo;
    private Integer ChType;
    private List<Timestamp> Time;
    private List<IndexDataContentBean> Content;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IndexDataContentBean {
        private Integer Dir;
        private Integer Code;
        private List<Double> Values;
    }
}
