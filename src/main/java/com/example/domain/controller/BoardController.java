package com.example.domain.controller;

import com.example.domain.dto.PageList;
import com.example.domain.dto.PageRequest;
import com.example.domain.entity.Board;
import com.example.domain.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping(value="")
    public PageList<Board> getBoardList(PageRequest pageRequest) {
        return boardService.getBoardList(pageRequest);
    }

}
