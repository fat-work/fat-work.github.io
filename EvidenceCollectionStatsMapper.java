package com.org.busi.dao;

import com.org.core.core.Mapper;
import com.org.busi.model.EvidenceCollectionStats;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;


public interface EvidenceCollectionStatsMapper extends Mapper<EvidenceCollectionStats> {

    /**
    * 根据条件查询
    * @param params
    * @return
    */
    List<Map> listByCondition(Map params);

    /**
    * 获取案件ID列表
    * @param map
    * @return
    */
    List<Map> getCase_idDatas(Map map);

    /**
    * 获取探针ID列表
    * @param map
    * @return
    */
    List<Map> getProbe_idDatas(Map map);




}