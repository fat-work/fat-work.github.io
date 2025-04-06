package com.org.busi.dao;

import com.org.core.core.Mapper;
import com.org.busi.model.WarningInfo;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;


public interface WarningInfoMapper extends Mapper<WarningInfo> {

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
    * 获取预警级别列表
    * @param map
    * @return
    */
    List<Map> getWarningLevelDatas(Map map);

    /**
    * 获取预警类型列表
    * @param map
    * @return
    */
    List<Map> getWarning_typeDatas(Map map);

    /**
    * 获取关联人物列表
    * @param map
    * @return
    */
    List<Map> getRelatedPersonsDatas(Map map);

    /**
    * 获取关联证据列表
    * @param map
    * @return
    */
    List<Map> getRelatedEvidenceDatas(Map map);

    /**
    * 获取状态列表
    * @param map
    * @return
    */
    List<Map> getStateDatas(Map map);

    /**
    * 获取处理人列表
    * @param map
    * @return
    */
    List<Map> getHandle_user_idDatas(Map map);




}