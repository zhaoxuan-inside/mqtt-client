package org.zhaoxuan.bean;

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
public class IndexDataExpandBean implements Cloneable {
    private UUID id;
    private Integer ChNo;
    private Integer ChType;
    private Timestamp Time;
    private Integer Dir;
    private Integer Code;
    private Double Value;

    @Override
    public IndexDataExpandBean clone() {
        try {
            return (IndexDataExpandBean) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

}
