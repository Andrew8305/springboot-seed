package com.wind.mybatis.pojo;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "car_fee")
public class CarFee {
    @Id
    private Long id;

    @Column(name = "park_Id")
    private Long parkId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "car_number")
    private String carNumber;

    @Column(name = "in_time")
    private Date inTime;

    @Column(name = "out_time")
    private Date outTime;

    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;

    @Column(name = "payment_mode")
    private String paymentMode;

    @Column(name = "payment_id")
    private Long paymentId;

    private String operator;

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
     * @return park_Id
     */
    public Long getParkId() {
        return parkId;
    }

    /**
     * @param parkId
     */
    public void setParkId(Long parkId) {
        this.parkId = parkId;
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
     * @return in_time
     */
    public Date getInTime() {
        return inTime;
    }

    /**
     * @param inTime
     */
    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    /**
     * @return out_time
     */
    public Date getOutTime() {
        return outTime;
    }

    /**
     * @param outTime
     */
    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    /**
     * @return payment_amount
     */
    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * @param paymentAmount
     */
    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    /**
     * @return payment_mode
     */
    public String getPaymentMode() {
        return paymentMode;
    }

    /**
     * @param paymentMode
     */
    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    /**
     * @return payment_id
     */
    public Long getPaymentId() {
        return paymentId;
    }

    /**
     * @param paymentId
     */
    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    /**
     * @return operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     * @param operator
     */
    public void setOperator(String operator) {
        this.operator = operator;
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