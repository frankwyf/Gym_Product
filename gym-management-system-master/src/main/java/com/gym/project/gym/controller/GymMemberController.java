package com.gym.project.gym.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.gym.project.gym.domain.GymRentalCabinet;
import com.gym.project.gym.domain.GymVip;
import com.gym.project.gym.service.IGymRentalCabinetService;
import com.gym.project.gym.service.IGymVipService;
import com.gym.project.system.domain.SysConfig;
import com.gym.project.system.service.ISysConfigService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gym.framework.aspectj.lang.annotation.Log;
import com.gym.framework.aspectj.lang.enums.BusinessType;
import com.gym.project.gym.domain.GymMember;
import com.gym.project.gym.service.IGymMemberService;
import com.gym.framework.web.controller.BaseController;
import com.gym.framework.web.domain.AjaxResult;
import com.gym.common.utils.poi.ExcelUtil;
import com.gym.framework.web.page.TableDataInfo;

/**
 * 会员管理Controller
 * 
 * @author gym
 * @date 2022-01-19
 */
@RestController
@RequestMapping("/gym/member")
public class GymMemberController extends BaseController
{
    @Autowired
    private IGymMemberService gymMemberService;
    @Autowired
    private IGymVipService gymVipService;
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IGymRentalCabinetService gymRentalCabinetService;

    /**
     * 查询会员管理列表
     */
    @PreAuthorize("@ss.hasPermi('gym:member:list')")
    @GetMapping("/list")
    public TableDataInfo list(GymMember gymMember)
    {
        startPage();
        List<GymMember> list = gymMemberService.selectGymMemberList(gymMember);
        return getDataTable(list);
    }

    /**
     * 导出会员管理列表
     */
    @PreAuthorize("@ss.hasPermi('gym:member:export')")
    @Log(title = "会员管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GymMember gymMember)
    {
        List<GymMember> list = gymMemberService.selectGymMemberList(gymMember);
        ExcelUtil<GymMember> util = new ExcelUtil<GymMember>(GymMember.class);
        util.exportExcel(response, list, "会员管理数据");
    }

    /**
     * 获取会员管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('gym:member:query')")
    @GetMapping(value = "/{memberId}")
    public AjaxResult getInfo(@PathVariable("memberId") Long memberId)
    {
        return AjaxResult.success(gymMemberService.selectGymMemberByMemberId(memberId));
    }

    /**
     * 新增会员管理
     */
    @PreAuthorize("@ss.hasPermi('gym:member:add')")
    @Log(title = "会员管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GymMember gymMember)
    {
        if(gymMember.getMemberBirthday()!=null){
            gymMember.setMemberAge(getCurrentAge(gymMember.getMemberBirthday()));
        }
        return toAjax(gymMemberService.insertGymMember(gymMember));
    }

    /**
     * 修改会员管理
     */
    @PreAuthorize("@ss.hasPermi('gym:member:edit')")
    @Log(title = "会员管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GymMember gymMember)
    {
        if(gymMember.getMemberBirthday()!=null){
            gymMember.setMemberAge(getCurrentAge(gymMember.getMemberBirthday()));
         }
        return toAjax(gymMemberService.updateGymMember(gymMember));
    }

    /**
     * 删除会员管理
     */
    @PreAuthorize("@ss.hasPermi('gym:member:remove')")
    @Log(title = "会员管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{memberIds}")
    public AjaxResult remove(@PathVariable Long[] memberIds)
    {
        for(Long id :memberIds){
            GymRentalCabinet  gymRentalCabinet = new GymRentalCabinet();
            gymRentalCabinet.setMemberId(id);
            List<GymRentalCabinet>  gymRentalCabinets = gymRentalCabinetService.selectGymRentalCabinetList(gymRentalCabinet);
            for(GymRentalCabinet grc: gymRentalCabinets){
                grc.setMemberId(null);
                gymRentalCabinetService.updateGymRentalCabinetMember(grc);
            }
        }
        return toAjax(gymMemberService.deleteGymMemberByMemberIds(memberIds));
    }

    @GetMapping(value = "/apply/{memberId}/{time}")
    public AjaxResult applyCard(@PathVariable("memberId") Long memberId,@PathVariable("time") Integer time){
        GymVip gymVip = new GymVip();
        SysConfig value = sysConfigService.selectConfigById(6L);
        Long no = Long.parseLong(value.getConfigValue());
        String vipNo = "NO.";
        for(int i =0;i<6-value.getConfigValue().length();i++){
            vipNo+="0";
        }
        vipNo+=value.getConfigValue();
        //插入会员卡信息
        gymVip.setVipNo(vipNo);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());//设置起时间
        cal.add(Calendar.MONTH, time);
        gymVip.setEffective(cal.getTime());
        gymVipService.insertGymVip(gymVip);
        //更新会员信息
        GymMember member = gymMemberService.selectGymMemberByMemberId(memberId);
        member.setVipId(gymVip.getVipId());
        gymMemberService.updateGymMember(member);
        //更新配置信息
        value.setConfigValue(String.valueOf(no+1));
        sysConfigService.resetConfigCache();
        return toAjax(sysConfigService.updateConfig(value));
    }

    /**
     * 计算年龄的方法
     * @return 年龄
     */
    public int getCurrentAge(Date birthday) {
        // 当前时间
        Calendar curr = Calendar.getInstance();
        // 生日
        Calendar born = Calendar.getInstance();
        born.setTime(birthday);
        // 年龄 = 当前年 - 出生年
        int age = curr.get(Calendar.YEAR) - born.get(Calendar.YEAR);
        if (age <= 0) {
            return 0;
        }
        // 如果当前月份小于出生月份: age-1
        // 如果当前月份等于出生月份, 且当前日小于出生日: age-1
        int currMonth = curr.get(Calendar.MONTH);
        int currDay = curr.get(Calendar.DAY_OF_MONTH);
        int bornMonth = born.get(Calendar.MONTH);
        int bornDay = born.get(Calendar.DAY_OF_MONTH);
        if ((currMonth < bornMonth) || (currMonth == bornMonth && currDay <= bornDay)) {
            age--;
        }
        return age < 0 ? 0 : age;
    }
}
