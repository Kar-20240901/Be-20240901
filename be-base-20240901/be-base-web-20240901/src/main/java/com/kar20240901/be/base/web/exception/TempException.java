package com.kar20240901.be.base.web.exception;

import cn.hutool.json.JSONUtil;
import com.kar20240901.be.base.web.model.vo.base.R;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TempException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private R<?> r;

    public TempException(R<?> r) {

        super(JSONUtil.toJsonStr(r)); // 把信息封装成json格式

        setR(r);

    }

}
