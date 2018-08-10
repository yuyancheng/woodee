package com.sf.kh.controller;

import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;
import com.sf.kh.model.WaybillRoute;
import com.sf.kh.service.IWaybillRouteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther: 01378178
 * @Date: 2018/7/9 16:26
 * @Description:
 */
@RestController
@RequestMapping("router")
public class WaybillRouterController {

    @Resource
    private IWaybillRouteService waybillRouteService;

    @GetMapping(path = "getByWaybillNo")
    public Result<List<WaybillRoute>> getRouters(@RequestParam("waybillNo") String waybillNo){

        if(StringUtils.isBlank(waybillNo)){
            return Result.failure(ResultCode.BAD_REQUEST, "运单号不能为空");
        }

        return Result.success(waybillRouteService.getByWaybillNo(waybillNo));
    }

}
