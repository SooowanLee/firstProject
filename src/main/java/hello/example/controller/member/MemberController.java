package hello.example.controller.member;

import hello.example.domain.member.Member;
import hello.example.dto.MemberFormDTO;
import hello.example.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder; //springSecurity 비밀번호 암호화

    @GetMapping("/members/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDTO", new MemberFormDTO());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Validated MemberFormDTO form, BindingResult result) {
        log.info("MemberController create");
        log.info("result={}", result);

        // 에러가 있으면 회원가입화면으로 보낸다.
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        // ControllerAdvice로 따로 뺴기
        try {
            //MemberFormDTO -> Member
            Member member = Member.createMember(form, passwordEncoder);
            memberService.join(member);
            log.info("members/new success join");
        } catch (IllegalStateException e) {
            log.info("error", e);
            result.rejectValue("email", "DuplicatedEmail", e.getMessage());
            return "members/createMemberForm";
        }

        return "redirect:/";
    }
}
