package com.example.bootproject.post.domain;


import com.example.bootproject.post.dto.PostDetailDTO;
import com.example.bootproject.post.service.PostService;
import lombok.*;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Post extends BaseEntity{

    private Long id;
    private String title;
    private String content;


    public static class PostBuilder{

        public PostBuilder title(String title){
            if(!title.matches("^.{3,200}")){
                throw new IllegalArgumentException("제목 200글자 이하로");
            }
            this.title = title;
            return this;
        }

        public PostBuilder content(String content){
            if(!title.matches("^.{0,1000}")){
                throw new IllegalArgumentException("게시글 1000글자 이내로");
            }
            this.content = content;
            return this;
        }
    }
}
