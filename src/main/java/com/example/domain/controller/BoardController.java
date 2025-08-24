package com.example.domain.controller;

import com.example.domain.dto.common.request.PageRequest;
import com.example.domain.dto.common.response.ApiResponse;
import com.example.domain.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ApiResponse getBoardList(PageRequest pageRequest) {
        return ApiResponse.of(boardService.getBoardList(pageRequest));
    }

    @PostMapping
    public ApiResponse createBoard(@RequestParam String name) {
        return ApiResponse.of(boardService.createBoard(name));
    }

}
