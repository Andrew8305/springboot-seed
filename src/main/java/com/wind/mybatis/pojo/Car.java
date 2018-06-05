package com.wind.mybatis.pojo;

import java.util.Date;
import javax.persistence.*;

public class Car {
    @Id
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "car_number")
    private String carNumber;

    @Column(name = "car_type")
    private String carType;

    private String owner;

    @Column(name = "license_picture")
    private String licensePicture;

    @Column(name = "vin_code")
    private String vinCode;

    @Column(name = "engine_code")
    private String engineCode;

    @Column(name = "register_date")
    private Date registerDate;

    @Column(name = "issue_date")
    private Date issueDate;

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
     * @return car_number
     */
    public String getCarNumber() {
        return carNumber;
    }

    /**
     * @param carNumber
     */
    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    /**
     * @return car_type
     */
    public String getCarType() {
        return carType;
    }

    /**
     * @param carType
     */
    public void setCarType(String carType) {
        this.carType = carType;
    }

    /**
     * @return owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @return license_picture
     */
    public String getLicensePicture() {
        return licensePicture;
    }

    /**
     * @param licensePicture
     */
    public void setLicensePicture(String licensePicture) {
        this.licensePicture = licensePicture;
    }

    /**
     * @return vin_code
     */
    public String getVinCode() {
        return vinCode;
    }

    /**
     * @param vinCode
     */
    public void setVinCode(String vinCode) {
        this.vinCode = vinCode;
    }

    /**
     * @return engine_code
     */
    public String getEngineCode() {
        return engineCode;
    }

    /**
     * @param engineCode
     */
    public void setEngineCode(String engineCode) {
        this.engineCode = engineCode;
    }

    /**
     * @return register_date
     */
    public Date getRegisterDate() {
        return registerDate;
    }

    /**
     * @param registerDate
     */
    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    /**
     * @return issue_date
     */
    public Date getIssueDate() {
        return issueDate;
    }

    /**
     * @param issueDate
     */
    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }
}