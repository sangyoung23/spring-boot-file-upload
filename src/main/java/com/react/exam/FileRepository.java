package com.react.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    // 추가적인 쿼리 메소드 필요시 작성
    @Query("SELECT f.id AS id, f.fileName AS fileName FROM File f")
    List<FileInfoProjection> findFileInfo();
}
