package com.shanzhu.hospital.entity.vo.user;

import lombok.Data;

/**
 * 病患登录 返回对象
 *
 */
@Data
public class PatientUserVo {

    /**
     * 病患id (账号)
     */
    private Integer pId;

    /**
     * 病患名称
     */
    private String pName;

    /**
     * 证件号码
     */
    private String pCard;

    /**
     * 生成的账号token（验签）
     */
    private String token;

}

  