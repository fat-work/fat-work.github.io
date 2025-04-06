package com.org.busi.dao;

import com.org.core.core.Mapper;
import com.org.busi.model.EvidenceInfo;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;


public interface EvidenceInfoMapper extends Mapper<EvidenceInfo> {

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
    * 获取采集探针列表
    * @param map
    * @return
    */
    List<Map> getProbe_idDatas(Map map);

    /**
    * 获取证据类型列表
    * @param map
    * @return
    */
    List<Map> getEvidenceTypeDatas(Map map);

    /**
    * 获取设备类型列表
    * @param map
    * @return
    */
    List<Map> getDeviceTypeDatas(Map map);

    /**
    * 获取重要性列表
    * @param map
    * @return
    */
    List<Map> getImportanceDatas(Map map);

    /**
    * 获取关联人物列表
    * @param map
    * @return
    */
    List<Map> getRelatedPersonsDatas(Map map);




}