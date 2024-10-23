package com.kar20240901.be.base.web.model.configuration.file;

import java.util.Set;

public interface IBaseFileRemove {

    /**
     * 当文件进行移除时
     */
    void handle(Set<Long> removeFileIdSet);

}
