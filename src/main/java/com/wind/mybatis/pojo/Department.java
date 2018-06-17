package com.wind.mybatis.pojo;

import javax.persistence.*;

public class Department {
    @Id
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "department_name")
    private String departmentName;

    private String duty;

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
     * @return department_name
     */
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * @param departmentName
     */
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /**
     * @return duty
     */
    public String getDuty() {
        return duty;
    }

    /**
     * @param duty
     */
    public void setDuty(String duty) {
        this.duty = duty;
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