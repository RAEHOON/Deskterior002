package com.example.desk0018.Server;

import com.example.desk0018.Comment.Comment;

import java.util.List;

public class CommentResponse {
    private boolean success;
    private List<Comment> comments;

    public boolean isSuccess() { return success; }
    public List<Comment> getComments() { return comments; }
}
