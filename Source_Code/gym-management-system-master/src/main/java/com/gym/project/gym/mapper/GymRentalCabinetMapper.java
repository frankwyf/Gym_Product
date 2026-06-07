package com.gym.project.gym.mapper;

import java.util.List;
import com.gym.project.gym.domain.GymRentalCabinet;
import org.apache.ibatis.annotations.Mapper;
/**
 * 租柜Mapper接口
 * 
 * @author gym
 * @date 2022-02-07
 */
@Mapper
public interface GymRentalCabinetMapper 
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
     * 删除租柜
     * 
     * @param cabinetId 租柜主键
     * @return 结果
     */
    public int deleteGymRentalCabinetByCabinetId(Long cabinetId);

    /**
     * 批量删除租柜
     * 
     * @param cabinetIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGymRentalCabinetByCabinetIds(Long[] cabinetIds);

}
