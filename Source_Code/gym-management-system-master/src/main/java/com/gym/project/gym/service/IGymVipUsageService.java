package com.gym.project.gym.service;

import java.util.List;
import com.gym.project.gym.domain.GymVipUsage;

/**
 * 会员卡使用记录Service接口
 * 
 * @author gym
 * @date 2022-01-23
 */
public interface IGymVipUsageService 
{
    /**
     * 查询会员卡使用记录
     * 
     * @param usageId 会员卡使用记录主键
     * @return 会员卡使用记录
     */
    public GymVipUsage selectGymVipUsageByUsageId(Long usageId);
    /**
     * 查询会员卡使用记录
     *
     * @param vipId 会员卡id
     * @return 会员卡使用记录
     */
    public List<GymVipUsage>  selectGymVipUsageByVipId(Long vipId);
    /**
     * 查询会员卡使用记录列表
     * 
     * @param gymVipUsage 会员卡使用记录
     * @return 会员卡使用记录集合
     */
    public List<GymVipUsage> selectGymVipUsageList(GymVipUsage gymVipUsage);

    /**
     * 新增会员卡使用记录
     * 
     * @param gymVipUsage 会员卡使用记录
     * @return 结果
     */
    public int insertGymVipUsage(GymVipUsage gymVipUsage);

    /**
     * 修改会员卡使用记录
     * 
     * @param gymVipUsage 会员卡使用记录
     * @return 结果
     */
    public int updateGymVipUsage(GymVipUsage gymVipUsage);

    /**
     * 批量删除会员卡使用记录
     * 
     * @param usageIds 需要删除的会员卡使用记录主键集合
     * @return 结果
     */
    public int deleteGymVipUsageByUsageIds(Long[] usageIds);

    /**
     * 删除会员卡使用记录信息
     * 
     * @param usageId 会员卡使用记录主键
     * @return 结果
     */
    public int deleteGymVipUsageByUsageId(Long usageId);
}
