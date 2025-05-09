package com.org.busi.dao;

import com.org.core.core.Mapper;
import com.org.busi.model.PersonBehavior;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;


public interface PersonBehaviorMapper extends Mapper<PersonBehavior> {

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
    List<Map> getCase_idDatas(Map map);

    /**
    * 获取人物列表
    * @param map
    * @return
    */
    List<Map> getPerson_idDatas(Map map);

    /**
    * 获取行为类型列表
    * @param map
    * @return
    */
    List<Map> getBehavior_typeDatas(Map map);

    /**
    * 获取风险等级列表
    * @param map
    * @return
    */
    List<Map> getRiskLevelDatas(Map map);

    /**
    * 获取支持证据列表
    * @param map
    * @return
    */
    List<Map> getSupportingEvidenceDatas(Map map);




}