package org.example.nowcoder.entity;

import lombok.Data;

import java.util.Date;

@Data

public class DiscussPost {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column discuss_post.id
     *
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column discuss_post.user_id
     *
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    private Integer userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column discuss_post.title
     *
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    private String title;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column discuss_post.type
     *
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    private Integer type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column discuss_post.status
     *
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    private Integer status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column discuss_post.create_time
     *
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column discuss_post.comment_count
     *
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    private Integer commentCount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column discuss_post.score
     *
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    private Double score;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column discuss_post.content
     *
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    private String content;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column discuss_post.id
     *
     * @return the value of discuss_post.id
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column discuss_post.id
     *
     * @param id the value for discuss_post.id
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column discuss_post.user_id
     *
     * @return the value of discuss_post.user_id
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column discuss_post.user_id
     *
     * @param userId the value for discuss_post.user_id
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column discuss_post.title
     *
     * @return the value of discuss_post.title
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column discuss_post.title
     *
     * @param title the value for discuss_post.title
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column discuss_post.type
     *
     * @return the value of discuss_post.type
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column discuss_post.type
     *
     * @param type the value for discuss_post.type
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column discuss_post.status
     *
     * @return the value of discuss_post.status
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column discuss_post.status
     *
     * @param status the value for discuss_post.status
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column discuss_post.create_time
     *
     * @return the value of discuss_post.create_time
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column discuss_post.create_time
     *
     * @param createTime the value for discuss_post.create_time
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column discuss_post.comment_count
     *
     * @return the value of discuss_post.comment_count
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    public Integer getCommentCount() {
        return commentCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column discuss_post.comment_count
     *
     * @param commentCount the value for discuss_post.comment_count
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column discuss_post.score
     *
     * @return the value of discuss_post.score
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    public Double getScore() {
        return score;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column discuss_post.score
     *
     * @param score the value for discuss_post.score
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    public void setScore(Double score) {
        this.score = score;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column discuss_post.content
     *
     * @return the value of discuss_post.content
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    public String getContent() {
        return content;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column discuss_post.content
     *
     * @param content the value for discuss_post.content
     * @mbggenerated Fri Aug 11 13:17:41 CST 2023
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}