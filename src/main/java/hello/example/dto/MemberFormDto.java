package hello.example.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter @Setter
public class MemberFormDto {

    @NotBlank(message = "필수 정보입니다.")
    private String name;

    @NotEmpty(message = "필수 정보입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotEmpty(message = "필수 정보입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;

    @NotEmpty(message = "필수 정보입니다.")
    private String address;
}
