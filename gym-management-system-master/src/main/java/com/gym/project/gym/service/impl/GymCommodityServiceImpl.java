package com.gym.project.gym.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gym.project.gym.mapper.GymCommodityMapper;
import com.gym.project.gym.domain.GymCommodity;
import com.gym.project.gym.service.IGymCommodityService;

/**
 * 商品Service业务层处理
 * 
 * @author gym
 * @date 2022-01-27
 */
@Service
public class GymCommodityServiceImpl implements IGymCommodityService 
{
    @Autowired
    private GymCommodityMapper gymCommodityMapper;

    /**
     * 查询商品
     * 
     * @param commodityId 商品主键
     * @return 商品
     */
    @Override
    public GymCommodity selectGymCommodityByCommodityId(Long commodityId)
    {
        return gymCommodityMapper.selectGymCommodityByCommodityId(commodityId);
    }

    /**
     * 查询商品列表
     * 
     * @param gymCommodity 商品
     * @return 商品
     */
    @Override
    public List<GymCommodity> selectGymCommodityList(GymCommodity gymCommodity)
    {
        return gymCommodityMapper.selectGymCommodityList(gymCommodity);
    }

    /**
     * 新增商品
     * 
     * @param gymCommodity 商品
     * @return 结果
     */
    @Override
    public int insertGymCommodity(GymCommodity gymCommodity)
    {
        return gymCommodityMapper.insertGymCommodity(gymCommodity);
    }

    /**
     * 修改商品
     * 
     * @param gymCommodity 商品
     * @return 结果
     */
    @Override
    public int updateGymCommodity(GymCommodity gymCommodity)
    {
        return gymCommodityMapper.updateGymCommodity(gymCommodity);
    }

    /**
     * 批量删除商品
     * 
     * @param commodityIds 需要删除的商品主键
     * @return 结果
     */
    @Override
    public int deleteGymCommodityByCommodityIds(Long[] commodityIds)
    {
        return gymCommodityMapper.deleteGymCommodityByCommodityIds(commodityIds);
    }

    /**
     * 删除商品信息
     * 
     * @param commodityId 商品主键
     * @return 结果
     */
    @Override
    public int deleteGymCommodityByCommodityId(Long commodityId)
    {
        return gymCommodityMapper.deleteGymCommodityByCommodityId(commodityId);
    }
}
