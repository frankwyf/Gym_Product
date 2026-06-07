package com.gym.project.gym.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gym.framework.aspectj.lang.annotation.Excel;
import com.gym.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 会员卡管理对象 gym_vip
 * 
 * @author gym
 * @date 2022-01-20
 */
public class GymVip extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 会员卡id */
    private Long vipId;

    /** 会员卡号 */
    @Excel(name = "会员卡号")
    private String vipNo;

    /** 会员卡有效日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "会员卡有效日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date effective;

    /** 会员手机号 */
    @Excel(name = "会员手机号")
    private String memberPhone;
    /** 会员姓名 */
    @Excel(name = "会员姓名")
    private String memberName;

    private Integer renewal;

    public Integer getRenewal() {
        return renewal;
    }

    public void setRenewal(Integer renewal) {
        this.renewal = renewal;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setVipId(Long vipId) 
    {
        this.vipId = vipId;
    }

    public Long getVipId() 
    {
        return vipId;
    }
    public void setVipNo(String vipNo) 
    {
        this.vipNo = vipNo;
    }

    public String getVipNo() 
    {
        return vipNo;
    }
    public void setEffective(Date effective) 
    {
        this.effective = effective;
    }

    public Date getEffective() 
    {
        return effective;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("vipId", getVipId())
            .append("vipNo", getVipNo())
            .append("effective", getEffective())
            .toString();
    }
}
