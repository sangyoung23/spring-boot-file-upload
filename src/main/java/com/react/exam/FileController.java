package com.react.exam;

import org.apache.tomcat.jni.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    // 파일 조회
    @GetMapping("/files")
    public List<FileInfoProjection> getFiles() {
        return fileService.getFile();
    }


    // 파일 업로드
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> handleFileUpload(@RequestParam("file")MultipartFile file) {
            try {
                // 클라이언트에서 전달 받은 파일을 서비스로 전달
                File uploadedFile = fileService.uploadFile(file);

                Map<String, Object> result = new HashMap<>();

                result.put("fileId", uploadedFile.getId());
                result.put("fileName", uploadedFile.getFileName());

                return ResponseEntity.ok(result);
            } catch (IOException e) {
                e.printStackTrace();

                Map<String, Object> errorResponse = new HashMap<>();

                errorResponse.put("error", "File upload failed");
                errorResponse.put("message", e.getMessage());

                return ResponseEntity.status(500).body(errorResponse);
            }
        }

    // 파일 다운로드
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") Long id) {
        return fileService.downloadFile(id);
    }
}
