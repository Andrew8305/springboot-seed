package com.wind.mybatis.pojo;

import javax.persistence.*;

public class Permission {
    @Id
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private String target;

    @Column(name = "permission_name")
    private String permissionName;

    private String comment;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return user_id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return target
     */
    public String getTarget() {
        return target;
    }

    /**
     * @param target
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * @return permission_name
     */
    public String getPermissionName() {
        return permissionName;
    }

    /**
     * @param permissionName
     */
    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    /**
     * @return comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}