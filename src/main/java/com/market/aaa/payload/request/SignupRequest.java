package com.market.aaa.payload.request;

import com.market.aaa.config.annotation.EachEnum;
import com.market.aaa.entity.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class SignupRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String memberId;

    @NotBlank
    @Size(min = 8, max = 40)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}",
            message = "패스워드는 최소 8자, 최대 40자, 특수문자, 숫자, 대문자, 소문자가 하나 이상 필요합니다.")
    private String password;

    @NotBlank
    private String company;

    @NotEmpty
    @EachEnum(enumClass = Role.class)
    private List<String> roles = new ArrayList<>();
}
