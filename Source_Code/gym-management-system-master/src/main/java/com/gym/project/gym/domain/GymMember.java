package com.gym.project.gym.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gym.framework.aspectj.lang.annotation.Excel;
import com.gym.framework.web.domain.BaseEntity;
import com.gym.project.system.domain.SysUser;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 会员管理对象 gym_member
 * 
 * @author gym
 * @date 2022-01-19
 */
public class GymMember extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 会员id */
    private Long memberId;

    /** 会员姓名 */
    @Excel(name = "会员姓名")
    private String memberName;

    /** 会员年龄 */
    @Excel(name = "会员年龄")
    private Integer memberAge;

    /** 会员性别 */
    @Excel(name = "会员性别")
    private String memberSex;

    /** 会员手机号 */
    @Excel(name = "会员手机号")
    private String memberPhone;

    /** 会员邮箱 */
    @Excel(name = "会员邮箱")
    private String memberEmail;

    /** 会员生日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "会员生日", width = 30, dateFormat = "yyyy-MM-dd")
    private Date memberBirthday;

    /** 会员卡id */
    @Excel(name = "会员卡id")
    private Long vipId;

    /** 私教id */
    @Excel(name = "私教id")
    private Long teacherId;

    /** 对应用户id */
    @Excel(name = "对应用户id")
    private Long userId;

    /** 对应私教实体 **/
    private SysUser teacher;
    
    /** 用户实体 **/
    private SysUser user;

    public SysUser getTeacher() {
        return teacher;
    }

    public void setTeacher(SysUser teacher) {
        this.teacher = teacher;
    }

    public SysUser getUser() {
        return user;
    }

    public void setUser(SysUser user) {
        this.user = user;
    }

    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
    }

    public Long getMemberId() 
    {
        return memberId;
    }
    public void setMemberName(String memberName) 
    {
        this.memberName = memberName;
    }

    public String getMemberName() 
    {
        return memberName;
    }
    public void setMemberAge(Integer memberAge) 
    {
        this.memberAge = memberAge;
    }

    public Integer getMemberAge() 
    {
        return memberAge;
    }
    public void setMemberSex(String memberSex) 
    {
        this.memberSex = memberSex;
    }

    public String getMemberSex() 
    {
        return memberSex;
    }
    public void setMemberPhone(String memberPhone) 
    {
        this.memberPhone = memberPhone;
    }

    public String getMemberPhone() 
    {
        return memberPhone;
    }
    public void setMemberEmail(String memberEmail) 
    {
        this.memberEmail = memberEmail;
    }

    public String getMemberEmail() 
    {
        return memberEmail;
    }
    public void setMemberBirthday(Date memberBirthday) 
    {
        this.memberBirthday = memberBirthday;
    }

    public Date getMemberBirthday() 
    {
        return memberBirthday;
    }
    public void setVipId(Long vipId) 
    {
        this.vipId = vipId;
    }

    public Long getVipId() 
    {
        return vipId;
    }
    public void setTeacherId(Long teacherId) 
    {
        this.teacherId = teacherId;
    }

    public Long getTeacherId() 
    {
        return teacherId;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("memberId", getMemberId())
            .append("memberName", getMemberName())
            .append("memberAge", getMemberAge())
            .append("memberSex", getMemberSex())
            .append("memberPhone", getMemberPhone())
            .append("memberEmail", getMemberEmail())
            .append("memberBirthday", getMemberBirthday())
            .append("vipId", getVipId())
            .append("teacherId", getTeacherId())
            .append("userId", getUserId())
            .toString();
    }
}
