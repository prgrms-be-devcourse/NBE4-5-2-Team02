package com.snackoverflow.toolgether.domain.post.dto;

import com.snackoverflow.toolgether.domain.post.entity.enums.Category;
import com.snackoverflow.toolgether.domain.post.entity.enums.PriceType;
import com.snackoverflow.toolgether.domain.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private Category category;
    private PriceType priceType;
    private int price;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;
    private int viewCount;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.priceType = post.getPriceType();
        this.price = post.getPrice();
        this.latitude = post.getLatitude();
        this.longitude = post.getLongitude();
        this.createdAt = post.getCreatedAt();
        this.viewCount = post.getViewCount();
    }
}
