package com.example.domain.service;

import com.example.domain.dto.common.request.PageRequest;
import com.example.domain.dto.common.response.PageResponse;
import com.example.domain.entity.Board;
import com.example.domain.repository.BoardRepository;
import com.example.global.exception.DataNotFoundException;
import com.example.global.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.domain.dto.common.request.PageRequest.toPageable;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public PageResponse<Board> getBoardList(PageRequest pageRequest) {
        // 공통 유틸리티 사용
        Pageable pageable = toPageable(pageRequest);
        Page<Board> boardPage = boardRepository.findAll(pageable);

        // 공통 응답 변환
        return PageResponse.of(pageRequest.pageSize(), boardPage);
    }

    public void validateBoardSeq(long boardSeq) {
        if (!boardRepository.existsById(boardSeq)) {
            throw new DataNotFoundException();
        }
    }

    public Board get(long id) {
        return boardRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public Board createBoard(String name) {
        return boardRepository.save(Board.from(name));
    }

    @Transactional
    public Board updateBoard(Long boardId, String newName) {
        Board board = get(boardId);
        board.updateName(newName);
        return boardRepository.save(board);
    }

        public boolean isBoardExists(String name) {
        return boardRepository.findAll().stream().anyMatch(board -> board.hasName(name));
    }

}
