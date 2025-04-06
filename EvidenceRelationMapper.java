package com.org.busi.dao;

import com.org.core.core.Mapper;
import com.org.busi.model.EvidenceRelation;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;


public interface EvidenceRelationMapper extends Mapper<EvidenceRelation> {

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
    * 获取证据列表
    * @param map
    * @return
    */
    List<Map> getEvidence_idDatas(Map map);

    /**
    * 获取关联证据列表
    * @param map
    * @return
    */
    List<Map> getRelated_evidence_idDatas(Map map);

    /**
    * 获取关系类型列表
    * @param map
    * @return
    */
    List<Map> getRelation_typeDatas(Map map);




}