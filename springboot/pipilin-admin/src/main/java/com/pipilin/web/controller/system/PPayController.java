package com.pipilin.web.controller.system;

import com.pipilin.common.annotation.Log;
import com.pipilin.common.core.controller.BaseController;
import com.pipilin.common.core.domain.AjaxResult;
import com.pipilin.common.core.page.TableDataInfo;
import com.pipilin.common.enums.BusinessType;
import com.pipilin.system.domain.PPay;
import com.pipilin.system.service.IPPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 充值记录信息Controller
 * 
 * 
 * . 2023-03-03
 */
@RestController
@RequestMapping("/system/pay")
public class PPayController extends BaseController
{
    @Autowired
    private IPPayService pPayService;

    /**
     * 查询充值记录信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:pay:list')")
    @GetMapping("/list")
    public TableDataInfo list(PPay pPay)
    {
        startPage();
        List<PPay> list = pPayService.selectPPayList(pPay);
        return getDataTable(list);
    }

    /**
     * 获取充值记录信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:pay:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(pPayService.selectPPayById(id));
    }

    /**
     * 新增充值记录信息
     */
    @PreAuthorize("@ss.hasPermi('system:pay:add')")
    @Log(title = "充值记录信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PPay pPay)
    {
        return toAjax(pPayService.insertPPay(pPay));
    }

    /**
     * 修改充值记录信息
     */
    @PreAuthorize("@ss.hasPermi('system:pay:edit')")
    @Log(title = "充值记录信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PPay pPay)
    {
        return toAjax(pPayService.updatePPay(pPay));
    }

    /**
     * 删除充值记录信息
     */
    @PreAuthorize("@ss.hasPermi('system:pay:remove')")
    @Log(title = "充值记录信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(pPayService.deletePPayByIds(ids));
    }
}
