package com.react.exam;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "TB_UPLOAD_FILE_INFO")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName; // 파일 이름

    private String contentType; // 파일 타입

    @Lob // 대용량 데이터 저장을 위해 @Lob 어노테이션 사용
    private byte[] data; // 파일 데이터
}
