package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import com.example.demo.dto.OptionDTO;
import com.example.demo.entity.Title;

public interface TitleRepository extends JpaRepository<Title, Integer> {
    @Query("SELECT new com.example.demo.dto.OptionDTO(t.id, t.titleName) FROM Title t ORDER BY t.id")
    List<OptionDTO<Integer>> findAllAsOptions();

}
