package com.gym.project.gym.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gym.framework.aspectj.lang.annotation.Excel;
import com.gym.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 会员卡使用记录对象 gym_vip_usage
 * 
 * @author gym
 * @date 2022-01-23
 */
public class GymVipUsage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 使用记录id */
    private Long usageId;

    /** 会员卡id */
    @Excel(name = "会员卡id", readConverterExp = "$column.readConverterExp()")
    private Long vipId;

    /** 使用日期 */
    @Excel(name = "使用日期", readConverterExp = "$column.readConverterExp()")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date date;

    private String vipNo;

    public void setUsageId(Long usageId) 
    {
        this.usageId = usageId;
    }

    public Long getUsageId() 
    {
        return usageId;
    }
    public void setVipId(Long vipId) 
    {
        this.vipId = vipId;
    }

    public Long getVipId() 
    {
        return vipId;
    }
    public void setDate(Date date) 
    {
        this.date = date;
    }

    public Date getDate() 
    {
        return date;
    }

    public String getVipNo() {
        return vipNo;
    }

    public void setVipNo(String vipNo) {
        this.vipNo = vipNo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("usageId", getUsageId())
            .append("vipId", getVipId())
            .append("date", getDate())
            .toString();
    }
}
