package com.org.busi.dao;

import com.org.core.core.Mapper;
import com.org.busi.model.PersonInfo;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;


public interface PersonInfoMapper extends Mapper<PersonInfo> {

    /**
    * 根据条件查询
    * @param params
    * @return
    */
    List<Map> listByCondition(Map params);

    /**
    * 获取所属案件列表
    * @param map
    * @return
    */
    List<Map> getBelongingCaseDatas(Map map);

    /**
    * 获取性别列表
    * @param map
    * @return
    */
    List<Map> getGenderDatas(Map map);

    /**
    * 获取重要性列表
    * @param map
    * @return
    */
    List<Map> getImportanceDatas(Map map);




}