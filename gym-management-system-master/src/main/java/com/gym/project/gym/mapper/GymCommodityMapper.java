package com.gym.project.gym.mapper;

import java.util.List;
import com.gym.project.gym.domain.GymCommodity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品Mapper接口
 * 
 * @author gym
 * @date 2022-01-27
 */
@Mapper
public interface GymCommodityMapper 
{
    /**
     * 查询商品
     * 
     * @param commodityId 商品主键
     * @return 商品
     */
    public GymCommodity selectGymCommodityByCommodityId(Long commodityId);

    /**
     * 查询商品列表
     * 
     * @param gymCommodity 商品
     * @return 商品集合
     */
    public List<GymCommodity> selectGymCommodityList(GymCommodity gymCommodity);

    /**
     * 新增商品
     * 
     * @param gymCommodity 商品
     * @return 结果
     */
    public int insertGymCommodity(GymCommodity gymCommodity);

    /**
     * 修改商品
     * 
     * @param gymCommodity 商品
     * @return 结果
     */
    public int updateGymCommodity(GymCommodity gymCommodity);

    /**
     * 删除商品
     * 
     * @param commodityId 商品主键
     * @return 结果
     */
    public int deleteGymCommodityByCommodityId(Long commodityId);

    /**
     * 批量删除商品
     * 
     * @param commodityIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGymCommodityByCommodityIds(Long[] commodityIds);
}
