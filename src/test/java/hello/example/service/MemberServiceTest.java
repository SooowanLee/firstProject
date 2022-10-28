package hello.example.service;

import hello.example.domain.member.Member;
import hello.example.dto.MemberFormDTO;
import hello.example.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember() {
        MemberFormDTO memberFormDTO = new MemberFormDTO();
            memberFormDTO.setName("test");
            memberFormDTO.setEmail("test@email.com");
            memberFormDTO.setPassword("!@#$qweQWE");
            memberFormDTO.setAddress("testAddress");
        return Member.createMember(memberFormDTO, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
    void saveMemberTest() {
        Member member = createMember();

        Long savedMember = memberService.join(member);

        assertThat(memberRepository.findOne(savedMember)).isEqualTo(member);
    }

    @Test
    public void duplicatedMemberTest() throws Exception {
        //given
        Member member = createMember();
        Member member2 = createMember();

        memberService.join(member);

        // when
        // 다 똑같은거다.
        assertThatThrownBy(() -> memberService.join(member2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 가입된 회원입니다.");

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> memberService.join(member2))
                .withMessage("이미 가입된 회원입니다.");

        assertThatIllegalStateException()
                .isThrownBy(() -> memberService.join(member2))
                .withMessage("이미 가입된 회원입니다.");
    }
}