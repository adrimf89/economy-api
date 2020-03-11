package com.adri.economy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormRoleDTO {

    @NotBlank
    @Size(max = 10)
    @ApiModelProperty(notes = "Max size 10")
    private String roleName;
}
