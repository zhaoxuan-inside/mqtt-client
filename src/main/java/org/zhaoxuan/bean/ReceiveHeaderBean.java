package org.zhaoxuan.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.zhaoxuan.common.enums.ReceiveServiceKeyEnum;
import reactor.util.annotation.Nullable;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiveHeaderBean {
    private String prefix;
    private String sign;
    private String code;
    private ReceiveServiceKeyEnum serviceKey;
    private Integer status;

    public String toPath() {
        return prefix + "/" + sign + "/" + code + "/" + serviceKey;
    }

    @Nullable
    public ReceiveHeaderBean toBean(String content) {
        String[] split = content.split("/");
        if (split.length != 5) {
            return null;
        }
        prefix = split[0];
        sign = split[1];
        code = split[2];
        try {
            serviceKey = ReceiveServiceKeyEnum.getByKey(split[3]);
        } catch (Exception ex) {
            log.warn("", ex);
            return null;
        }
        status = Integer.parseInt(split[4]);
        return this;
    }
}
