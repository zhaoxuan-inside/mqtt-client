package org.zhaoxuan.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
@SuppressWarnings("unused")
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class FileUtil {

    private final RestTemplate restTemplate;

    @Value("${file.server.address}")
    private String fileServerAddress;

    @Value("${file.server.asp.token}")
    private String fileServerEmpToken;

    public String upload(String fileName, String fileIncPath, String content)
            throws Exception {
        ByteArrayInputStream fileStream = new ByteArrayInputStream(content.getBytes());
        MultipartFile multipartFile = new MockMultipartFile("acc.json", "acc.json", "", fileStream);
        return upload(multipartFile, fileIncPath);
    }

    public String upload(MultipartFile file, String fileNameIncPath) throws Exception {

        if (file.isEmpty()) {
            log.warn("FileIsEmpty.FileName:[{}].", fileNameIncPath);
            throw new RuntimeException("上传文件不能为空！");
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("token", fileServerEmpToken);

            String url = fileServerAddress + "upload";
            headers.setContentType(MediaType.parseMediaType("multipart/form-data; charset=UTF-8"));

            MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
            params.add("file", file.getResource());
            params.add("objectName", fileNameIncPath);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            log.info("UploadFileResult:[{}]", response.getBody());
            if (response.getBody() != null) {
                JSONObject result = JSON.parseObject(response.getBody());
                if (result.getInteger("data") != 1) {
                    log.warn("UploadFileFailure,FileName:[{}].", fileNameIncPath);
                    throw new RuntimeException("UploadFailure.");
                }
            }
            return fileNameIncPath;
        } catch (Exception ex) {
            log.warn("FileServerAbnormal.", ex);
            throw new Exception(ex);
        }
    }

    public void download(String fileAddress, String filename, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        InputStream inputStream = null;
        try {
            filename = URLEncoder.encode(filename, "UTF-8");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("multipart/form-data; charset=UTF-8"));
            headers.add("token", fileServerEmpToken);
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

            String url = fileServerAddress + "download?objectName=" + fileAddress;
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
            ResponseEntity<byte[]> fileResponse = restTemplate.exchange(url, HttpMethod.GET, requestEntity, byte[].class);

            inputStream = new ByteArrayInputStream(Objects.requireNonNull(fileResponse.getBody()));
            response.setCharacterEncoding(request.getCharacterEncoding());
            response.setContentType("application/octet-stream");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            log.warn("DownloadFileFailure.FileName:[{}]", filename, ex);
            throw new IOException(ex);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    log.warn("DownloadFileFailure.FileName:[{}]", filename, ex);
                }
            }
        }
    }

    public String getFileName(UUID id, String fileName) {
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        return id + suffix;
    }

}
