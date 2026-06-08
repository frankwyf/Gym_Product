package com.gym.project.gym.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gym.project.gym.mapper.GymVipUsageMapper;
import com.gym.project.gym.domain.GymVipUsage;
import com.gym.project.gym.service.IGymVipUsageService;

/**
 * 会员卡使用记录Service业务层处理
 * 
 * @author gym
 * @date 2022-01-23
 */
@Service
public class GymVipUsageServiceImpl implements IGymVipUsageService 
{
    @Autowired
    private GymVipUsageMapper gymVipUsageMapper;

    /**
     * 查询会员卡使用记录
     * 
     * @param usageId 会员卡使用记录主键
     * @return 会员卡使用记录
     */
    @Override
    public GymVipUsage selectGymVipUsageByUsageId(Long usageId)
    {
        return gymVipUsageMapper.selectGymVipUsageByUsageId(usageId);
    }
    /**
     * 查询会员卡使用记录
     *
     * @param vipId 会员卡id
     * @return 会员卡使用记录
     */
    @Override
    public List<GymVipUsage> selectGymVipUsageByVipId(Long vipId) {
        return gymVipUsageMapper.selectGymVipUsageByVipId(vipId);
    }

    /**
     * 查询会员卡使用记录列表
     * 
     * @param gymVipUsage 会员卡使用记录
     * @return 会员卡使用记录
     */
    @Override
    public List<GymVipUsage> selectGymVipUsageList(GymVipUsage gymVipUsage)
    {
        return gymVipUsageMapper.selectGymVipUsageList(gymVipUsage);
    }

    /**
     * 新增会员卡使用记录
     * 
     * @param gymVipUsage 会员卡使用记录
     * @return 结果
     */
    @Override
    public int insertGymVipUsage(GymVipUsage gymVipUsage)
    {
        return gymVipUsageMapper.insertGymVipUsage(gymVipUsage);
    }

    /**
     * 修改会员卡使用记录
     * 
     * @param gymVipUsage 会员卡使用记录
     * @return 结果
     */
    @Override
    public int updateGymVipUsage(GymVipUsage gymVipUsage)
    {
        return gymVipUsageMapper.updateGymVipUsage(gymVipUsage);
    }

    /**
     * 批量删除会员卡使用记录
     * 
     * @param usageIds 需要删除的会员卡使用记录主键
     * @return 结果
     */
    @Override
    public int deleteGymVipUsageByUsageIds(Long[] usageIds)
    {
        return gymVipUsageMapper.deleteGymVipUsageByUsageIds(usageIds);
    }

    /**
     * 删除会员卡使用记录信息
     * 
     * @param usageId 会员卡使用记录主键
     * @return 结果
     */
    @Override
    public int deleteGymVipUsageByUsageId(Long usageId)
    {
        return gymVipUsageMapper.deleteGymVipUsageByUsageId(usageId);
    }
}
