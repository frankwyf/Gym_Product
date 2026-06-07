package com.gym.project.gym.domain;

import java.math.BigDecimal;
import com.gym.framework.aspectj.lang.annotation.Excel;
import com.gym.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 商品对象 gym_commodity
 * 
 * @author gym
 * @date 2022-01-27
 */
public class GymCommodity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 商品id */
    private Long commodityId;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String commodityName;

    /** 商品价格 */
    @Excel(name = "商品价格")
    private BigDecimal commodityPrice;

    /** 商品数量 */
    @Excel(name = "商品数量")
    private Long commodityNumber;
    /** 出库入库数据 **/
    private Long inputOrOutput;

    public Long getInputOrOutput() {
        return inputOrOutput;
    }

    public void setInputOrOutput(Long inputOrOutput) {
        this.inputOrOutput = inputOrOutput;
    }
    public void setCommodityId(Long commodityId) 
    {
        this.commodityId = commodityId;
    }

    public Long getCommodityId() 
    {
        return commodityId;
    }
    public void setCommodityName(String commodityName) 
    {
        this.commodityName = commodityName;
    }

    public String getCommodityName() 
    {
        return commodityName;
    }
    public void setCommodityPrice(BigDecimal commodityPrice) 
    {
        this.commodityPrice = commodityPrice;
    }

    public BigDecimal getCommodityPrice() 
    {
        return commodityPrice;
    }
    public void setCommodityNumber(Long commodityNumber) 
    {
        this.commodityNumber = commodityNumber;
    }

    public Long getCommodityNumber() 
    {
        return commodityNumber;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("commodityId", getCommodityId())
            .append("commodityName", getCommodityName())
            .append("commodityPrice", getCommodityPrice())
            .append("commodityNumber", getCommodityNumber())
            .append("remark", getRemark())
            .toString();
    }
}
