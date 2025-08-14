package com.example.controller;

import com.example.model.Board;
import com.example.service.boardservice.service.BoardService;
import com.example.dto.PageList;
import com.example.dto.PageRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value="/v1/boards")
@RequiredArgsConstructor
public class BoardController {
    final Logger logger = LoggerFactory.getLogger(BoardController.class);

    private final BoardService boardService;

    /**
     * 게시판 목록 조회
     *
     * @author hjkim
     * @param  pageRequest-pageIndex(nullable), pageSize(nullable)
     * @return Board-totalCount, totalPage, list
     */
    @GetMapping(value="")
    public PageList<Board> getBoardList(PageRequest pageRequest) {
        logger.info("getBoardList ::: {}", pageRequest);
        return boardService.getBoardList(pageRequest);
    }
}
