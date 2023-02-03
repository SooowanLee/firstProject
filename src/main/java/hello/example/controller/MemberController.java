package hello.example.controller;

import hello.example.domain.Member;
import hello.example.dto.MemberFormDto;
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
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder; //springSecurity 비밀번호 암호화

    @GetMapping("/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "members/memberForm";
    }

    /**
     * 1. 형식에 맞게 작성했는지 확인
     * 2. 검증을 통과하면 회원을 저장
     */
    @PostMapping("/new")
    public String newMember(@Validated MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
        log.info("MemberController create");
        log.info("result={}", bindingResult);

        if (bindingResult.hasErrors()) {
            return "/members/memberForm";
        }

        try {

            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);

            log.info("success saveMember");
        } catch (IllegalStateException e) {

            model.addAttribute("errorMessage", e.getMessage());

            log.info("e.message = {}", e.getMessage());
            return "/members/memberForm";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginMember() {
        return "/members/memberLoginForm";
    }

    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/members/memberLoginForm";
    }
}
