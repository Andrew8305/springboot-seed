package com.wind.mybatis.pojo;

import javax.persistence.*;

@Table(name = "role_auth")
public class RoleAuth {
    @Id
    private Long id;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "auth_id")
    private Long authId;

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
     * @return role_id
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * @param roleId
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * @return auth_id
     */
    public Long getAuthId() {
        return authId;
    }

    /**
     * @param authId
     */
    public void setAuthId(Long authId) {
        this.authId = authId;
    }
}