package com.org.busi.dao;

import com.org.core.core.Mapper;
import com.org.busi.model.ProbeInfo;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;


public interface ProbeInfoMapper extends Mapper<ProbeInfo> {

    /**
    * 根据条件查询
    * @param params
    * @return
    */
    List<Map> listByCondition(Map params);

    /**
    * 获取状态列表
    * @param map
    * @return
    */
    List<Map> getStateDatas(Map map);




}