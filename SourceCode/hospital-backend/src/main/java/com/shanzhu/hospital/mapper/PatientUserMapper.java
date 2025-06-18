package com.shanzhu.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shanzhu.hospital.entity.po.Patient;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 病患相关 持久层 （mapper）
 *
 
 * @date: 2023-11-17
 */
@Mapper
public interface PatientUserMapper extends BaseMapper<Patient> {

    /**
     * 查询患者年龄访问
     *
     * @param startAge 开始年龄
     * @param endAge 结束年龄
     * @return 数量
     */
    Integer patientAge(@Param("startAge") int startAge, @Param("endAge") int endAge);

}
