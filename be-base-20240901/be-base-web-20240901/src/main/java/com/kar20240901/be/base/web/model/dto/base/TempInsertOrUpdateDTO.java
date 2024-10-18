package com.kar20240901.be.base.web.model.dto.base;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TempInsertOrUpdateDTO {

    /**
     * 允许为 null
     */
    @Min(1)
    @Schema(description = "主键 id")
    private Long id;

}
