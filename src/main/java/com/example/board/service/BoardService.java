package com.example.board.service;

import com.example.board.model.Board;
import com.example.common.dto.PageList;
import com.example.common.dto.PageRequest;

import java.util.Optional;

public interface BoardService {
    PageList<Board> getBoardList(PageRequest pageRequest);

    void validateBoardSeq(long boardSeq);

    Optional<Board> get(long boardSeq);

}
