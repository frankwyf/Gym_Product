package com.gym.project.gym.service;

import java.util.List;
import com.gym.project.gym.domain.GymRentalCabinet;

/**
 * 租柜Service接口
 * 
 * @author gym
 * @date 2022-02-07
 */
public interface IGymRentalCabinetService 
{
    /**
     * 查询租柜
     * 
     * @param cabinetId 租柜主键
     * @return 租柜
     */
    public GymRentalCabinet selectGymRentalCabinetByCabinetId(Long cabinetId);

    /**
     * 查询租柜列表
     * 
     * @param gymRentalCabinet 租柜
     * @return 租柜集合
     */
    public List<GymRentalCabinet> selectGymRentalCabinetList(GymRentalCabinet gymRentalCabinet);

    /**
     * 新增租柜
     * 
     * @param gymRentalCabinet 租柜
     * @return 结果
     */
    public int insertGymRentalCabinet(GymRentalCabinet gymRentalCabinet);


    /**
     * 修改租柜
     * 
     * @param gymRentalCabinet 租柜
     * @return 结果
     */
    public int updateGymRentalCabinet(GymRentalCabinet gymRentalCabinet);

    public int updateGymRentalCabinetMember(GymRentalCabinet gymRentalCabinet);

    /**
     * 批量删除租柜
     * 
     * @param cabinetIds 需要删除的租柜主键集合
     * @return 结果
     */
    public int deleteGymRentalCabinetByCabinetIds(Long[] cabinetIds);

    /**
     * 删除租柜信息
     * 
     * @param cabinetId 租柜主键
     * @return 结果
     */
    public int deleteGymRentalCabinetByCabinetId(Long cabinetId);
}
