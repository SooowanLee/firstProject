package hello.example.controller;

import hello.example.domain.Member;
import hello.example.service.MemberService;
import hello.example.web.form.MemberForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members")
    public String findAllMembers(@ModelAttribute MemberForm form) {
        log.info("findAllMembers form={}", form);

        Member member = new Member("이수완", 29);
        memberService.join(member);

        return "members/new";
    }
}
