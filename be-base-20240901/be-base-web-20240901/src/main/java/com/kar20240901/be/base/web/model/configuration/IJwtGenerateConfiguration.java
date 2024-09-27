package com.kar20240901.be.base.web.model.configuration;

import cn.hutool.json.JSONObject;
import com.kar20240901.be.base.web.model.enums.BaseRequestCategoryEnum;
import com.kar20240901.be.base.web.model.vo.SignInVO;
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
