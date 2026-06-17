package com.gym.project.gym.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.gym.project.gym.domain.GymMember;
import com.gym.project.gym.service.IGymMemberService;
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
import com.gym.project.gym.domain.GymRentalCabinet;
import com.gym.project.gym.service.IGymRentalCabinetService;
import com.gym.framework.web.controller.BaseController;
import com.gym.framework.web.domain.AjaxResult;
import com.gym.common.utils.poi.ExcelUtil;
import com.gym.framework.web.page.TableDataInfo;

/**
 * 租柜Controller
 * 
 * @author gym
 * @date 2022-02-07
 */
@RestController
@RequestMapping("/gym/cabinet")
public class GymRentalCabinetController extends BaseController
{
    @Autowired
    private IGymRentalCabinetService gymRentalCabinetService;

    @Autowired
    private IGymMemberService memberService;

    /**
     * 查询租柜列表
     */
    @PreAuthorize("@ss.hasPermi('gym:cabinet:list')")
    @GetMapping("/list")
    public TableDataInfo list(GymRentalCabinet gymRentalCabinet)
    {
        startPage();
        List<GymRentalCabinet> list = gymRentalCabinetService.selectGymRentalCabinetList(gymRentalCabinet);
        return getDataTable(list);
    }
    @PreAuthorize("@ss.hasPermi('gym:cabinet:list')")
    @GetMapping("/member/list")
    public AjaxResult getMemberList()
    {
        return AjaxResult.success(memberService.selectGymMemberList(new GymMember()));
    }



    /**
     * 租柜续期续费
     */
    @PreAuthorize("@ss.hasPermi('gym:cabinet:edit')")
    @Log(title = "租柜续期续费", businessType = BusinessType.UPDATE)
    @PostMapping("/renewal")
    public AjaxResult  renewal(@RequestBody GymRentalCabinet gymRentalCabinet){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());//设置起时间
        cal.add(Calendar.MONTH, gymRentalCabinet.getRenewal());
        gymRentalCabinet.setCabinetDate(cal.getTime());
        return toAjax(gymRentalCabinetService.updateGymRentalCabinet(gymRentalCabinet));
    }


    /**
     * 导出租柜列表
     */
    @PreAuthorize("@ss.hasPermi('gym:cabinet:export')")
    @Log(title = "租柜", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GymRentalCabinet gymRentalCabinet)
    {
        List<GymRentalCabinet> list = gymRentalCabinetService.selectGymRentalCabinetList(gymRentalCabinet);
        ExcelUtil<GymRentalCabinet> util = new ExcelUtil<>(GymRentalCabinet.class);
        util.exportExcel(response, list, "租柜数据");
    }

    /**
     * 获取租柜详细信息
     */
    @PreAuthorize("@ss.hasPermi('gym:cabinet:query')")
    @GetMapping(value = "/{cabinetId}")
    public AjaxResult getInfo(@PathVariable("cabinetId") Long cabinetId)
    {
        return AjaxResult.success(gymRentalCabinetService.selectGymRentalCabinetByCabinetId(cabinetId));
    }

    /**
     * 新增租柜
     */
    @PreAuthorize("@ss.hasPermi('gym:cabinet:add')")
    @Log(title = "租柜", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GymRentalCabinet gymRentalCabinet)
    {
        return toAjax(gymRentalCabinetService.insertGymRentalCabinet(gymRentalCabinet));
    }

    /**
     * 修改租柜
     */
    @PreAuthorize("@ss.hasPermi('gym:cabinet:edit')")
    @Log(title = "租柜", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GymRentalCabinet gymRentalCabinet)
    {
        return toAjax(gymRentalCabinetService.updateGymRentalCabinet(gymRentalCabinet));
    }

    /**
     * 删除租柜
     */
    @PreAuthorize("@ss.hasPermi('gym:cabinet:remove')")
    @Log(title = "租柜", businessType = BusinessType.DELETE)
	@DeleteMapping("/{cabinetIds}")
    public AjaxResult remove(@PathVariable Long[] cabinetIds)
    {
        String returnStr = "租柜";
        boolean deleteFlag = false;

        for(Long id : cabinetIds){
            GymRentalCabinet gymRentalCabinet = gymRentalCabinetService.selectGymRentalCabinetByCabinetId(id);
            if(gymRentalCabinet.getMemberId()!=null){
                returnStr+="【"+gymRentalCabinet.getCabinetNo()+"】、";
                deleteFlag = true;
            }else {
                gymRentalCabinetService.deleteGymRentalCabinetByCabinetId(id);
            }
        }
        if (deleteFlag){
            returnStr = returnStr.substring(0,returnStr.length()-1);
            returnStr+="正在被使用，不允许删除！";
            return AjaxResult.error(returnStr);
        }else {
            return AjaxResult.success("删除成功！");
        }
    }


}
