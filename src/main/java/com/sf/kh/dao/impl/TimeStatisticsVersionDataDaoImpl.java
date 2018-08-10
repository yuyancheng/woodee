package com.sf.kh.dao.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;
import com.sf.kh.dao.ITimeStatisticsVersionDataDao;
import com.sf.kh.dao.mapper.TimeStatisticsVersionDataMapper;
import com.sf.kh.model.TimeStatisticsVersionData;

/****
 * 物资统计-方向单位
 * @author 866316
 *
 */
@Repository
public class TimeStatisticsVersionDataDaoImpl implements ITimeStatisticsVersionDataDao{

    private @Resource TimeStatisticsVersionDataMapper timeStatisticsVersionDataMapper;

	@Override
	public List<TimeStatisticsVersionData> getListByDeptNosAndType(List<Long> deptNos,String type) {
		return timeStatisticsVersionDataMapper.selectByDeptNosAndType(deptNos,type);
	}


}
