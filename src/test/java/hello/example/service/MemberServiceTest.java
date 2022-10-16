package hello.example.service;

import hello.example.controller.MemberFormDTO;
import hello.example.domain.Member;
import hello.example.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    public Member createMember() {
        MemberFormDTO memberFormDTO = MemberFormDTO.builder()
                .name("테스트이름")
                .email("test@email.com")
                .password("!Qwe123")
                .address("테스트주소입니다.")
                .build();
        return Member.createMember(memberFormDTO);
    }

    @Test
    @DisplayName("회원가입 테스트")
//    @Commit
    void saveMemberTest() {
        Member member = createMember();

        Long memberId = memberService.saveMember(member);
        Member findMember = memberRepository.findOne(memberId);
        assertThat(findMember).isEqualTo(member);
    }
}