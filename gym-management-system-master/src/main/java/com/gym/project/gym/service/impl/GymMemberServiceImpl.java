package com.gym.project.gym.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gym.common.utils.SecurityUtils;
import com.gym.project.system.domain.SysUser;
import com.gym.project.system.domain.SysUserRole;
import com.gym.project.system.mapper.SysUserMapper;
import com.gym.project.system.mapper.SysUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gym.project.gym.mapper.GymMemberMapper;
import com.gym.project.gym.domain.GymMember;
import com.gym.project.gym.service.IGymMemberService;

/**
 * 会员管理Service业务层处理
 * 
 * @author gym
 * @date 2022-01-19
 */
@Service
public class GymMemberServiceImpl implements IGymMemberService 
{
    @Autowired
    private GymMemberMapper gymMemberMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    /**
     * 查询会员管理
     * 
     * @param memberId 会员管理主键
     * @return 会员管理
     */
    @Override
    public GymMember selectGymMemberByMemberId(Long memberId)
    {
        return gymMemberMapper.selectGymMemberByMemberId(memberId);
    }

    /**
     * 查询会员管理列表
     * 
     * @param gymMember 会员管理
     * @return 会员管理
     */
    @Override
    public List<GymMember> selectGymMemberList(GymMember gymMember)
    {
        return gymMemberMapper.selectGymMemberList(gymMember);
    }

    /**
     * 新增会员管理
     * 
     * @param gymMember 会员管理
     * @return 结果
     */
    @Override
    public int insertGymMember(GymMember gymMember)
    {
        //增加会员信息的同时创建一个用户
        SysUser sysUser = new SysUser();
        sysUser.setUserName(gymMember.getMemberPhone());
        sysUser.setNickName(gymMember.getMemberName());
        sysUser.setEmail(gymMember.getMemberEmail());
        sysUser.setSex(gymMember.getMemberSex());
        sysUser.setPhonenumber(gymMember.getMemberPhone());
        sysUser.setPassword(SecurityUtils.encryptPassword("123456"));
        sysUser.setCreateBy(SecurityUtils.getUsername());
        sysUser.setCreateTime(new Date());
        sysUserMapper.insertUser(sysUser);
        //回写userid
        sysUser = sysUserMapper.selectUserByUserName(gymMember.getMemberPhone());
        //创建角色
        SysUserRole userRole =  new SysUserRole();
        userRole.setUserId(sysUser.getUserId());
        userRole.setRoleId(103L);
        List<SysUserRole> userRoleList =  new ArrayList<SysUserRole>();
        userRoleList.add(userRole);
        sysUserRoleMapper.batchUserRole(userRoleList);
        //报错会员信息
        gymMember.setUserId(sysUser.getUserId());
        return gymMemberMapper.insertGymMember(gymMember);
    }

    /**
     * 修改会员管理
     * 
     * @param gymMember 会员管理
     * @return 结果
     */
    @Override
    public int updateGymMember(GymMember gymMember)
    {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(gymMember.getUserId());
        sysUser.setUserName(gymMember.getMemberPhone());
        sysUser.setNickName(gymMember.getMemberName());
        sysUser.setEmail(gymMember.getMemberEmail());
        sysUser.setSex(gymMember.getMemberSex());
        sysUser.setPhonenumber(gymMember.getMemberPhone());
        sysUserMapper.updateUser(sysUser);
        return gymMemberMapper.updateGymMember(gymMember);
    }

    /**
     * 批量删除会员管理
     * 
     * @param memberIds 需要删除的会员管理主键
     * @return 结果
     */
    @Override
    public int deleteGymMemberByMemberIds(Long[] memberIds)
    {
        Long deletUSerIds[] = new Long[memberIds.length];
        for(int i = 0;i<deletUSerIds.length;i++){
            deletUSerIds[i] = gymMemberMapper.selectGymMemberByMemberId(memberIds[i]).getUserId();
        }
        sysUserMapper.deleteUserByIds(deletUSerIds);
        return gymMemberMapper.deleteGymMemberByMemberIds(memberIds);
    }

    /**
     * 删除会员管理信息
     * 
     * @param memberId 会员管理主键
     * @return 结果
     */
    @Override
    public int deleteGymMemberByMemberId(Long memberId)
    {
        GymMember gymMember = gymMemberMapper.selectGymMemberByMemberId(memberId);
        sysUserMapper.deleteUserById(gymMember.getUserId());
        return gymMemberMapper.deleteGymMemberByMemberId(memberId);
    }

    @Override
    public List<GymMember> selectNoTeacherGymMemberList() {
        return gymMemberMapper.selectNoTeacherGymMemberList();
    }

    @Override
    public GymMember selectByUserId(Long userId) {
        return gymMemberMapper.selectGymMemberByUserId(userId);
    }

    public Long getStudentNumber(Long teacherId){
        return gymMemberMapper.getStudentNumber(teacherId);
    }
}
