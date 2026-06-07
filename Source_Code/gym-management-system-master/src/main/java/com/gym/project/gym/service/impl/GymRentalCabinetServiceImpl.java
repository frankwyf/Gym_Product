package com.gym.project.gym.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gym.project.gym.mapper.GymRentalCabinetMapper;
import com.gym.project.gym.domain.GymRentalCabinet;
import com.gym.project.gym.service.IGymRentalCabinetService;

/**
 * 租柜Service业务层处理
 * 
 * @author gym
 * @date 2022-02-07
 */
@Service
public class GymRentalCabinetServiceImpl implements IGymRentalCabinetService
{
    @Autowired
    private GymRentalCabinetMapper gymRentalCabinetMapper;

    /**
     * 查询租柜
     * 
     * @param cabinetId 租柜主键
     * @return 租柜
     */
    @Override
    public GymRentalCabinet selectGymRentalCabinetByCabinetId(Long cabinetId)
    {
        return gymRentalCabinetMapper.selectGymRentalCabinetByCabinetId(cabinetId);
    }

    /**
     * 查询租柜列表
     * 
     * @param gymRentalCabinet 租柜
     * @return 租柜
     */
    @Override
    public List<GymRentalCabinet> selectGymRentalCabinetList(GymRentalCabinet gymRentalCabinet)
    {
        return gymRentalCabinetMapper.selectGymRentalCabinetList(gymRentalCabinet);
    }

    /**
     * 新增租柜
     * 
     * @param gymRentalCabinet 租柜
     * @return 结果
     */
    @Override
    public int insertGymRentalCabinet(GymRentalCabinet gymRentalCabinet)
    {
        return gymRentalCabinetMapper.insertGymRentalCabinet(gymRentalCabinet);
    }

    /**
     * 修改租柜
     * 
     * @param gymRentalCabinet 租柜
     * @return 结果
     */
    @Override
    public int updateGymRentalCabinet(GymRentalCabinet gymRentalCabinet)
    {
        return gymRentalCabinetMapper.updateGymRentalCabinet(gymRentalCabinet);
    }

    @Override
    public int updateGymRentalCabinetMember(GymRentalCabinet gymRentalCabinet) {
        return gymRentalCabinetMapper.updateGymRentalCabinetMember(gymRentalCabinet);
    }

    /**
     * 批量删除租柜
     * 
     * @param cabinetIds 需要删除的租柜主键
     * @return 结果
     */
    @Override
    public int deleteGymRentalCabinetByCabinetIds(Long[] cabinetIds)
    {
        return gymRentalCabinetMapper.deleteGymRentalCabinetByCabinetIds(cabinetIds);
    }

    /**
     * 删除租柜信息
     * 
     * @param cabinetId 租柜主键
     * @return 结果
     */
    @Override
    public int deleteGymRentalCabinetByCabinetId(Long cabinetId)
    {
        return gymRentalCabinetMapper.deleteGymRentalCabinetByCabinetId(cabinetId);
    }
}
