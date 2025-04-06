package com.org.busi.dao;

import com.org.core.core.Mapper;
import com.org.busi.model.ReportInfo;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;


public interface ReportInfoMapper extends Mapper<ReportInfo> {

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
    * 获取报告类型列表
    * @param map
    * @return
    */
    List<Map> getReportTypeDatas(Map map);

    /**
    * 获取生成人列表
    * @param map
    * @return
    */
    List<Map> getGenerate_user_idDatas(Map map);

    /**
    * 获取关联证据列表
    * @param map
    * @return
    */
    List<Map> getRelatedEvidenceDatas(Map map);




}