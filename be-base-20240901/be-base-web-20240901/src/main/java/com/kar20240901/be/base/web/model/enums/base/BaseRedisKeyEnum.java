package com.kar20240901.be.base.web.model.enums.base;

import com.kar20240901.be.base.web.model.interfaces.base.IRedisKey;

/**
 * redis中 key的枚举类 备注：如果是 redisson的锁 key，一定要备注：锁什么，例如：锁【用户主键 id】 备注：【PRE_】开头，表示 key后面还要跟字符串 备注：【_CACHE】结尾，表示 key后面不用跟字符串
 */
public enum BaseRedisKeyEnum implements IRedisKey {

    // 【PRE_】开头 ↓
    PRE_FILE_TRANSFER, // 传输锁，锁：【传输id】，目的：防止重复合并

    PRE_FILE_TRANSFER_CHUNK_NUM, // 传输分片编号锁，锁：【传输id + 传输分片编号】，目的：防止重复上传

    PRE_USER_WALLET_WITHDRAW_LOG, // 用户钱包-提现记录锁，锁：【提现记录主键 id】

    PRE_USER_WALLET, // 用户钱包锁，锁：【用户钱包主键 id】

    PRE_PAY, // 支付锁，锁：【支付主键 id】

    PRE_OTHER_APP_TYPE_AND_APP_ID, // 第三方应用，类型code 和 appid锁，目的：同一个类型下的 appId不能重复，锁：【类型code 和 appid】

    PRE_SIGN_CONFIGURATION, // 用户登录注册相关配置锁

    PRE_BASE_WX_WORK_SYNC_MSG, // 企业微信，获取消息锁

    PRE_BASE_WX_QR_CODE_BIND, // 微信扫码绑定时，生成的，二维码 id，备注：只有扫描了二维码之后，才会放数据到 redis里面

    PRE_WEB_SOCKET_CODE, // webSocket连接锁，锁：【随机码】

    PRE_USER_MANAGE_SIGN_IN_FLAG, // 用户是否允许登录后台，后面跟：用户主键 id

    PRE_WX_APP_ID, // 微信 appId（应用）：锁：【微信 appId】
    PRE_WX_OPEN_ID, // 微信 openId（用户）：锁：【微信 openId】，备注：一般锁：微信 openId，不锁：微信 appId
    PRE_WX_UNION_ID, // 微信 unionId（平台）：锁：【微信 unionId】

    PRE_PHONE, // 手机号码：锁：【手机号码】
    PRE_EMAIL, // 邮箱：锁：【邮箱】
    PRE_USER_NAME, // 用户名：锁：【用户名】

    PRE_TOO_MANY_PASSWORD_ERROR, // 密码错误次数太多：锁【用户主键 id】
    PRE_PASSWORD_ERROR_COUNT, // 密码错误总数：锁【用户主键 id】

    // 【_CACHE】结尾 ↓
    GOOGLE_ACCESS_TOKEN_CACHE, // google接口调用凭据

    WX_WORK_ACCESS_TOKEN_CACHE, // 企业微信全局唯一后台接口调用凭据

    WX_OFFICIAL_ACCESS_TOKEN_CACHE, // 微信公众号全局唯一后台接口调用凭据

    BAI_DU_ACCESS_TOKEN_CACHE, // 百度全局唯一后台接口调用凭据

    // 其他 ↓

    ;

}
