package com.pet.webservice.dto;

import lombok.Data;

import java.util.Set;

@Data
public class PostDTO {
    private Long id;
    private String title;
    private String caption;
    private Integer likes;
    private Integer dislikes;
    private Set<String> usersLiked;
}
