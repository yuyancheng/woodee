package com.sf.kh.dao.mapper;

import java.util.List;
import java.util.Map;
import com.sf.kh.model.ImportLog;

public interface ImportLogMapper {

    int insert(ImportLog record);
    
    List<ImportLog> query4list(Map<String, ?> params);
    
    int updateById(ImportLog record);
    
    int updateByFileKey(ImportLog record);
}