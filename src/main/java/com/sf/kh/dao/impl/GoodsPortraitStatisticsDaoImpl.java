package com.sf.kh.dao.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;
import com.sf.kh.dao.IGoodsPortraitStatisticsDao;
import com.sf.kh.dao.mapper.PortraitStatisticsDepartmentMapper;
import com.sf.kh.dao.mapper.PortraitStatisticsMonthMapper;
import com.sf.kh.dao.mapper.PortraitStatisticsNameMapper;
import com.sf.kh.dao.mapper.PortraitStatisticsRateMapper;
import com.sf.kh.dao.mapper.PortraitStatisticsVersionMapper;
import com.sf.kh.dto.PortraitStatisticsDto;
import com.sf.kh.model.PortraitStatisticsDepartment;
import com.sf.kh.model.PortraitStatisticsMonth;
import com.sf.kh.model.PortraitStatisticsName;
import com.sf.kh.model.PortraitStatisticsRate;
import com.sf.kh.model.PortraitStatisticsVersion;

/****
 * 物资纵向统计
 * @author 866316
 *
 */
@Repository
public class GoodsPortraitStatisticsDaoImpl implements IGoodsPortraitStatisticsDao{
	
	private @Resource PortraitStatisticsVersionMapper portraitStatisticsVersionMapper;

    private @Resource PortraitStatisticsDepartmentMapper portraitStatisticsDepartmentMapper;
    
    private @Resource PortraitStatisticsRateMapper portraitStatisticsRateMapper;
    
    private @Resource PortraitStatisticsNameMapper portraitStatisticsNameMapper;
    
    private @Resource PortraitStatisticsMonthMapper portraitStatisticsMonthMapper;
    
	
	@Override
	public List<PortraitStatisticsDepartment> getDeptList(PortraitStatisticsDto record){
		return portraitStatisticsDepartmentMapper.getList(this.getMaxVersion(record));
	}


	@Override
	public List<PortraitStatisticsRate> getRateList(PortraitStatisticsDto record) {
		return portraitStatisticsRateMapper.getList(this.getMaxVersion(record));
	}
	
	@Override
	public List<PortraitStatisticsName> getNameList(PortraitStatisticsDto record){
		return portraitStatisticsNameMapper.getList(this.getMaxVersion(record));
	}
	
	@Override
	public List<PortraitStatisticsMonth> getMonthList(PortraitStatisticsDto record){
		return portraitStatisticsMonthMapper.getList(this.getMaxVersion(record));
	}
	
	private PortraitStatisticsDto getMaxVersion(PortraitStatisticsDto record){
		PortraitStatisticsVersion versionObj = portraitStatisticsVersionMapper.selectByMaxVersion();
		if(null == versionObj){
			record.setVersion(-1);
		}else{
			record.setVersion(versionObj.getVersion());
		}
		return record;
	}
}
