package com.example.desk0018.Comment;

public class Comment {
    private int commentEa;  // 댓글 수
    private String nickname;  // 닉네임
    private String profileImage;  // 프로필 이미지 URL
    private String content;  // 댓글 내용
    private boolean isLiked;  // 좋아요 여부
    private int likeCount;  // 좋아요 수
    private int parentCommentId;  // 대댓글 부모 ID

    // 생성자
    public Comment(int commentEa, String nickname, String profileImage,
                   String content, boolean isLiked, int likeCount, int parentCommentId) {
        this.commentEa = commentEa;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.content = content;
        this.isLiked = isLiked;
        this.likeCount = likeCount;
        this.parentCommentId = parentCommentId;
    }

    // Getter 메서드
    public int getCommentEa() { return commentEa; }  // 수정된 댓글 수 필드
    public String getNickname() { return nickname; }
    public String getProfileImage() { return profileImage; }
    public String getContent() { return content; }
    public boolean isLiked() { return isLiked; }
    public int getLikeCount() { return likeCount; }
    public int getParentCommentId() { return parentCommentId; }

    // Setter 메서드
    public void setLiked(boolean liked) { isLiked = liked; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }
    public void setContent(String content) { this.content = content; }
}
