package com.sf.kh.controller;

import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sf.kh.dto.UserDto;
import com.sf.kh.exception.BusinessException;
import com.sf.kh.model.*;
import com.sf.kh.dto.query.ShipmentQueryDto;
import com.sf.kh.service.IDepartmentService;
import com.sf.kh.service.IMonitorService;
import com.sf.kh.util.Constants;
import com.sf.kh.util.ExcelUtil;
import com.sf.kh.util.WebContextHolder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: 01378178
 * @Date: 2018/7/5 19:22
 * @Description:
 */
@RestController
@RequestMapping("monitor")
public class MonitorController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private IMonitorService monitorService;

    @Resource
    private IDepartmentService departmentService;

    /**
     * 接收物资查询
     * @param dto
     * @return
     */
    @PostMapping("toReceiveList")
    public Result<Page<List<Shipment>>> toReceiveList(@RequestBody ShipmentQueryDto dto){

        String chosenCustNo = dto.getCustNo();
        List<String> filteredCustNo = validateAndFilterCustNo(chosenCustNo);

        List<Department> depts = departmentService.getDeptsByCustNoList(filteredCustNo);
        if(CollectionUtils.isEmpty(depts)){
            logger.warn("toReceiveList, no dept linked, cust no={}", filteredCustNo);
            return Result.success(new Page(Lists.newArrayList()));
        }

        List<Long> rcvCompanyIds = depts.stream().map(Department::getId).collect(Collectors.toList());

        Map<String, Object> params = Maps.newHashMap();

        params.put("custNos", filteredCustNo);
        params.put("rcvCompanyIds", rcvCompanyIds);
        params.put("waybillStatusCode", Shipment.WaybillStatusEnum.TORECEIVE.getStatus());
        params.put("waybillStatus", Shipment.WaybillStatusEnum.TORECEIVE.getDesc());
        params.put("pageNum", dto.getPageNum());
        params.put("pageSize", dto.getPageSize());

        List<Shipment> list = monitorService.getReceiveList(params);

        return Result.success(new Page(list));
    }


    @PostMapping("receivedList")
    public Result<Page<List<Shipment>>> receivedList(@RequestBody ShipmentQueryDto dto){

        String chosedCustNo = dto.getCustNo();

        List<String> filteredCustNo = validateAndFilterCustNo(chosedCustNo);

        List<Department> depts = departmentService.getDeptsByCustNoList(filteredCustNo);

        if(CollectionUtils.isEmpty(depts)){
            logger.warn("toReceiveList, no dept linked, cust no={}", filteredCustNo);
            return Result.success(new Page(Lists.newArrayList()));
        }

        List<Long> rcvCompanyIds = depts.stream().map(Department::getId).collect(Collectors.toList());

        Map<String, Object> params = Maps.newHashMap();

        params.put("custNos", filteredCustNo);
        params.put("rcvCompanyIds", rcvCompanyIds);
        params.put("waybillStatusCode", Shipment.WaybillStatusEnum.SIGNED.getStatus());
        params.put("waybillStatus", Shipment.WaybillStatusEnum.SIGNED.getDesc());
        params.put("pageNum", dto.getPageNum());
        params.put("pageSize", dto.getPageSize());
        params.put("bisNums", dto.getBisNums());

        List<Shipment> list = monitorService.getReceiveList(params);

        return Result.success(new Page(list));

    }

    public List<String> validateAndFilterCustNo(String chosenCustNo){

        User user = WebContextHolder.currentUser();
        if(user==null){
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "无用户信息");
        }

        List<String> custos = Lists.newArrayList();

        if(!isSuperAdmin(user)) {

            UserDto userDto = WebContextHolder.currentUserDto();

            custos = userDto.getCustNoList();
            if (CollectionUtils.isEmpty(custos)) {
                throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "该用户无关联月结卡号");
            }

            if (StringUtils.isNotBlank(chosenCustNo) && !"全部".equalsIgnoreCase(chosenCustNo)) {
                if (!custos.contains(chosenCustNo)) {
                    logger.warn("validateAndFilterCustNo user={}, chosedCustNo={}, bestowedCustNo={}", user.getId(), chosenCustNo, custos);
                    throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "用户无该月结卡号权限");
                }
                return Lists.newArrayList(chosenCustNo);
            } else {
                return custos;
            }
        }
        return custos;
    }

    /**
     *
     *
     * 付款方式
     * 寄件时间 from, to
     * 发货单位
     * 接收单位
     * 单号[发货单, 运单]
     * 状态
     * 月结卡号
     *
     * @return
     */
    @PostMapping("shipment")
    public Result<Page<List<Shipment>>> shipment(@RequestBody ShipmentQueryDto dto){

        // 组建参数
        Map<String, Object> params = buildQueryParameter(dto);
        params.put(Constants.PAGE_SIZE, dto.getPageSize());
        params.put(Constants.PAGE_NUM, dto.getPageNum());
        List<Shipment> shipments = monitorService.getShipments(params);
        return Result.success(new Page(CollectionUtils.isEmpty(shipments) ? Lists.newArrayList() : shipments));
    }


    @PostMapping(path="statistic")
    public Result<ShipmentStatistic> getStatistic(@RequestBody ShipmentQueryDto dto){

        ShipmentStatistic statistic = monitorService.getShipmentStatistic(buildQueryParameter(dto));
        return Result.success(statistic);
    }


    @GetMapping("shipmentDetail")
    public Result<List<ShipmentDetail>> shipmentDetail(@RequestParam(value = "batchId", required = true) Long batchId){
        return Result.success(monitorService.getShipmentDetail(batchId));
    }


    private boolean isSuperAdmin(User user){
        return user.getRoles()!=null && user.getRoles().stream().anyMatch(o->o.getRoleCode().equalsIgnoreCase(Constants.ROLE_MANAGER));
    }

    private Map<String, Object> buildQueryParameter(ShipmentQueryDto dto){

        User user = WebContextHolder.currentUser();
        if(user==null){
            logger.warn("get statistic no user");
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "不存在用户信息");
        }

        // 超级管理员, 无需月结卡号
        List<String> custos = Lists.newArrayList();
        if(!isSuperAdmin(user)) {
            UserDto userDto = WebContextHolder.currentUserDto();

            if (StringUtils.isNotBlank(dto.getCustNo()) && !"全部".equalsIgnoreCase(dto.getCustNo())) {
                custos.add(dto.getCustNo());
            } else {
                custos = userDto.getCustNoList();
                if (CollectionUtils.isEmpty(custos)) {
                    logger.warn("get shipment statistic, custos is empty userId={}", user.getId());
                    throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "用户无月结卡号");
                }
            }

        }

        // 页面参数， 发运单位参数， 对应的月结卡号.
        Long sendOrgTypeId = dto.getSendOrgTypeId();
        Long sendOrgId = dto.getSendOrgId();
        Long sendDeptId = dto.getSendDeptId();

        List<Long> sendDeptIds = departmentService.getDeptIdsAccordingly(sendOrgTypeId, sendOrgId, sendDeptId);

        // 页面参数, 接收单位参数， 对应的月结卡号.
        Long rcvOrgTypeId = dto.getRcvOrgTypeId();
        Long rcvOrgId = dto.getRcvOrgId();
        Long rcvDeptId = dto.getRcvOrgId();

        List<Long> rcvDeptIds = departmentService.getDeptIdsAccordingly(rcvOrgTypeId, rcvOrgId, rcvDeptId);


        String timeFrom = dto.getTimeFrom();
        String timeTo = dto.getTimeTo();
        String dateFormat = "yyyyMMdd";
        String fullDateFormat = "yyyy-MM-dd HH:mm:ss";

        String consignedTimeFrom = "";
        String consignedTimeTo = "";

        try {
            if (StringUtils.isNotBlank(timeFrom)) {
                Date fromDate = DateUtils.parseDate(timeFrom, dateFormat);
                consignedTimeFrom = DateFormatUtils.format(fromDate, fullDateFormat);
            }
            if(StringUtils.isNotBlank(timeTo)){
                Date toDate = DateUtils.parseDate(timeTo, dateFormat);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(toDate);
                calendar.set(Calendar.HOUR_OF_DAY,23);
                calendar.set(Calendar.MINUTE,59);
                calendar.set(Calendar.SECOND,59);

                consignedTimeTo = DateFormatUtils.format(calendar, fullDateFormat);
            }
        }catch(Exception e){
            logger.warn("convert date error, timeFrom={}, timeTo={}", timeFrom, timeTo);
        }

        Map<String, Object> params = Maps.newHashMap();
        params.put("bisNums", dto.getBisNums());
        params.put("paymentType", dto.getPaymentType());
        params.put("consignedTimeFrom", consignedTimeFrom);
        params.put("consignedTimeTo",   consignedTimeTo);
        params.put("sendCompanyIds", sendDeptIds);
        params.put("rcvCompanyIds",rcvDeptIds );
        params.put("custNos", custos);
        params.put("waybillStatusCode", dto.getWaybillStatusCode());
        params.put("waybillStatus", Shipment.WaybillStatusEnum.getDescByStatus(dto.getWaybillStatusCode()));

        return params;
    }


    @PostMapping(path="receivedExport")
    public void receivedExport(HttpSession session, HttpServletRequest request, HttpServletResponse response, @RequestBody  ShipmentQueryDto dto) {


        String chosedCustNo = dto.getCustNo();

        List<String> filteredCustNo = validateAndFilterCustNo(chosedCustNo);

        List<Department> depts = departmentService.getDeptsByCustNoList(filteredCustNo);

        if(CollectionUtils.isEmpty(depts)){
            logger.warn("toReceiveList, no dept linked, cust no={}", filteredCustNo);
            return ;
        }

        List<Long> rcvCompanyIds = depts.stream().map(Department::getId).collect(Collectors.toList());

        Map<String, Object> params = Maps.newHashMap();

        params.put("rcvCustNos", filteredCustNo);
        params.put("rcvCompanyIds", rcvCompanyIds);
        params.put("waybillStatusCode", Shipment.WaybillStatusEnum.SIGNED.getStatus());
        params.put("waybillStatus", Shipment.WaybillStatusEnum.SIGNED.getDesc());
        params.put("bisNums", dto.getBisNums());

        List<Shipment> shipmentsWithDetails = Lists.newArrayList();
        Integer count = monitorService.getShipmentsWithDetailCount(params);
        if(count==null){
            logger.warn("receivedExport get shipment count, null found. param={}", params);
        }else if(count>60000){
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(),"查询数量太大，请缩小时间范围查询.");
        }else if(count!=0){
            shipmentsWithDetails = monitorService.getShipmentsWithDetail(params);
        }

        List<String> header = Arrays.asList("单号", "发运时间", "签收时间","状态", "发运批次号",
                "发运单位", "接收单位","包裹类型", "专业", "类别", "品名", "单位", "数量");

        List<String[]> body = Lists.newArrayList();

        if(CollectionUtils.isNotEmpty(shipmentsWithDetails)){

            for(Shipment s : shipmentsWithDetails){

                List<String> row = null;
                List<ShipmentDetail> ds = s.getShipmentDetails();

                if(CollectionUtils.isEmpty(ds)){
                    row = Lists.newArrayList();
                    row.add(s.getWaybillNo());
                    row.add(s.getConsignedTimeStr());
                    row.add(s.getSignInTimStr());
                    row.add(s.getWaybillStatus());
                    row.add(s.getBatchId().toString());
                    row.add(s.getSendAddr());
                    row.add(s.getRcvAddr());
                    row.add(StringUtils.isBlank(s.getPackageType())? "" : (s.getPackageType().equals("1") ? "单品" : "混合"));
                    body.add(row.toArray(new String[row.size()]));
                }else{
                    for(ShipmentDetail d : ds){
                        row = Lists.newArrayList();
                        row.add(s.getWaybillNo());
                        row.add(s.getConsignedTimeStr());
                        row.add(s.getSignInTimStr());
                        row.add(s.getWaybillStatus());
                        row.add(s.getBatchId()+"");
                        row.add(s.getSendAddr());
                        row.add(s.getRcvAddr());
                        row.add(StringUtils.isBlank(s.getPackageType())? "" : (s.getPackageType().equals("1") ? "单品" : "混合"));
                        row.add(d.getCategory());
                        row.add(d.getChildCategory());
                        row.add(d.getName());
                        row.add(d.getUnit());
                        row.add(d.getGoodsNum()+"");
                        body.add(row.toArray(new String[row.size()]));
                    }
                }
            }

        }
        ExcelUtil.exportData("接收明细", header.toArray(new String[header.size()]), body, request, response);

    }



    @PostMapping(path="shipmentExport")
    public void shipmentExport(HttpSession session, HttpServletRequest request, HttpServletResponse response, @RequestBody  ShipmentQueryDto dto){

        Map<String, Object> params = buildQueryParameter(dto);
        List<Shipment> shipmentsWithDetails = Lists.newArrayList();
        Integer count = monitorService.getShipmentsWithDetailCount(params);
        if(count==null){
            logger.warn("receivedExport get shipment count, null found. param={}", params);
        }else if(count>60000){
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(),"查询数量太大，请缩小时间范围查询.");
        }else if(count!=0){
            shipmentsWithDetails = monitorService.getShipmentsWithDetail(params);
        }

        List<String> header = Arrays.asList("单号", "发运时间", "状态", "发运批次号",
                "发运单位", "接收单位","包裹类型", "专业", "类别", "品名", "单位", "数量");

        List<String[]> body = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(shipmentsWithDetails)){

            for(Shipment s : shipmentsWithDetails){
                List<String> row = null;
                List<ShipmentDetail> ds = s.getShipmentDetails();
                if(CollectionUtils.isEmpty(ds)){
                    row = Lists.newArrayList();
                    row.add(s.getWaybillNo());
                    row.add(s.getConsignedTimeStr());
                    row.add(s.getWaybillStatus());
                    row.add(s.getBatchId()+"");
                    row.add(s.getSendAddr());
                    row.add(s.getRcvAddr());
                    row.add(StringUtils.isBlank(s.getPackageType())? "" : (s.getPackageType().equals("1") ? "单品" : "混合"));
                    body.add(row.toArray(new String[row.size()]));
                }else{
                    for(ShipmentDetail d : ds){
                        row = Lists.newArrayList();
                        row.add(s.getWaybillNo());
                        row.add(s.getConsignedTimeStr());
                        row.add(s.getWaybillStatus());
                        row.add(s.getBatchId()+"");
                        row.add(s.getSendAddr());
                        row.add(s.getRcvAddr());
                        row.add(StringUtils.isBlank(s.getPackageType())? "" : (s.getPackageType().equals("1") ? "单品" : "混合"));
                        row.add(d.getCategory());
                        row.add(d.getChildCategory());
                        row.add(d.getName());
                        row.add(d.getUnit());
                        row.add(d.getGoodsNum()+"");
                        body.add(row.toArray(new String[row.size()]));
                    }
                }

            }
        }
        ExcelUtil.exportData("发运明细", header.toArray(new String[header.size()]), body, request, response);
    }


}
