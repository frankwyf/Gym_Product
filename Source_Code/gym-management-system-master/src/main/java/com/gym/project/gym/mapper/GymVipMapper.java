package com.gym.project.gym.mapper;

import java.util.List;
import com.gym.project.gym.domain.GymVip;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员卡管理Mapper接口
 * 
 * @author gym
 * @date 2022-01-20
 */
@Mapper
public interface GymVipMapper 
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
     * 删除会员卡管理
     * 
     * @param vipId 会员卡管理主键
     * @return 结果
     */
    public int deleteGymVipByVipId(Long vipId);

    /**
     * 批量删除会员卡管理
     * 
     * @param vipIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGymVipByVipIds(Long[] vipIds);
}
