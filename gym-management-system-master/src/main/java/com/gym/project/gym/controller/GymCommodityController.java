package com.gym.project.gym.controller;

import java.util.ArrayList;
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
import com.gym.project.gym.domain.GymCommodity;
import com.gym.project.gym.service.IGymCommodityService;
import com.gym.framework.web.controller.BaseController;
import com.gym.framework.web.domain.AjaxResult;
import com.gym.common.utils.poi.ExcelUtil;
import com.gym.framework.web.page.TableDataInfo;

/**
 * 商品Controller
 * 
 * @author gym
 * @date 2022-01-27
 */
@RestController
@RequestMapping("/operation/commodity")
public class GymCommodityController extends BaseController
{
    @Autowired
    private IGymCommodityService gymCommodityService;

    /**
     * 查询商品列表
     */
    @PreAuthorize("@ss.hasPermi('operation:commodity:list')")
    @GetMapping("/list")
    public TableDataInfo list(GymCommodity gymCommodity)
    {
        startPage();
        List<GymCommodity> list = gymCommodityService.selectGymCommodityList(gymCommodity);
        return getDataTable(list);
    }

    /**
     * 导出商品列表
     */
    @PreAuthorize("@ss.hasPermi('operation:commodity:export')")
    @Log(title = "商品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GymCommodity gymCommodity)
    {
        List<GymCommodity> list = gymCommodityService.selectGymCommodityList(gymCommodity);
        ExcelUtil<GymCommodity> util = new ExcelUtil<GymCommodity>(GymCommodity.class);
        util.exportExcel(response, list, "商品数据");
    }

    /**
     * 获取商品详细信息
     */
    @PreAuthorize("@ss.hasPermi('operation:commodity:query')")
    @GetMapping(value = "/{commodityId}")
    public AjaxResult getInfo(@PathVariable("commodityId") Long commodityId)
    {
        return AjaxResult.success(gymCommodityService.selectGymCommodityByCommodityId(commodityId));
    }
    @GetMapping(value = "/charts")
    public AjaxResult getChartInfo()
    {
        AjaxResult ajax = AjaxResult.success();
        List<GymCommodity> gymCommodities = gymCommodityService.selectGymCommodityList(new GymCommodity());
        List<String> name = new ArrayList<>();
        List<Long> number = new ArrayList<>();
        for(GymCommodity g:gymCommodities){
            name.add(g.getCommodityName());
            number.add(g.getCommodityNumber());
        }
        ajax.put("c",name);
        ajax.put("d",number);
        return ajax;
    }

    /**
     * 新增商品
     */
    @PreAuthorize("@ss.hasPermi('operation:commodity:add')")
    @Log(title = "商品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GymCommodity gymCommodity)
    {
        return toAjax(gymCommodityService.insertGymCommodity(gymCommodity));
    }

    @PreAuthorize("@ss.hasPermi('operation:commodity:edit')")
    @Log(title = "商品", businessType = BusinessType.UPDATE)
    @PostMapping("/modify/{status}")
    public AjaxResult modifyInventory(@PathVariable("status") String status,@RequestBody GymCommodity gymCommodit){
        Long total = gymCommodit.getCommodityNumber();
        Long number = gymCommodit.getInputOrOutput();
        if(status.equals("in")){
            gymCommodit.setCommodityNumber(total+number);
        }else if(status.equals("out")){
            gymCommodit.setCommodityNumber(total-number);
        }
        return toAjax(gymCommodityService.updateGymCommodity(gymCommodit));
    }
    /**
     * 修改商品
     */
    @PreAuthorize("@ss.hasPermi('operation:commodity:edit')")
    @Log(title = "商品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GymCommodity gymCommodity)
    {
        return toAjax(gymCommodityService.updateGymCommodity(gymCommodity));
    }

    /**
     * 删除商品
     */
    @PreAuthorize("@ss.hasPermi('operation:commodity:remove')")
    @Log(title = "商品", businessType = BusinessType.DELETE)
	@DeleteMapping("/{commodityIds}")
    public AjaxResult remove(@PathVariable Long[] commodityIds)
    {
        String returnStr = "商品";
        boolean deleteFlag = false;
        for(Long id : commodityIds){
            GymCommodity gymCommodity = gymCommodityService.selectGymCommodityByCommodityId(id);
            if(gymCommodity.getCommodityNumber()>0){
                returnStr+="【"+gymCommodity.getCommodityName()+"】、";
                deleteFlag = true;
            }else {
                gymCommodityService.deleteGymCommodityByCommodityId(id);
            }
        }
        if (deleteFlag){
            returnStr = returnStr.substring(0,returnStr.length()-1);
            returnStr+="数量不为零，不允许删除！";
            return AjaxResult.error(returnStr);
        }else {
            return AjaxResult.success("删除成功！");
        }
    }
}
