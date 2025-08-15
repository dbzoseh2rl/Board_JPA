package com.example.domain.service;

import com.example.domain.dto.common.response.PageResponse;
import com.example.domain.dto.common.request.PageRequest;
import com.example.domain.entity.Board;
import com.example.domain.repository.BoardRepository;
import com.example.global.common.exception.DataNotFoundException;
import com.example.global.common.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService{

    private final BoardRepository boardRepository;

    public PageResponse<Board> getBoardList(PageRequest pageRequest) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.pageIndex() - 1,  // record getter 사용
                pageRequest.pageSize()
        );
        Page<Board> boardPage = boardRepository.findAll(pageable);

        long totalCount = boardPage.getTotalElements();
        List<Board> boardList = boardPage.getContent();

        return PageResponse.of(pageRequest.pageSize(), (int) totalCount, boardList);
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
        // Entity의 정적 팩토리 메서드 사용
        Board board = Board.from(name);
        return boardRepository.save(board);
    }

    @Transactional
    public Board updateBoard(Long boardId, String newName) {
        Board board = get(boardId);
        board.updateName(newName);
        return boardRepository.save(board);
    }

    public boolean isBoardExists(String name) {
        // 방법 1: findAll()을 사용하여 이름으로 검색
        return boardRepository.findAll().stream()
                .anyMatch(board -> board.hasName(name));
        
        // 방법 2: Repository에 findByName 메서드가 있다면
        // return boardRepository.findByName(name).isPresent();
        
        // 방법 3: Repository에 existsByName 메서드가 있다면
        // return boardRepository.existsByName(name);
    }
}
