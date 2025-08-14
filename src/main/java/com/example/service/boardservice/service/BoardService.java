package com.example.service.boardservice.service;

import com.example.model.Board;
import com.example.dto.PageList;
import com.example.dto.PageRequest;

import java.util.Optional;

public interface BoardService {
    PageList<Board> getBoardList(PageRequest pageRequest);

    void validateBoardSeq(long boardSeq);

    Optional<Board> get(long boardSeq);

}
