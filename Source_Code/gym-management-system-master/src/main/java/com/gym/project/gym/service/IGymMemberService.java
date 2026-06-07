package com.gym.project.gym.service;

import java.util.List;
import com.gym.project.gym.domain.GymMember;

/**
 * 会员管理Service接口
 * 
 * @author gym
 * @date 2022-01-19
 */
public interface IGymMemberService 
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
     * 批量删除会员管理
     * 
     * @param memberIds 需要删除的会员管理主键集合
     * @return 结果
     */
    public int deleteGymMemberByMemberIds(Long[] memberIds);

    /**
     * 删除会员管理信息
     * 
     * @param memberId 会员管理主键
     * @return 结果
     */
    public int deleteGymMemberByMemberId(Long memberId);

    /**
     * 获取没有老师的会员
     * @return
     */
    public List<GymMember> selectNoTeacherGymMemberList();

    public GymMember selectByUserId(Long memberUserId);

    public Long getStudentNumber(Long teacherId);
}
