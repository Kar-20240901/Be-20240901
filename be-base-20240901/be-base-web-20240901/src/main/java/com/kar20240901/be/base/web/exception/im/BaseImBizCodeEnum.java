package com.kar20240901.be.base.web.exception.im;

import com.kar20240901.be.base.web.model.interfaces.base.IBizCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 规则： 错误代码格式：300010 300021 解释：前面5位：错误代码，最后一位：0，系统异常，1，业务异常 注意：10001开头的留给 BaseBizCodeEnum类进行配置，建议用 20001开头配置一些公用异常，实际业务从
 * 30001开头，开始使用 备注：可以每个业务的错误代码配置类，使用相同的 错误代码，比如每个业务的错误代码配置类都从 30001开始，原因：因为 R 任何的
 * error方法都会打印服务名，所有就不用关心是哪个服务报出异常，直接根据打印的服务名，找到对应的错误信息即可。
 */
@AllArgsConstructor
@Getter
public enum BaseImBizCodeEnum implements IBizCode {

    MSG_TYPE_NOT_EXIST(400011, "操作失败：消息类型不存在"), //
    SESSION_INFO_NOT_EXIST(400021, "操作失败：会话信息不存在"), //
    SESSION_TYPE_NOT_EXIST(400031, "操作失败：会话类型不存在"), //

    TARGET_NOT_YOUR_FRIEND(400041, "操作失败：对方不是您的好友，无法发送消息"), //
    YOU_NOT_TARGET_FRIEND(400051, "操作失败：您不是对方的好友，无法发送消息"), //
    TARGET_REFUSE_RECEIVE_MSG(400061, "操作失败：对方拒绝接收您的消息，无法发送消息"); //

    ;

    private final int code;
    private final String msg;

}
