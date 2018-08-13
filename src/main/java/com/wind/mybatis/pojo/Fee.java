package com.wind.mybatis.pojo;

import java.math.BigDecimal;
import javax.persistence.*;

public class Fee {
    @Id
    private Long id;

    @Column(name = "is_free")
    private Boolean isFree;

    private String parameters;

    @Column(name = "in_free_minutes")
    private Short inFreeMinutes;

    @Column(name = "out_free_minutes")
    private Short outFreeMinutes;

    @Column(name = "per_time")
    private BigDecimal perTime;

    @Column(name = "per_hour")
    private BigDecimal perHour;

    @Column(name = "per_month")
    private BigDecimal perMonth;

    @Column(name = "per_year")
    private BigDecimal perYear;

    @Column(name = "limit_per_time")
    private BigDecimal limitPerTime;

    @Column(name = "limit_per_day")
    private BigDecimal limitPerDay;

    @Column(name = "differential_pricing")
    private String differentialPricing;

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
     * @return is_free
     */
    public Boolean getIsFree() {
        return isFree;
    }

    /**
     * @param isFree
     */
    public void setIsFree(Boolean isFree) {
        this.isFree = isFree;
    }

    /**
     * @return parameters
     */
    public String getParameters() {
        return parameters;
    }

    /**
     * @param parameters
     */
    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    /**
     * @return in_free_minutes
     */
    public Short getInFreeMinutes() {
        return inFreeMinutes;
    }

    /**
     * @param inFreeMinutes
     */
    public void setInFreeMinutes(Short inFreeMinutes) {
        this.inFreeMinutes = inFreeMinutes;
    }

    /**
     * @return out_free_minutes
     */
    public Short getOutFreeMinutes() {
        return outFreeMinutes;
    }

    /**
     * @param outFreeMinutes
     */
    public void setOutFreeMinutes(Short outFreeMinutes) {
        this.outFreeMinutes = outFreeMinutes;
    }

    /**
     * @return per_time
     */
    public BigDecimal getPerTime() {
        return perTime;
    }

    /**
     * @param perTime
     */
    public void setPerTime(BigDecimal perTime) {
        this.perTime = perTime;
    }

    /**
     * @return per_hour
     */
    public BigDecimal getPerHour() {
        return perHour;
    }

    /**
     * @param perHour
     */
    public void setPerHour(BigDecimal perHour) {
        this.perHour = perHour;
    }

    /**
     * @return per_month
     */
    public BigDecimal getPerMonth() {
        return perMonth;
    }

    /**
     * @param perMonth
     */
    public void setPerMonth(BigDecimal perMonth) {
        this.perMonth = perMonth;
    }

    /**
     * @return per_year
     */
    public BigDecimal getPerYear() {
        return perYear;
    }

    /**
     * @param perYear
     */
    public void setPerYear(BigDecimal perYear) {
        this.perYear = perYear;
    }

    /**
     * @return limit_per_time
     */
    public BigDecimal getLimitPerTime() {
        return limitPerTime;
    }

    /**
     * @param limitPerTime
     */
    public void setLimitPerTime(BigDecimal limitPerTime) {
        this.limitPerTime = limitPerTime;
    }

    /**
     * @return limit_per_day
     */
    public BigDecimal getLimitPerDay() {
        return limitPerDay;
    }

    /**
     * @param limitPerDay
     */
    public void setLimitPerDay(BigDecimal limitPerDay) {
        this.limitPerDay = limitPerDay;
    }

    /**
     * @return differential_pricing
     */
    public String getDifferentialPricing() {
        return differentialPricing;
    }

    /**
     * @param differentialPricing
     */
    public void setDifferentialPricing(String differentialPricing) {
        this.differentialPricing = differentialPricing;
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