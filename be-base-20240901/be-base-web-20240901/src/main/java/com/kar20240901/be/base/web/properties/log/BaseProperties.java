package com.kar20240901.be.base.web.properties.log;

import com.kar20240901.be.base.web.model.constant.base.BaseConstant;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

/**
 * 只会控制：控制台的输出，不会控制：日志文件的输出
 */
@Data
public class BaseProperties {

    /**
     * 为空时，则是正常的日志状态，并且不会打印 be开头的日志，不为空时，则只打印集合里面日志
     */
    private Set<String> logTopicSet = new HashSet<>();

    /**
     * 不打印的正常日志，即不打印的不以 be开头的日志
     */
    private Set<String> notLogTopicSet = new HashSet<>();

    /**
     * 生产环境不处理的 kafkaTopic
     */
    private Set<String> prodNotHandleKafkaTopicSet = new HashSet<>();

    /**
     * 非生产环境不处理的 kafkaTopic
     */
    private Set<String> devNotHandleKafkaTopicSet = new HashSet<>();

    /**
     * 每个分片文件的大小
     */
    private Integer fileChunkSize = BaseConstant.FILE_CHUNK_SIZE;

}
