package com.syh.gptblog.repository;

import com.syh.gptblog.domain.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {

}
