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
public class FormUserDTO {

    @NotBlank
    @Size(max = 10)
    @ApiModelProperty(notes = "Max size 10")
    private String firstName;

    @Size(max = 20)
    @ApiModelProperty(notes = "Max size 20")
    private String lastName;

    @NotBlank
    @Size(max = 10)
    @ApiModelProperty(notes = "Max size 10")
    private String userName;

    @NotBlank
    @Size(max = 10)
    @ApiModelProperty(notes = "Max size 10")
    private String password;

    @NotBlank
    private String role;
}
