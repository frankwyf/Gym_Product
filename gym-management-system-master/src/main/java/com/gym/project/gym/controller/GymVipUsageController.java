package com.gym.project.gym.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.gym.project.gym.domain.GymVipUsage;
import com.gym.project.gym.service.IGymVipUsageService;
import com.gym.framework.web.controller.BaseController;
import com.gym.framework.web.domain.AjaxResult;
import com.gym.common.utils.poi.ExcelUtil;
import com.gym.framework.web.page.TableDataInfo;

/**
 * 会员卡使用记录Controller
 * 
 * @author gym
 * @date 2022-01-23
 */
@RestController
@RequestMapping("/gym/usage")
public class GymVipUsageController extends BaseController
{
    @Autowired
    private IGymVipUsageService gymVipUsageService;

    /**
     * 查询会员卡使用记录列表
     */
    @PreAuthorize("@ss.hasPermi('gym:usage:list')")
    @GetMapping("/list")
    public TableDataInfo list(GymVipUsage gymVipUsage)
    {
        startPage();
        List<GymVipUsage> list = gymVipUsageService.selectGymVipUsageList(gymVipUsage);
        return getDataTable(list);
    }

    /**
     * 导出会员卡使用记录列表
     */
    @PreAuthorize("@ss.hasPermi('gym:usage:export')")
    @Log(title = "会员卡使用记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GymVipUsage gymVipUsage)
    {
        List<GymVipUsage> list = gymVipUsageService.selectGymVipUsageList(gymVipUsage);
        ExcelUtil<GymVipUsage> util = new ExcelUtil<>(GymVipUsage.class);
        util.exportExcel(response, list, "会员卡使用记录数据");
    }

    /**
     * 获取会员卡使用记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('gym:usage:query')")
    @GetMapping(value = "/{usageId}")
    public AjaxResult getInfo(@PathVariable("usageId") Long usageId)
    {
        return AjaxResult.success(gymVipUsageService.selectGymVipUsageByUsageId(usageId));
    }

    /**
     * 新增会员卡使用记录
     */
    @PreAuthorize("@ss.hasPermi('gym:usage:add')")
    @Log(title = "会员卡使用记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GymVipUsage gymVipUsage)
    {
        return toAjax(gymVipUsageService.insertGymVipUsage(gymVipUsage));
    }

    /**
     * 修改会员卡使用记录
     */
    @PreAuthorize("@ss.hasPermi('gym:usage:edit')")
    @Log(title = "会员卡使用记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GymVipUsage gymVipUsage)
    {
        return toAjax(gymVipUsageService.updateGymVipUsage(gymVipUsage));
    }

    /**
     * 删除会员卡使用记录
     */
    @PreAuthorize("@ss.hasPermi('gym:usage:remove')")
    @Log(title = "会员卡使用记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{usageIds}")
    public AjaxResult remove(@PathVariable Long[] usageIds)
    {
        return toAjax(gymVipUsageService.deleteGymVipUsageByUsageIds(usageIds));
    }
}
