package com.kar20240901.be.base.web.configuration.security;

import cn.hutool.json.JSONObject;
import com.kar20240901.be.base.web.model.configuration.IJwtGenerateConfiguration;
import com.kar20240901.be.base.web.model.enums.BaseRequestCategoryEnum;
import com.kar20240901.be.base.web.model.vo.SignInVO;
import com.kar20240901.be.base.web.util.BaseJwtUtil;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseJwtConfiguration implements IJwtGenerateConfiguration {

    @Override
    public SignInVO generateJwt(Long userId, @Nullable Consumer<JSONObject> consumer, boolean generateRefreshTokenFlag,
        BaseRequestCategoryEnum baseRequestCategoryEnum) {

        return BaseJwtUtil.generateJwt(userId, consumer, generateRefreshTokenFlag, baseRequestCategoryEnum, null);

    }

}
