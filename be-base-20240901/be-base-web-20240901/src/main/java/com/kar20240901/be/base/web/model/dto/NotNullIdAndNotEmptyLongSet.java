package com.kar20240901.be.base.web.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotNullIdAndNotEmptyLongSet extends NotNullId {

    public NotNullIdAndNotEmptyLongSet(Long id, Set<Long> valueSet) {

        super(id);
        this.valueSet = valueSet;

    }

    @NotEmpty
    @Schema(description = "值 set")
    private Set<Long> valueSet;

}
