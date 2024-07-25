package com.befriend.detour.domain.file.repository;

import com.befriend.detour.domain.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    void deleteByFileUrl(String fileUrl);
    List<File> findByMarkerId(Long markerId);

}
