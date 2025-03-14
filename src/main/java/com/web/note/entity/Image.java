package com.web.note.entity;

import java.util.Date;

public class Image {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column image.image_id
     *
     * @mbggenerated Mon Feb 10 14:57:59 CST 2025
     */
    private String imageId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column image.document_id
     *
     * @mbggenerated Mon Feb 10 14:57:59 CST 2025
     */
    private String documentId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column image.collection_id
     *
     * @mbggenerated Mon Feb 10 14:57:59 CST 2025
     */
    private String collectionId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column image.user_id
     *
     * @mbggenerated Mon Feb 10 14:57:59 CST 2025
     */
    private String userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column image.image_name
     *
     * @mbggenerated Mon Feb 10 14:57:59 CST 2025
     */
    private String imageName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column image.image_size
     *
     * @mbggenerated Mon Feb 10 14:57:59 CST 2025
     */
    private Integer imageSize;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column image.created_at
     *
     * @mbggenerated Mon Feb 10 14:57:59 CST 2025
     */
    private Date createdAt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column image.pending_delete_at
     *
     * @mbggenerated Mon Feb 10 14:57:59 CST 2025
     */
    private Date pendingDeleteAt;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column image.image_id
     *
     * @return the value of image.image_id
     *
     * @mbggenerated Mon Feb 10 14:57:59 CST 2025
     */
    public String getImageId() {
        return imageId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column image.image_id
     *
     * @param imageId the value for image.image_id
     *
     * @mbggenerated Mon Feb 10 14:57:59 CST 2025
     */
    public void setImageId(String imageId) {
        this.imageId = imageId == null ? null : imageId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column image.document_id
     *
     * @return the value of image.document_id
     *
     * @mbggenerated Mon Feb 10 14:57:59 CST 2025
     */
    public String getDocumentId() {
        return documentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column image.document_id
     *
     * @param documentId the value for image.document_id
     *
     * @mbggenerated Mon Feb 10 14:57:59 CST 2025
     */
    public void setDocumentId(String documentId) {
        this.documentId = documentId == null ? null : documentId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column image.collection_id
     *
     * @return the value of image.collection_id
     *
     * @mbggenerated Mon Feb 10 14:57:59 CST 2025
     */
    public String getCollectionId() {
        return collectionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column image.collection_id
     *
     * @param collectionId the value for image.collection_id
     *
     * @mbggenerated Mon Feb 10 14:57:59 CST 2025
     */
    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId == null ? null : collectionId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column image.user_id
     *
     * @return the value of image.user_id
     *
     * @mbggenerated Mon Feb 10 14:57:59 CST 2025
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column image.user_id
     *
     * @param userId the value for image.user_id
     *
     * @mbggenerated Mon Feb 10 14:57:59 CST 2025
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column image.image_name
     *
     * @return the value of image.image_name
     *
     * @mbggenerated Mon Feb 10 14:57:59 CST 2025
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column image.image_name
     *
     * @param imageName the value for image.image_name
     *
     * @mbggenerated Mon Feb 10 14:57:59 CST 2025
     */
    public void setImageName(String imageName) {
        this.imageName = imageName == null ? null : imageName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column image.image_size
     *
     * @return the value of image.image_size
     *
     * @mbggenerated Mon Feb 10 14:57:59 CST 2025
     */
    public Integer getImageSize() {
        return imageSize;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column image.image_size
     *
     * @param imageSize the value for image.image_size
     *
     * @mbggenerated Mon Feb 10 14:57:59 CST 2025
     */
    public void setImageSize(Integer imageSize) {
        this.imageSize = imageSize;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column image.created_at
     *
     * @return the value of image.created_at
     *
     * @mbggenerated Mon Feb 10 14:57:59 CST 2025
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column image.created_at
     *
     * @param createdAt the value for image.created_at
     *
     * @mbggenerated Mon Feb 10 14:57:59 CST 2025
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column image.pending_delete_at
     *
     * @return the value of image.pending_delete_at
     *
     * @mbggenerated Mon Feb 10 14:57:59 CST 2025
     */
    public Date getPendingDeleteAt() {
        return pendingDeleteAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column image.pending_delete_at
     *
     * @param pendingDeleteAt the value for image.pending_delete_at
     *
     * @mbggenerated Mon Feb 10 14:57:59 CST 2025
     */
    public void setPendingDeleteAt(Date pendingDeleteAt) {
        this.pendingDeleteAt = pendingDeleteAt;
    }
}