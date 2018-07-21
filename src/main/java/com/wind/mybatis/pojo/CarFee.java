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

    private Integer code;

    @Column(name = "in_time")
    private Date inTime;

    @Column(name = "out_time")
    private Date outTime;

    @Column(name = "payment_time")
    private Date paymentTime;

    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;

    @Column(name = "payment_mode")
    private String paymentMode;

    @Column(name = "payment_id")
    private Long paymentId;

    private BigDecimal cash;

    @Column(name = "cash_type")
    private Integer cashType;

    @Column(name = "in_operation_type")
    private Integer inOperationType;

    @Column(name = "in_operator")
    private String inOperator;

    @Column(name = "in_image_url")
    private String inImageUrl;

    @Column(name = "in_gate")
    private String inGate;

    @Column(name = "in_comment")
    private String inComment;

    @Column(name = "out_operation_type")
    private Integer outOperationType;

    @Column(name = "out_operator")
    private String outOperator;

    @Column(name = "out_image_url")
    private String outImageUrl;

    @Column(name = "out_gate")
    private String outGate;

    @Column(name = "out_comment")
    private String outComment;

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
     * @return code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(Integer code) {
        this.code = code;
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
     * @return payment_time
     */
    public Date getPaymentTime() {
        return paymentTime;
    }

    /**
     * @param paymentTime
     */
    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
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
     * @return cash
     */
    public BigDecimal getCash() {
        return cash;
    }

    /**
     * @param cash
     */
    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    /**
     * @return cash_type
     */
    public Integer getCashType() {
        return cashType;
    }

    /**
     * @param cashType
     */
    public void setCashType(Integer cashType) {
        this.cashType = cashType;
    }

    /**
     * @return in_operation_type
     */
    public Integer getInOperationType() {
        return inOperationType;
    }

    /**
     * @param inOperationType
     */
    public void setInOperationType(Integer inOperationType) {
        this.inOperationType = inOperationType;
    }

    /**
     * @return in_operator
     */
    public String getInOperator() {
        return inOperator;
    }

    /**
     * @param inOperator
     */
    public void setInOperator(String inOperator) {
        this.inOperator = inOperator;
    }

    /**
     * @return in_image_url
     */
    public String getInImageUrl() {
        return inImageUrl;
    }

    /**
     * @param inImageUrl
     */
    public void setInImageUrl(String inImageUrl) {
        this.inImageUrl = inImageUrl;
    }

    /**
     * @return in_gate
     */
    public String getInGate() {
        return inGate;
    }

    /**
     * @param inGate
     */
    public void setInGate(String inGate) {
        this.inGate = inGate;
    }

    /**
     * @return in_comment
     */
    public String getInComment() {
        return inComment;
    }

    /**
     * @param inComment
     */
    public void setInComment(String inComment) {
        this.inComment = inComment;
    }

    /**
     * @return out_operation_type
     */
    public Integer getOutOperationType() {
        return outOperationType;
    }

    /**
     * @param outOperationType
     */
    public void setOutOperationType(Integer outOperationType) {
        this.outOperationType = outOperationType;
    }

    /**
     * @return out_operator
     */
    public String getOutOperator() {
        return outOperator;
    }

    /**
     * @param outOperator
     */
    public void setOutOperator(String outOperator) {
        this.outOperator = outOperator;
    }

    /**
     * @return out_image_url
     */
    public String getOutImageUrl() {
        return outImageUrl;
    }

    /**
     * @param outImageUrl
     */
    public void setOutImageUrl(String outImageUrl) {
        this.outImageUrl = outImageUrl;
    }

    /**
     * @return out_gate
     */
    public String getOutGate() {
        return outGate;
    }

    /**
     * @param outGate
     */
    public void setOutGate(String outGate) {
        this.outGate = outGate;
    }

    /**
     * @return out_comment
     */
    public String getOutComment() {
        return outComment;
    }

    /**
     * @param outComment
     */
    public void setOutComment(String outComment) {
        this.outComment = outComment;
    }
}