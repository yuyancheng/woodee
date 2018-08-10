package com.sf.kh.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sf.kh.model.BaseDict;

/***
 * 字典表数据获取
 * @author 866316
 *
 */
public interface BaseDictMapper {
   /***
    * 根据类型获取专业、类别数据
    */
   List<BaseDict> getListByType(String type);
   /***
    * 获取插入的id号
    * @param baseDict
    * @return
    */
   int insert(BaseDict baseDict);
   
   /***
    * 根据类型和名字获取指定记录
    */
   BaseDict getBaseDictByTypeAndName(
		   @Param("type")String type,@Param("name")String name);
   
   /***
    * 根据类型和父id获取所有子集集合
    * @param type
    * @param parentId
    * @return
    */
   List<BaseDict> getSonsByTypeAndParentId(
		   @Param("type")String type,@Param("parentId")Long parentId);
   
   /***
    * 根据类型和父id获取所有子集集合
    * @param type
    * @param parentId
    * @return
    */
   List<BaseDict> getListByTypeAndObj(
		   @Param("type")String type,@Param("baseDict")BaseDict baseDict);
}