package com.web.note.entity;

import java.util.Date;

public class Document {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column document.document_id
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    private String documentId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column document.collection_id
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    private String collectionId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column document.user_id
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    private String userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column document.document_name
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    private String documentName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column document.document_type
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    private String documentType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column document.sort_order
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    private Integer sortOrder;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column document.created_at
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    private Date createdAt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column document.updated_at
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    private Date updatedAt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column document.document_content
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    private String documentContent;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column document.document_id
     *
     * @return the value of document.document_id
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    public String getDocumentId() {
        return documentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column document.document_id
     *
     * @param documentId the value for document.document_id
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    public void setDocumentId(String documentId) {
        this.documentId = documentId == null ? null : documentId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column document.collection_id
     *
     * @return the value of document.collection_id
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    public String getCollectionId() {
        return collectionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column document.collection_id
     *
     * @param collectionId the value for document.collection_id
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId == null ? null : collectionId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column document.user_id
     *
     * @return the value of document.user_id
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column document.user_id
     *
     * @param userId the value for document.user_id
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column document.document_name
     *
     * @return the value of document.document_name
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    public String getDocumentName() {
        return documentName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column document.document_name
     *
     * @param documentName the value for document.document_name
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    public void setDocumentName(String documentName) {
        this.documentName = documentName == null ? null : documentName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column document.document_type
     *
     * @return the value of document.document_type
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column document.document_type
     *
     * @param documentType the value for document.document_type
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    public void setDocumentType(String documentType) {
        this.documentType = documentType == null ? null : documentType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column document.sort_order
     *
     * @return the value of document.sort_order
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    public Integer getSortOrder() {
        return sortOrder;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column document.sort_order
     *
     * @param sortOrder the value for document.sort_order
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column document.created_at
     *
     * @return the value of document.created_at
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column document.created_at
     *
     * @param createdAt the value for document.created_at
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column document.updated_at
     *
     * @return the value of document.updated_at
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column document.updated_at
     *
     * @param updatedAt the value for document.updated_at
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column document.document_content
     *
     * @return the value of document.document_content
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    public String getDocumentContent() {
        return documentContent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column document.document_content
     *
     * @param documentContent the value for document.document_content
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    public void setDocumentContent(String documentContent) {
        this.documentContent = documentContent == null ? null : documentContent.trim();
    }
}