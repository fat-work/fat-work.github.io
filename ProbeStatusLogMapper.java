package com.org.busi.dao;

import com.org.core.core.Mapper;
import com.org.busi.model.ProbeStatusLog;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;


public interface ProbeStatusLogMapper extends Mapper<ProbeStatusLog> {

    /**
    * 根据条件查询
    * @param params
    * @return
    */
    List<Map> listByCondition(Map params);

    /**
    * 获取探针列表
    * @param map
    * @return
    */
    List<Map> getProbe_idDatas(Map map);

    /**
    * 获取所属案件列表
    * @param map
    * @return
    */
    List<Map> getCase_idDatas(Map map);

    /**
    * 获取状态列表
    * @param map
    * @return
    */
    List<Map> getStateDatas(Map map);




}