package org.zhaoxuan.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.zhaoxuan.common.constants.DateTimePattern;
import org.zhaoxuan.common.constants.FileSuffixConstant;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class SensorUtil {

    @Value("${file.save.base-path}")
    private String fileSaveBasePath;

    public String getFilePath(String serialNumber, String fileName) {
        String format = LocalDate.now().format(DateTimeFormatter.ofPattern(DateTimePattern.FILE_SAVE_PATH_DATA));
        return fileSaveBasePath + format + "/" + serialNumber + "/" + fileName + FileSuffixConstant.JSON_SUFFIX;
    }

}
