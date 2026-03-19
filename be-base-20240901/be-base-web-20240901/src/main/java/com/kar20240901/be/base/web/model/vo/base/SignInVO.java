package com.kar20240901.be.base.web.model.vo.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInVO {

    @Schema(description = "jwt")
    private String jwt;

    @Schema(description = "jwt过期时间戳")
    private Long jwtExpireTs;

    @Schema(description = "jwtRefreshToken")
    private String jwtRefreshToken;

}
