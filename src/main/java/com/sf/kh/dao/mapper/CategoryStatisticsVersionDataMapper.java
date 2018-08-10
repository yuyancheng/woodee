package com.sf.kh.dao.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.sf.kh.model.CategoryStatisticsVersionData;
/***
 * 物资统计-类别
 */
public interface CategoryStatisticsVersionDataMapper {
    /***
     * 
     * @param deptNos
     * @param type:1,发运物资；2，收运物资
     * @return
     */
	List<CategoryStatisticsVersionData> selectByDeptNosAndType(@Param("deptNos")List<Long> deptNos,
			@Param("type") String type);
}