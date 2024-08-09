package com.befriend.detour.domain.file.repository;

import com.befriend.detour.domain.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    Optional<File> findByFileUrl(String fileUrl);

    void deleteByFileUrl(String fileUrl);

    List<File> findByMarkerId(Long markerId);

}
