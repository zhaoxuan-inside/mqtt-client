package org.zhaoxuan.controller;

import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.zhaoxuan.common.constants.HeaderConstant;
import org.zhaoxuan.common.utils.FileUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class FileModelController {

    private final FileUtil fileUtil;
    private final RestTemplate restTemplate;

    @Value("${file.server.address}")
    private String fileServerAddress;
    @Value("${file.server.asp.token}")
    private String fileServerEmpToken;

    private static final String DOWNLOAD_FAILURE = "下载失败";

    @GetMapping("/downloadFile")
    public void downloadFile(@RequestParam String fileUrl,
                             @RequestParam String name,
                             HttpServletRequest request,
                             HttpServletResponse response)
            throws IOException {

        if (ObjectUtils.isEmpty(fileUrl)) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(HeaderConstant.CONTENT_TYPE_JSON);
            response.getWriter().write(DOWNLOAD_FAILURE);
            return;
        }

        String fileName;
        if (!ObjectUtils.isEmpty(name)) {
            fileName = name + fileUrl.substring(fileUrl.lastIndexOf("."));
        } else {
            fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        }

        try {
            fileUtil.download(fileUrl, fileName, request, response);
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(HeaderConstant.CONTENT_TYPE_JSON);
            response.getWriter().write(DOWNLOAD_FAILURE);
        }
    }

    @GetMapping(value = "/get")
    public ResponseEntity<byte[]> getFile(@RequestParam(value = "filePath") String filePath) {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HeaderConstant.TOKEN, fileServerEmpToken);

        HttpEntity<Object> entity = new HttpEntity<>(null, headers);
        String imageUrl = fileServerAddress + "download?objectName=" + filePath;

        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(imageUrl, HttpMethod.GET, entity, byte[].class);
        if (responseEntity.getBody() == null || HttpStatus.OK.value() != responseEntity.getStatusCode().value()) {
            log.info("GetFileFailure.Response:[{}]", JSON.toJSONString(responseEntity.getBody()));
            return null;
        }

        return responseEntity;
    }
}
