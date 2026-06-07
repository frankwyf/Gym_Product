package com.gym.project.gym.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gym.project.gym.mapper.GymVipMapper;
import com.gym.project.gym.domain.GymVip;
import com.gym.project.gym.service.IGymVipService;

/**
 * 会员卡管理Service业务层处理
 * 
 * @author gym
 * @date 2022-01-20
 */
@Service
public class GymVipServiceImpl implements IGymVipService 
{
    @Autowired
    private GymVipMapper gymVipMapper;

    /**
     * 查询会员卡管理
     * 
     * @param vipId 会员卡管理主键
     * @return 会员卡管理
     */
    @Override
    public GymVip selectGymVipByVipId(Long vipId)
    {
        return gymVipMapper.selectGymVipByVipId(vipId);
    }

    /**
     * 查询会员卡管理列表
     * 
     * @param gymVip 会员卡管理
     * @return 会员卡管理
     */
    @Override
    public List<GymVip> selectGymVipList(GymVip gymVip)
    {
        return gymVipMapper.selectGymVipList(gymVip);
    }

    /**
     * 新增会员卡管理
     * 
     * @param gymVip 会员卡管理
     * @return 结果
     */
    @Override
    public int insertGymVip(GymVip gymVip)
    {
        return gymVipMapper.insertGymVip(gymVip);
    }

    /**
     * 修改会员卡管理
     * 
     * @param gymVip 会员卡管理
     * @return 结果
     */
    @Override
    public int updateGymVip(GymVip gymVip)
    {
        return gymVipMapper.updateGymVip(gymVip);
    }

    /**
     * 批量删除会员卡管理
     * 
     * @param vipIds 需要删除的会员卡管理主键
     * @return 结果
     */
    @Override
    public int deleteGymVipByVipIds(Long[] vipIds)
    {
        return gymVipMapper.deleteGymVipByVipIds(vipIds);
    }

    /**
     * 删除会员卡管理信息
     * 
     * @param vipId 会员卡管理主键
     * @return 结果
     */
    @Override
    public int deleteGymVipByVipId(Long vipId)
    {
        return gymVipMapper.deleteGymVipByVipId(vipId);
    }
}
