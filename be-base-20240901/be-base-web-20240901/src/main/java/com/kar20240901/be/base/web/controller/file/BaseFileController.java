package com.kar20240901.be.base.web.controller.file;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.file.BaseFileDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.file.BaseFileCopySelfDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileCreateFolderSelfSelfDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileMoveSelfDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFilePageDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFilePageSelfDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileUpdateSelfDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileUploadChunkDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileUploadChunkPreDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileUploadDTO;
import com.kar20240901.be.base.web.model.vo.base.LongObjectMapVO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.file.BaseFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/base/file")
@Tag(name = "基础-文件-管理")
public class BaseFileController {

    @Resource
    BaseFileService baseService;

    @Operation(summary = "上传文件：公有和私有")
    @PostMapping("/upload")
    public R<Long> upload(BaseFileUploadDTO dto) {
        return R.okData(baseService.upload(dto));
    }

    @Operation(summary = "上传分片文件-准备工作：公有和私有")
    @PostMapping("/upload/chunk/pre")
    public R<Long> uploadChunkPre(BaseFileUploadChunkPreDTO dto) {
        return R.okData(baseService.uploadChunkPre(dto));
    }

    @Operation(summary = "上传分片文件：公有和私有")
    @PostMapping("/upload/chunk")
    public R<Long> uploadChunk(BaseFileUploadChunkDTO dto) {
        return R.okData(baseService.uploadChunk(dto));
    }

    @Operation(summary = "下载文件：私有")
    @PostMapping("/privateDownload")
    public void privateDownload(@RequestBody @Valid NotNullId notNullId, HttpServletResponse response) {
        baseService.privateDownload(notNullId, response);
    }

    @Operation(summary = "批量删除文件：公有和私有，文件和文件夹")
    @PostMapping("/removeByFileIdSet")
    public R<String> removeByFileIdSet(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return R.okMsg(baseService.removeByFileIdSet(notEmptyIdSet, true));
    }

    @Operation(summary = "批量获取：公开文件的 url")
    @PostMapping("/getPublicUrl")
    public R<LongObjectMapVO<String>> getPublicUrl(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return R.okData(baseService.getPublicUrl(notEmptyIdSet));
    }

    @Operation(summary = "分页排序查询")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('baseFile:page')")
    public R<Page<BaseFileDO>> myPage(@RequestBody @Valid BaseFilePageDTO dto) {
        return R.okData(baseService.myPage(dto, true, true));
    }

    @Operation(summary = "分页排序查询-自我")
    @PostMapping("/page/self")
    public R<Page<BaseFileDO>> myPageSelf(@RequestBody @Valid BaseFilePageSelfDTO dto) {
        return R.okData(baseService.myPageSelf(dto));
    }

    @Operation(summary = "查询：树结构")
    @PostMapping("/page/tree")
    @PreAuthorize("hasAuthority('baseFile:page')")
    public R<List<BaseFileDO>> tree(@RequestBody @Valid BaseFilePageDTO dto) {
        return R.okData(baseService.tree(dto));
    }

    @Operation(summary = "查询：树结构-自我")
    @PostMapping("/page/tree/self")
    public R<List<BaseFileDO>> treeSelf(@RequestBody @Valid BaseFilePageSelfDTO dto) {
        return R.okData(baseService.treeSelf(dto));
    }

    @Operation(summary = "创建：文件夹-自我")
    @PostMapping("/createFolder/self")
    public R<String> createFolderSelf(@RequestBody @Valid BaseFileCreateFolderSelfSelfDTO dto) {
        return R.okMsg(baseService.createFolderSelf(dto));
    }

    @Operation(summary = "修改：文件和文件夹-自我")
    @PostMapping("/update/self")
    public R<String> updateSelf(@RequestBody @Valid BaseFileUpdateSelfDTO dto) {
        return R.okMsg(baseService.updateSelf(dto));
    }

    @Operation(summary = "移动：文件和文件夹-自我")
    @PostMapping("/move/self")
    public R<String> moveSelf(@RequestBody @Valid BaseFileMoveSelfDTO dto) {
        return R.okMsg(baseService.moveSelf(dto));
    }

    @Operation(summary = "复制：文件和文件夹-自我")
    @PostMapping("/copy/self")
    public R<String> copySelf(@RequestBody @Valid BaseFileCopySelfDTO dto) {
        return R.okMsg(baseService.copySelf(dto));
    }

}
