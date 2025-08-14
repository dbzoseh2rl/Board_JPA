package com.example.board.service.impl;

import com.example.board.model.Board;
import com.example.board.repository.BoardRepository;
import com.example.board.service.BoardService;
import com.example.common.dto.PageList;
import com.example.common.dto.PageRequest;
import com.example.common.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Override
    public PageList<Board> getBoardList(PageRequest pageRequest) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.getPageIndex() - 1,
                pageRequest.getPageSize()
        );
        Page<Board> boardPage = boardRepository.findAll(pageable);

        long totalCount = boardPage.getTotalElements();
        List<Board> boardList = boardPage.getContent();

        return new PageList<>(pageRequest.getPageSize(), (int) totalCount, boardList);
    }

    @Override
    public void validateBoardSeq(long boardSeq) {
        if (!boardRepository.existsById(boardSeq)) {
            throw new DataNotFoundException();
        }
    }
}
