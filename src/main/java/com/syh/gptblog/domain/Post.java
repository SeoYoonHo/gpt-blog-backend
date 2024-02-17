package com.syh.gptblog.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper=false)
public class Post extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    private String slug;
    private String title;
    @ElementCollection
    private List<String> categories = new ArrayList<>();
    private String cover;
    private String date;
    private String published;
    private String lastEditedAt;
    private String blurUrl;
    private String content;
}
