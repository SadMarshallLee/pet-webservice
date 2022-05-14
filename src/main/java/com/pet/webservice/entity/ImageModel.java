package com.pet.webservice.entity;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;

@Data
@Entity
public class ImageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    // for saving photo int blob format
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] imageBytes;
    /* for transfer ban to client part */
    @JsonIgnore
    private Long userId;
    @JsonIgnore
    private Long postId;
}
