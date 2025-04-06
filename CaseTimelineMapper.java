package com.org.busi.dao;

import com.org.core.core.Mapper;
import com.org.busi.model.CaseTimeline;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;


public interface CaseTimelineMapper extends Mapper<CaseTimeline> {

    /**
    * 根据条件查询
    * @param params
    * @return
    */
    List<Map> listByCondition(Map params);

    /**
    * 获取案件id列表
    * @param map
    * @return
    */
    List<Map> getCaseIDDatas(Map map);

    /**
    * 获取重要性列表
    * @param map
    * @return
    */
    List<Map> getImportanceDatas(Map map);




}