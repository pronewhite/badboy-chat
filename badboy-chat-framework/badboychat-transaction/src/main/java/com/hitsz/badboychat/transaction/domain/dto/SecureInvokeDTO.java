package com.hitsz.badboychat.transaction.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/14 21:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecureInvokeDTO {

    private String methodName;
    private String className;
    private String parametersType;
    private String args;
}
