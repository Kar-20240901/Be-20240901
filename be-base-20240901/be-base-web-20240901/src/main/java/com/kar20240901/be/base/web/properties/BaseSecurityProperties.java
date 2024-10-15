package com.kar20240901.be.base.web.properties;

import cn.hutool.crypto.digest.DigestUtil;
import com.kar20240901.be.base.web.model.constant.PropertiesPrefixConstant;
import com.kar20240901.be.base.web.util.PasswordConvertUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = PropertiesPrefixConstant.SECURITY)
public class BaseSecurityProperties {

    @Schema(description = "jwt 密钥前缀")
    private String jwtSecretPre =
        "202e5c4e94c60b8e96cc6c8c2471309c11123a40ef996dd5ab3b180ba9a0dabefe99123edeff526e1d3d264f8dde85eaf6ace1ea236d826fda32080d00f64b47ad0111";

    @Schema(description = "jwt 刷新token密钥前缀")
    private String jwtRefreshTokenSecretPre =
        "202e5c4e94c60b8e96cc6c8c2331309c11123a40ef996dd5ab3b180ba9a123cefe99123edeff526e1d3d264f8dde85eaf6ace1ea235d826fda32080d00f64b47ad0111";

    public static void main(String[] args) {

        generateAdminPassword();

    }

    /**
     * 生成密码 备注：需要和前端一致：先 512，然后再 256
     */
    private static void generateAdminPassword() {

        String password = "karadmin";

        password = DigestUtil.sha256Hex((DigestUtil.sha512Hex(password)));

        System.out.println(password); // 19b5818d7fe851f4510715cbf2193204df3f5b82808a84faaa02634752886ff2

        String convert = PasswordConvertUtil.convert(password, true);

        System.out.println(
            convert); // 51791182cd33494e8c8172469639b6d1/2ec129bd2d5914753d2dcee2fb2fc781bb65ef907e831435d2fec9f0afd7481971be9840167bb839b834c0d8ba536420515d39fcaf4144ba9a86c70c783f7c69

    }

}
