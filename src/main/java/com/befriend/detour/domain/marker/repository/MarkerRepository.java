package com.befriend.detour.domain.marker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.error.Mark;

@Repository
public interface MarkerRepository extends JpaRepository<Mark, Long>, MarkerRepositoryCustom  {
}
