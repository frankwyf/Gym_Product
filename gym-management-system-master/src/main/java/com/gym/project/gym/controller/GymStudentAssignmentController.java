package com.gym.project.gym.controller;

import com.github.pagehelper.PageInfo;
import com.gym.common.constant.HttpStatus;
import com.gym.framework.aspectj.lang.annotation.Log;
import com.gym.framework.aspectj.lang.enums.BusinessType;
import com.gym.framework.web.domain.AjaxResult;
import com.gym.framework.web.page.TableDataInfo;
import com.gym.project.gym.domain.GymMember;
import com.gym.project.gym.service.IGymMemberService;
import com.gym.project.gym.service.IGymVipService;
import com.gym.project.system.domain.SysRole;
import com.gym.project.system.domain.SysUser;
import com.gym.project.system.service.ISysRoleService;
import com.gym.project.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.gym.framework.web.domain.AjaxResult.success;

@RestController
@RequestMapping("/gym/studentAssignment")
public class GymStudentAssignmentController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private IGymMemberService gymMemberService;
    @Autowired
    private ISysRoleService roleService;


    /**
     * 获取角色为私教的用户
     * @param sysUser
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo list(SysUser sysUser)
    {
        sysUser.setRoleId(102L);
        return getDataTable(userService.selectAllocatedList(sysUser));
    }

    /**
     * 获取私教用户的所有学生
     * @param teacherId
     * @return
     */
    @GetMapping("/student/list/{teacherId}")
    public AjaxResult studentList(@PathVariable("teacherId") Long teacherId){
        GymMember gymMember = new GymMember();
        gymMember.setTeacherId(teacherId);
        AjaxResult ajax = success();
        SysUser user = userService.selectUserById(teacherId);
        ajax.put("user", user);
        ajax.put("students", gymMemberService.selectGymMemberList(gymMember));
        return ajax;
    }

    /**
     * 获取私教用户的所有学生
     * @param memberUserId
     * @return
     */
    @GetMapping("/teacher/list/{memberUserId}")
    public AjaxResult teacherList(@PathVariable("memberUserId") Long memberUserId){
        GymMember gymMember = gymMemberService.selectByUserId(memberUserId);
        AjaxResult ajax = success();
        if(gymMember!=null){
            SysUser user = userService.selectUserById(gymMember.getTeacherId());
            List<SysUser> users = new ArrayList<SysUser>();
            users.add(user);
            ajax.put("teachers", users);
            return ajax;
        }
        ajax.put("teachers", "");
        return ajax;
    }

    /**
     * 获取没有老师的学员
     * @return
     */
    @GetMapping("/student/list/")
    public AjaxResult studentListNoTeacher(){
        AjaxResult ajax = success();
        ajax.put("students", gymMemberService.selectNoTeacherGymMemberList());
        return ajax;
    }

    /**
     * 学员分配
     */
    @Log(title = "学员分配", businessType = BusinessType.GRANT)
    @PutMapping("/update")
    public AjaxResult insertTeacher(Long teacherId, Long[] students)
    {
        for(Long id : students){
           GymMember gymMember = gymMemberService.selectGymMemberByMemberId(id);
           gymMember.setTeacherId(teacherId);
           gymMemberService.updateGymMember(gymMember);
        }
        return success();
    }

    /**
     * 学员分配
     */
    @Log(title = "学员分配", businessType = BusinessType.GRANT)
    @GetMapping("/delete/{studentId}")
    public AjaxResult deleteTeacher(@PathVariable("studentId") Long studentId)
    {
        GymMember gymMember = gymMemberService.selectGymMemberByMemberId(studentId);
        gymMember.setTeacherId(-1L);
        gymMemberService.updateGymMember(gymMember);
        return success();
    }
    @GetMapping("/charts")
    public AjaxResult getChartsData(){
        List<SysUser> users = userService.selectUserList(new SysUser());
        List<SysUser> teachers = new ArrayList<>();
        List<String> teacherName = new ArrayList<>();
        List<Long> studentNumber = new ArrayList<>();

        for (SysUser user:users){
            List<Long> sysRoles = roleService.selectRoleListByUserId(user.getUserId());
            for(Long i :sysRoles){
                if(i==102L){
                    teachers.add(user);
                    break;
                }
            }
        }
        for (SysUser user:teachers){
            Long n = gymMemberService.getStudentNumber(user.getUserId());
            teacherName.add(user.getNickName());
            studentNumber.add(n);
        }
        AjaxResult ajax = success();
        ajax.put("a",teacherName);
        ajax.put("b",studentNumber);
        return ajax;
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }


}
