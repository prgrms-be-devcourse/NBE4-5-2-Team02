package com.snackoverflow.toolgether.domain.post.dto;

import com.snackoverflow.toolgether.domain.post.entity.enums.PriceType;
import com.snackoverflow.toolgether.domain.post.entity.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostCreateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private Category category;

    @NotNull
    private PriceType priceType;

    @NotNull
    private Integer price;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;
}

