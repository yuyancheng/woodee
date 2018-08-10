package com.sf.kh.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.sf.kh.dao.IOverviewVersionDataDao;
import com.sf.kh.dao.mapper.OverviewVersionDataMapper;
import com.sf.kh.dao.mapper.OverviewVersionMapper;
import com.sf.kh.model.OverviewVersion;
import com.sf.kh.model.OverviewVersionData;
import com.sf.kh.util.DateUtil;

/****
 * 基础维表dao
 * @author 866316
 *
 */
@Repository
public class OverviewVersionDataImpl implements IOverviewVersionDataDao {

    private @Resource OverviewVersionDataMapper overviewVersionDataMapper;
    
    private @Resource OverviewVersionMapper overviewVersionMapper;

	@Override
	public List<OverviewVersionData> getListByCompanyIdsAndType(List<String> companyIds,String type) {
		OverviewVersion temp = overviewVersionMapper.selectMaxVersion();
		if(null == temp){
			return new ArrayList<OverviewVersionData>();
		}
		List<OverviewVersionData> result = overviewVersionDataMapper.selectByCompanyIdsAndTypeAndVersion(
				companyIds, type, temp.getVersion());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date updateDate;
		try {
			updateDate = sdf.parse(temp.getUpdateTime());
		} catch (ParseException e) {
			e.printStackTrace();
			updateDate = new Date();
		}
		for(OverviewVersionData ovd : result){
			ovd.setDate(DateUtil.formatDateYmDhMCh(updateDate));
		}
		return result;
	}
}
