package com.wind.mybatis.pojo;

import java.util.Date;
import javax.persistence.*;

public class Payment {
    @Id
    private Long id;

    private String body;

    private String detail;

    private String ip;

    @Column(name = "bank_type")
    private String bankType;

    @Column(name = "trade_type")
    private String tradeType;

    @Column(name = "fee_type")
    private String feeType;

    @Column(name = "out_trade_no")
    private String outTradeNo;

    @Column(name = "transaction_no")
    private String transactionNo;

    @Column(name = "total_fee")
    private Integer totalFee;

    @Column(name = "create_time")
    private Date createTime;

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
     * @return body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @param detail
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * @return ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return bank_type
     */
    public String getBankType() {
        return bankType;
    }

    /**
     * @param bankType
     */
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    /**
     * @return trade_type
     */
    public String getTradeType() {
        return tradeType;
    }

    /**
     * @param tradeType
     */
    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    /**
     * @return fee_type
     */
    public String getFeeType() {
        return feeType;
    }

    /**
     * @param feeType
     */
    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    /**
     * @return out_trade_no
     */
    public String getOutTradeNo() {
        return outTradeNo;
    }

    /**
     * @param outTradeNo
     */
    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    /**
     * @return transaction_no
     */
    public String getTransactionNo() {
        return transactionNo;
    }

    /**
     * @param transactionNo
     */
    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    /**
     * @return total_fee
     */
    public Integer getTotalFee() {
        return totalFee;
    }

    /**
     * @param totalFee
     */
    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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