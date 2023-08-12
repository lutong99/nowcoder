package org.example.nowcoder.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.id
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.user_id
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    private Integer userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.entity_type
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    private Integer entityType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.entity_id
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    private Integer entityId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.target_id
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    private Integer targetId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.content
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    private String content;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.status
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    private Integer status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.create_time
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    private Date createTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comment.id
     *
     * @return the value of comment.id
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comment.id
     *
     * @param id the value for comment.id
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comment.user_id
     *
     * @return the value of comment.user_id
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comment.user_id
     *
     * @param userId the value for comment.user_id
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comment.entity_type
     *
     * @return the value of comment.entity_type
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    public Integer getEntityType() {
        return entityType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comment.entity_type
     *
     * @param entityType the value for comment.entity_type
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    public void setEntityType(Integer entityType) {
        this.entityType = entityType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comment.entity_id
     *
     * @return the value of comment.entity_id
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    public Integer getEntityId() {
        return entityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comment.entity_id
     *
     * @param entityId the value for comment.entity_id
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comment.target_id
     *
     * @return the value of comment.target_id
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    public Integer getTargetId() {
        return targetId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comment.target_id
     *
     * @param targetId the value for comment.target_id
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comment.content
     *
     * @return the value of comment.content
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    public String getContent() {
        return content;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comment.content
     *
     * @param content the value for comment.content
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comment.status
     *
     * @return the value of comment.status
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comment.status
     *
     * @param status the value for comment.status
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comment.create_time
     *
     * @return the value of comment.create_time
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comment.create_time
     *
     * @param createTime the value for comment.create_time
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}