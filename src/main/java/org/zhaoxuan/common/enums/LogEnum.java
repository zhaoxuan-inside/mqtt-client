package org.zhaoxuan.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LogEnum {
    RECEIVE_MESSAGE("ReceiveMessage.Topic:[{}],Payload:[{}]."),
    INVALID_CODE("InvalidCode.Code:[{}]"),
    INVALID_TOPIC("InvalidTopic.Topic:[{}]"),
    INVALID_DIR("InvalidDir.Dir:[{}]"),
    INVALID_CH_TYPE("InvalidChType.ChType:[{}]"),
    EMPTY_PAYLOAD("PayloadIsEmpty.Topic:[{}]."),
    BYTES_NOT_FOUND("NotFoundBytesByKey.Key:[{}]."),
    CRC_CHECK_FAILURE("CRCCheckFailure.CRC:[{}],Data:[{}]."),
    NOT_FOUND_TIME("NotFoundTime.Topic:[{}]."),
    NOT_FOUND_VALUE("NotFoundValue.Topic:[{}]."),
    NOT_FOUND_MEASURING_POINT_ID("NotFoundMeasuringPointId.ChNo:[{}],Axial:[{}]"),

    INVALID_RESPONSE("InvalidResponse.Receive:[{}],Status:[{}]."),

    UPLOAD_FILE_FAILURE("UploadFileFailure.Key:[{}].");
    private final String print;
}
