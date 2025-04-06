package com.org.busi.dao;

import com.org.core.core.Mapper;
import com.org.busi.model.AssetInfo;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;


public interface AssetInfoMapper extends Mapper<AssetInfo> {

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
    * 获取资产类型列表
    * @param map
    * @return
    */
    List<Map> getAssetTypeDatas(Map map);

    /**
    * 获取货币类型列表
    * @param map
    * @return
    */
    List<Map> getCurrencyDatas(Map map);

    /**
    * 获取所有人列表
    * @param map
    * @return
    */
    List<Map> getOwner_person_idDatas(Map map);

    /**
    * 获取资产状态列表
    * @param map
    * @return
    */
    List<Map> getAsset_statusDatas(Map map);

    /**
    * 获取支持证据列表
    * @param map
    * @return
    */
    List<Map> getSupportingEvidenceDatas(Map map);




}