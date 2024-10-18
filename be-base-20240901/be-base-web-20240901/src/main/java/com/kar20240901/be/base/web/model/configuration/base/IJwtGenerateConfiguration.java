package com.kar20240901.be.base.web.model.configuration.base;

import cn.hutool.json.JSONObject;
import com.kar20240901.be.base.web.model.enums.base.BaseRequestCategoryEnum;
import com.kar20240901.be.base.web.model.vo.base.SignInVO;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;

public interface IJwtGenerateConfiguration {

    /**
     * 生成 jwt
     */
    @Nullable
    SignInVO generateJwt(Long userId, @Nullable Consumer<JSONObject> consumer, boolean generateRefreshTokenFlag,
        BaseRequestCategoryEnum baseRequestCategoryEnum);

}
