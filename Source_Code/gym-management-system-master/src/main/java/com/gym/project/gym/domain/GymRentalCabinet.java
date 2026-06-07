package com.gym.project.gym.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gym.framework.aspectj.lang.annotation.Excel;
import com.gym.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 租柜对象 gym_rental_cabinet
 * 
 * @author gym
 * @date 2022-02-07
 */
public class GymRentalCabinet extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 租柜id */
    private Long cabinetId;

    /** 租柜编号 */
    @Excel(name = "租柜编号")
    private String cabinetNo;

    /** 租用会员id */
    private Long memberId;

    /** 到期日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "到期日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date cabinetDate;

    @Excel(name = "使用者", width = 30)
    private String memberName;

    private Integer renewal;

    public Integer getRenewal() {
        return renewal;
    }

    public void setRenewal(Integer renewal) {
        this.renewal = renewal;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setCabinetId(Long cabinetId)
    {
        this.cabinetId = cabinetId;
    }

    public Long getCabinetId() 
    {
        return cabinetId;
    }
    public void setCabinetNo(String cabinetNo) 
    {
        this.cabinetNo = cabinetNo;
    }

    public String getCabinetNo() 
    {
        return cabinetNo;
    }
    public void setMemberId(Long memberId) 
    {
        this.memberId = memberId;
    }

    public Long getMemberId() 
    {
        return memberId;
    }
    public void setCabinetDate(Date cabinetDate) 
    {
        this.cabinetDate = cabinetDate;
    }

    public Date getCabinetDate() 
    {
        return cabinetDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("cabinetId", getCabinetId())
            .append("cabinetNo", getCabinetNo())
            .append("memberId", getMemberId())
            .append("cabinetDate", getCabinetDate())
            .toString();
    }
}
