package com.sf.kh.dao.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;
import com.sf.kh.dao.ICategoryStatisticsVersionDataDao;
import com.sf.kh.dao.mapper.CategoryStatisticsVersionDataMapper;
import com.sf.kh.model.CategoryStatisticsVersionData;

/****
 * 物资统计-物资类别
 * @author 866316
 *
 */
@Repository
public class CategoryStatisticsVersionDataDaoImpl implements ICategoryStatisticsVersionDataDao {

    private @Resource CategoryStatisticsVersionDataMapper categoryStatisticsVersionDataMapper;

	@Override
	public List<CategoryStatisticsVersionData> getListByDeptNosAndType(
			List<Long> deptNos, String type) {
		return categoryStatisticsVersionDataMapper.selectByDeptNosAndType(deptNos, type);
	}

}
