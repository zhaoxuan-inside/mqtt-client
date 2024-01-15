package org.zhaoxuan.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.zhaoxuan.common.enums.ReceiveServiceKeyEnum;
import org.zhaoxuan.common.enums.ResponseEnum;
import reactor.util.annotation.Nullable;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendHeaderBean {
    private String prefix;
    private String code;
    private ReceiveServiceKeyEnum serviceKey;
    private ResponseEnum status;

    public String toPath() {
        return prefix + "/" + code + "/" + serviceKey.getServiceKey() + "/" + status.getCode();
    }

    @Nullable
    public SendHeaderBean toBean(String content) {
        String[] split = content.split("/");
        if (split.length != 4) {
            return null;
        }
        prefix = split[0];
        code = split[1];
        try {
            serviceKey = ReceiveServiceKeyEnum.getByKey(split[2]);
            status = ResponseEnum.getByCode(Integer.parseInt(split[3]));
        } catch (Exception ex) {
            log.warn("", ex);
            return null;
        }
        return this;
    }

    @Nullable
    public SendHeaderBean getByReceive(ReceiveHeaderBean bean, int status) {
        prefix = bean.getPrefix();
        code = bean.getCode();
        serviceKey = bean.getServiceKey();
        ResponseEnum byCode = ResponseEnum.getByCode(status);
        if (ObjectUtils.isEmpty(byCode)) {
            return null;
        }
        this.status = byCode;
        return this;
    }

}
