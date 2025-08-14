package com.example.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.validation.constraints.NotEmpty;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentBody {
    @NotEmpty
    private String content;
}
