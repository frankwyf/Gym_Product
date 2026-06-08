package com.gym.project.gym.service;

import java.util.List;
import com.gym.project.gym.domain.GymVip;

/**
 * 会员卡管理Service接口
 * 
 * @author gym
 * @date 2022-01-20
 */
public interface IGymVipService 
{
    /**
     * 查询会员卡管理
     * 
     * @param vipId 会员卡管理主键
     * @return 会员卡管理
     */
    public GymVip selectGymVipByVipId(Long vipId);

    /**
     * 查询会员卡管理列表
     * 
     * @param gymVip 会员卡管理
     * @return 会员卡管理集合
     */
    public List<GymVip> selectGymVipList(GymVip gymVip);

    /**
     * 新增会员卡管理
     * 
     * @param gymVip 会员卡管理
     * @return 结果
     */
    public int insertGymVip(GymVip gymVip);

    /**
     * 修改会员卡管理
     * 
     * @param gymVip 会员卡管理
     * @return 结果
     */
    public int updateGymVip(GymVip gymVip);

    /**
     * 批量删除会员卡管理
     * 
     * @param vipIds 需要删除的会员卡管理主键集合
     * @return 结果
     */
    public int deleteGymVipByVipIds(Long[] vipIds);

    /**
     * 删除会员卡管理信息
     * 
     * @param vipId 会员卡管理主键
     * @return 结果
     */
    public int deleteGymVipByVipId(Long vipId);
}
