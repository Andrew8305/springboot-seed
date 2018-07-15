package com.wind.mybatis.pojo;

import javax.persistence.*;

public class Park {
    @Id
    private Long id;

    private String name;

    private String address;

    private String province;

    private String city;

    private String longitude;

    private String latitude;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "fee_id")
    private Long feeId;

    private Long parent;

    private String gates;

    private Short count;

    @Column(name = "remaining_count")
    private Short remainingCount;

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
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return province
     */
    public String getProvince() {
        return province;
    }

    /**
     * @param province
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * @param longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * @return latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * @param latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
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
     * @return fee_id
     */
    public Long getFeeId() {
        return feeId;
    }

    /**
     * @param feeId
     */
    public void setFeeId(Long feeId) {
        this.feeId = feeId;
    }

    /**
     * @return parent
     */
    public Long getParent() {
        return parent;
    }

    /**
     * @param parent
     */
    public void setParent(Long parent) {
        this.parent = parent;
    }

    /**
     * @return gates
     */
    public String getGates() {
        return gates;
    }

    /**
     * @param gates
     */
    public void setGates(String gates) {
        this.gates = gates;
    }

    /**
     * @return count
     */
    public Short getCount() {
        return count;
    }

    /**
     * @param count
     */
    public void setCount(Short count) {
        this.count = count;
    }

    /**
     * @return remaining_count
     */
    public Short getRemainingCount() {
        return remainingCount;
    }

    /**
     * @param remainingCount
     */
    public void setRemainingCount(Short remainingCount) {
        this.remainingCount = remainingCount;
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