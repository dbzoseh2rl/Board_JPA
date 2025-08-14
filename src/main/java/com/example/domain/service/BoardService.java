package com.example.domain.service;

import com.example.domain.dto.PageList;
import com.example.domain.dto.PageRequest;
import com.example.domain.entity.Board;
import com.example.domain.repository.BoardRepository;
import com.example.global.common.exception.DataNotFoundException;
import com.example.global.common.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService{

    private final BoardRepository boardRepository;

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

    public void validateBoardSeq(long boardSeq) {
        if (!boardRepository.existsById(boardSeq)) {
            throw new DataNotFoundException();
        }
    }

    public Board get(long id) {
        return boardRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

}
