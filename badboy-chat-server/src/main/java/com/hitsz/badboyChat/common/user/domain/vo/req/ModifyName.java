package com.hitsz.badboyChat.common.user.domain.vo.req;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/27 20:26
 */
@Data
public class ModifyName {

    @NotNull
    @Length(max = 10,message = "用户名字不能太长")
    private String name;
}
