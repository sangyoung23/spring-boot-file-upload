package com.react.exam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    private final String uploadDir = "C:/uploads"; // 파일을 젖아할 디렉토리 경로

    // 파일 조회
    public List<FileInfoProjection> getFile() {
        return fileRepository.findFileInfo();
    }

    // 파일 업로드
    public File uploadFile(MultipartFile file) throws IOException {
        // 파일 정보 객체 생성
        File uploadedFile = new File();
        uploadedFile.setFileName(file.getOriginalFilename());
        uploadedFile.setContentType(file.getContentType());

        // 파일을 저장할 디렉토리가 없으면 생성
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 파일 경로 설정
        Path filePath = uploadPath.resolve(file.getOriginalFilename());
        // 파일을 지정한 경로에 저장
        Files.copy(file.getInputStream(), filePath);

        // 데이터베이스에 파일 메타데이터 저장
        uploadedFile.setData(file.getBytes());
        return fileRepository.save(uploadedFile);
    }

    // 파일 다운로드
    public ResponseEntity<Resource> downloadFile(Long id) {
        // 파일 정보 조회
        File uploadedFile = fileRepository.findById(id).orElse(null);
        if (uploadedFile == null) {
            return ResponseEntity.notFound().build();
        }

        // 파일 경로 설정
        Path filePath = Paths.get(uploadDir, uploadedFile.getFileName());
        Resource resource = new FileSystemResource(filePath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + uploadedFile.getFileName() + "\"")
                .body(resource);
    }
}
