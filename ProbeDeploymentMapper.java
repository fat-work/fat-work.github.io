package com.org.busi.dao;

import com.org.core.core.Mapper;
import com.org.busi.model.ProbeDeployment;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;


public interface ProbeDeploymentMapper extends Mapper<ProbeDeployment> {

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
    * 获取探针列表
    * @param map
    * @return
    */
    List<Map> getProbe_idDatas(Map map);

    /**
    * 获取部署人员列表
    * @param map
    * @return
    */
    List<Map> getDeploy_user_idDatas(Map map);

    /**
    * 获取部署状态列表
    * @param map
    * @return
    */
    List<Map> getDeploymentStatusDatas(Map map);




}