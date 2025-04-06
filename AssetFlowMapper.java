package com.org.busi.dao;

import com.org.core.core.Mapper;
import com.org.busi.model.AssetFlow;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;


public interface AssetFlowMapper extends Mapper<AssetFlow> {

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
    * 获取资产列表
    * @param map
    * @return
    */
    List<Map> getAsset_idDatas(Map map);

    /**
    * 获取流动类型列表
    * @param map
    * @return
    */
    List<Map> getFlowTypeDatas(Map map);

    /**
    * 获取货币类型列表
    * @param map
    * @return
    */
    List<Map> getCurrencyDatas(Map map);

    /**
    * 获取来源人物列表
    * @param map
    * @return
    */
    List<Map> getFrom_person_idDatas(Map map);

    /**
    * 获取目标人物列表
    * @param map
    * @return
    */
    List<Map> getTo_person_idDatas(Map map);

    /**
    * 获取关联资产列表
    * @param map
    * @return
    */
    List<Map> getRelated_asset_idDatas(Map map);

    /**
    * 获取支持证据列表
    * @param map
    * @return
    */
    List<Map> getSupportingEvidenceDatas(Map map);




}