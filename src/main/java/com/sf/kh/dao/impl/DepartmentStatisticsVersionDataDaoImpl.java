package com.sf.kh.dao.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;
import com.sf.kh.dao.IDepartmentStatisticsVersionDataDao;
import com.sf.kh.dao.mapper.DepartmentStatisticsVersionDataMapper;
import com.sf.kh.model.DepartmentStatisticsVersionData;

/****
 * 物资统计-方向单位
 * @author 866316
 *
 */
@Repository
public class DepartmentStatisticsVersionDataDaoImpl implements IDepartmentStatisticsVersionDataDao {

    private @Resource DepartmentStatisticsVersionDataMapper departmentStatisticsVersionDataMapper;

	@Override
	public List<DepartmentStatisticsVersionData> selectByDeptNosAndType(
			List<Long> deptList, String type) {
		return departmentStatisticsVersionDataMapper.selectByDeptNosAndType(deptList, type);
	}

}
