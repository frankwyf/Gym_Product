package com.gym.project.gym.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.gym.project.gym.domain.GymVipUsage;
import com.gym.project.gym.service.IGymVipUsageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.gym.framework.aspectj.lang.annotation.Log;
import com.gym.framework.aspectj.lang.enums.BusinessType;
import com.gym.project.gym.domain.GymVip;
import com.gym.project.gym.service.IGymVipService;
import com.gym.framework.web.controller.BaseController;
import com.gym.framework.web.domain.AjaxResult;
import com.gym.common.utils.poi.ExcelUtil;
import com.gym.framework.web.page.TableDataInfo;

/**
 * 会员卡管理Controller
 * 
 * @author gym
 * @date 2022-01-20
 */
@RestController
@RequestMapping("/gym/vip")
public class GymVipController extends BaseController
{
    @Autowired
    private IGymVipService gymVipService;
    @Autowired
    private IGymVipUsageService gymVipUsageService;

    /**
     * 查询会员卡管理列表
     */
    @PreAuthorize("@ss.hasPermi('gym:vip:list')")
    @GetMapping("/list")
    public TableDataInfo list(GymVip gymVip)
    {
        startPage();
        List<GymVip> list = gymVipService.selectGymVipList(gymVip);
        return getDataTable(list);
    }

    /**
     * 导出会员卡管理列表
     */
    @PreAuthorize("@ss.hasPermi('gym:vip:export')")
    @Log(title = "会员卡管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GymVip gymVip)
    {
        List<GymVip> list = gymVipService.selectGymVipList(gymVip);
        ExcelUtil<GymVip> util = new ExcelUtil<GymVip>(GymVip.class);
        util.exportExcel(response, list, "会员卡管理数据");
    }

    /**
     * 获取会员卡管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('gym:vip:query')")
    @GetMapping(value = "/{vipId}")
    public AjaxResult getInfo(@PathVariable("vipId") Long vipId)
    {
        return AjaxResult.success(gymVipService.selectGymVipByVipId(vipId));
    }

    /**
     * 新增会员卡管理
     */
    @PreAuthorize("@ss.hasPermi('gym:vip:add')")
    @Log(title = "会员卡管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GymVip gymVip)
    {
        return toAjax(gymVipService.insertGymVip(gymVip));
    }

    /**
     * 修改会员卡管理
     */
    @PreAuthorize("@ss.hasPermi('gym:vip:edit')")
    @Log(title = "会员卡管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GymVip gymVip)
    {
        return toAjax(gymVipService.updateGymVip(gymVip));
    }

    /**
     * 到店签到 增加会员卡使用记录
     */
    @PreAuthorize("@ss.hasPermi('gym:vip:edit')")
    @Log(title = "会员卡管理", businessType = BusinessType.UPDATE)
    @GetMapping("/sign/in/{vipId}")
    public AjaxResult signIn(@PathVariable("vipId") Long vipId)
    {
        GymVipUsage gymVipUsage = new GymVipUsage();
        gymVipUsage.setVipId(vipId);
        gymVipUsage.setDate(new Date());
        return toAjax(gymVipUsageService.insertGymVipUsage(gymVipUsage));
    }

    /**
     * 会员卡续费
     */
    @PreAuthorize("@ss.hasPermi('gym:vip:edit')")
    @Log(title = "会员卡续费", businessType = BusinessType.UPDATE)
    @PostMapping("/renewal")
    public AjaxResult  renewal(@RequestBody GymVip gymVip){
        if(gymVip.getRenewal()!=null){
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());//设置起时间
            cal.add(Calendar.MONTH, gymVip.getRenewal());
            gymVip.setEffective(cal.getTime());
        }
        return toAjax(gymVipService.updateGymVip(gymVip));
    }

    /**
     * 删除会员卡管理
     */
    @PreAuthorize("@ss.hasPermi('gym:vip:remove')")
    @Log(title = "会员卡管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{vipIds}")
    public AjaxResult remove(@PathVariable Long[] vipIds)
    {
        List<GymVipUsage> list = gymVipUsageService.selectGymVipUsageByVipId(vipIds[0]);
        if(list.size()>0) return AjaxResult.error("会员卡已被使用不允许删除");
        return toAjax(gymVipService.deleteGymVipByVipIds(vipIds));
    }
}
