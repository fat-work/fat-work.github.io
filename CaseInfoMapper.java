package com.org.busi.dao;

import com.org.core.core.Mapper;
import com.org.busi.model.CaseInfo;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;


public interface CaseInfoMapper extends Mapper<CaseInfo> {

    /**
    * 根据条件查询
    * @param params
    * @return
    */
    List<Map> listByCondition(Map params);

    /**
    * 获取案件状态列表
    * @param map
    * @return
    */
    List<Map> getCaseStatusDatas(Map map);

    /**
    * 获取负责人列表
    * @param map
    * @return
    */
    List<Map> getCreate_userDatas(Map map);

    /**
    * 获取案件类型列表
    * @param map
    * @return
    */
    List<Map> getCase_typeDatas(Map map);




}