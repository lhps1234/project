package com.example.board.service;

import com.example.board.model.AttachedImg;
import com.example.board.model.Review;
import com.example.board.model.board.AttachedFile;
import com.example.board.model.board.Board;
import com.example.board.repository.ReviewMapper;
import com.example.board.util.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewMapper reviewMapper;
    private final FileService fileService;

    @Value("${file.upload.path}")
    private String uploadPath;

    public void saveReview(Review review, MultipartFile file) {

    	reviewMapper.saveReview(review);
        // 파일을 저장한다.
        if (file != null && file.getSize() > 0) {
        	AttachedImg attachedImg = fileService.saveFile(file);
        	attachedImg.setReview_id(review.getReview_id());
            reviewMapper.saveImg(attachedImg);
        }
    }

    public List<Review> findReviews(String searchText, int startRecord, int countPerPage) {
        // 전체 검색 결과 중 시작 위치와 갯수
        RowBounds rowBounds = new RowBounds(startRecord, countPerPage);
        return reviewMapper.findReviews(searchText, rowBounds);
    }

    public Board findBoard(Long board_id) {
        return reviewMapper.findBoard(board_id);
    }

    public Board readBoard(Long board_id) {
        Board board = findBoard(board_id);
        board.addHit();
        updateBoard(board, false, null);
        return board;
    }

    @Transactional
    public void updateBoard(Board updateBoard, boolean isFileRemoved, MultipartFile file) {
        Board board = reviewMapper.findBoard(updateBoard.getBoard_id());
        if (board != null) {
            reviewMapper.updateBoard(board);
            // 첨부파일 정보를 가져온다.
            AttachedFile attachedFile = reviewMapper.findFileByBoardId(updateBoard.getBoard_id());
            if (attachedFile != null && (isFileRemoved || (file != null && file.getSize() > 0))) {
                // 파일 삭제를 요청했거나 새로운 파일이 업로드 되면 기존 파일을 삭제한다.
                removeAttachedFile(attachedFile.getAttachedFile_id());
            }
            // 새로 저장할 파일이 있으면 저장한다.
            if (file != null && file.getSize() > 0) {
                // 첨부파일을 서버에 저장한다.
                AttachedImg savedFile = fileService.saveFile(file);
//                savedFile.setReview_id(updateBoard.getReview_id());
                reviewMapper.saveImg(savedFile);
            }
        }
    }

    @Transactional
    public void removeAttachedFile(Long attachedFile_id) {
        AttachedFile attachedFile = reviewMapper.findFileByAttachedFileId(attachedFile_id);
        if (attachedFile != null) {
            String fullPath = uploadPath + "/" + attachedFile.getSaved_filename();
            fileService.deleteFile(fullPath);
            log.info("기존 파일 삭제: {}", attachedFile);
            reviewMapper.removeAttachedFile(attachedFile.getAttachedFile_id());
        }
    }

    public void removeBoard(Long board_id) {
        AttachedFile attachedFile = findFileByBoardId(board_id);
        if (attachedFile!= null) {
            removeAttachedFile(attachedFile.getAttachedFile_id());
        }
        reviewMapper.removeBoard(board_id);
    }

    public AttachedFile findFileByBoardId(Long board_id) {
        return reviewMapper.findFileByBoardId(board_id);
    }

    public AttachedFile findFileByAttachedFileId(Long attachedFile_id) {
        return reviewMapper.findFileByAttachedFileId(attachedFile_id);
    }

    public int getTotal(String searchText) {
        return reviewMapper.getTotal(searchText);
    }
}
