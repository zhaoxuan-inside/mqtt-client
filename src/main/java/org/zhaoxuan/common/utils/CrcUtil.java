package org.zhaoxuan.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.zhaoxuan.common.enums.LogEnum;

import java.util.zip.CRC32;

@Slf4j
public class CrcUtil {
    public static boolean checkCRC(Long crc, byte[] bytes) {
        CRC32 crc32 = new CRC32();
        crc32.update(bytes);
        if (crc32.getValue() == crc) {
            return true;
        }
        log.info(LogEnum.CRC_CHECK_FAILURE.getPrint(), crc, bytes);
        return false;
    }
}
