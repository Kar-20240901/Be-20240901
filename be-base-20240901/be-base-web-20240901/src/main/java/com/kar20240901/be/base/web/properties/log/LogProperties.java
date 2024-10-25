package com.kar20240901.be.base.web.properties.log;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class LogProperties {

    /**
     * 为空时，则是正常的日志状态，并且不会打印 be开头的日志，不为空时，则只打印集合里面日志
     */
    private Set<String> logTopicSet = new HashSet<>();

    /**
     * 不打印的正常日志
     */
    private Set<String> notLogTopicSet = new HashSet<>();

}
