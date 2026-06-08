package com.gym.project.gym.mapper;

import java.util.List;
import com.gym.project.gym.domain.GymMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 会员管理Mapper接口
 * 
 * @author gym
 * @date 2022-01-19
 */
@Mapper
public interface GymMemberMapper 
{
    /**
     * 查询会员管理
     * 
     * @param memberId 会员管理主键
     * @return 会员管理
     */
    public GymMember selectGymMemberByMemberId(Long memberId);

    /**
     * 查询会员管理列表
     * 
     * @param gymMember 会员管理
     * @return 会员管理集合
     */
    public List<GymMember> selectGymMemberList(GymMember gymMember);

    /**
     * 新增会员管理
     * 
     * @param gymMember 会员管理
     * @return 结果
     */
    public int insertGymMember(GymMember gymMember);

    /**
     * 修改会员管理
     * 
     * @param gymMember 会员管理
     * @return 结果
     */
    public int updateGymMember(GymMember gymMember);

    /**
     * 删除会员管理
     * 
     * @param memberId 会员管理主键
     * @return 结果
     */
    public int deleteGymMemberByMemberId(Long memberId);

    /**
     * 批量删除会员管理
     * 
     * @param memberIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGymMemberByMemberIds(Long[] memberIds);

    /**
     * 获取没有老师的会员
     * @return
     */
    public List<GymMember> selectNoTeacherGymMemberList();


    public GymMember selectGymMemberByUserId(Long userId);

    public Long getStudentNumber(Long teacherId);
}
