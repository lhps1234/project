package com.example.board.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Review {
    private Long review_id;
    private String member_id;
    private String title;
    private String contents;
    private Long hit;
    private Long review_like;
    private LocalDateTime created_time;

//    public static BoardUpdateForm toBoardUpdateForm(Review board) {
//        BoardUpdateForm boardUpdateForm = new BoardUpdateForm();
//        boardUpdateForm.setBoard_id(board.getBoard_id());
//        boardUpdateForm.setTitle(board.getTitle());
//        boardUpdateForm.setContents(board.getContents());
//        boardUpdateForm.setMember_id(board.getMember_id());
//        boardUpdateForm.setHit(board.getHit());
//        boardUpdateForm.setCreated_time(board.getCreated_time());
//        return boardUpdateForm;
//    }

    public void addHit() {
        this.hit++;
    }
}
