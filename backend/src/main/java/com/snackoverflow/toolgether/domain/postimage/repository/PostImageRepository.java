package com.snackoverflow.toolgether.domain.postimage.repository;

import com.snackoverflow.toolgether.domain.postimage.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    List<PostImage> findAllByPostId(Long postId); // 특정 게시물의 이미지 리스트 조회

    List<PostImage> findByPostId(Long postId);
    void deleteByPostId(Long postId);

    @Query("SELECT pi.imageUrl FROM PostImage pi")
    List<String> findAllImageUrl();
}
