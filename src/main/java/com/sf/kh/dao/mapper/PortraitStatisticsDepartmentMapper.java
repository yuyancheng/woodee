package com.sf.kh.dao.mapper;

import java.util.List;

import com.sf.kh.dto.PortraitStatisticsDto;
import com.sf.kh.model.PortraitStatisticsDepartment;
/***
 * 物资纵向统计-方向部门
 * @author 866316
 *
 */
public interface PortraitStatisticsDepartmentMapper {
    /***
     * 根据提供的参数获取记录
     * @param record
     * @return
     */
    List<PortraitStatisticsDepartment> getList(PortraitStatisticsDto  record);
}