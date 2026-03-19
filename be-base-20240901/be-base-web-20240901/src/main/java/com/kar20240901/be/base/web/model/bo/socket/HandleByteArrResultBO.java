package com.kar20240901.be.base.web.model.bo.socket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HandleByteArrResultBO<T> {

    /**
     * 字符串数据
     */
    public String text;

    /**
     * dto对象
     */
    public T dto;

    /**
     * 二进制数据
     */
    public byte[] byteDataArr;

}
