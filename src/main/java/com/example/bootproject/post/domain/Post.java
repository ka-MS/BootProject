package com.example.bootproject.post.domain;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Post extends BaseEntity{

    private Long id;
    private String title;
    private String content;

    public void checkTitle(String title){
        if(!title.matches("^.{3,200}")){
            throw new IllegalArgumentException("");
        }
        this.title = title;
    }

    public void checkContent(String content){
        if(!title.matches("^.{0,200}")){
            throw new IllegalArgumentException("");
        }
        this.content = content;
    }
}
