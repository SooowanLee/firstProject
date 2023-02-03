package hello.example.service;

import hello.example.domain.Member;
import hello.example.dto.MemberFormDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
//@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember() {
        MemberFormDto memberFormDTO = new MemberFormDto();
            memberFormDTO.setName("test");
            memberFormDTO.setEmail("test@email.com");
            memberFormDTO.setPassword("!@#$qweQWE");
            memberFormDTO.setAddress("testAddress");
        return Member.createMember(memberFormDTO, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
    @Rollback(value = false)
    void saveMemberTest() {
        Member member = createMember();

        Member savedMember = memberService.saveMember(member);

        assertThat(savedMember).isEqualTo(member);
    }

    @Test
    @DisplayName("종복 회원 가입 테스트")
    public void duplicatedMemberTest() throws Exception {
        //given
        Member member = createMember();
        Member member2 = createMember();

        memberService.saveMember(member);

        // when
        // 다 똑같은거다.
        assertThatThrownBy(() -> memberService.saveMember(member2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 가입된 회원입니다.");

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> memberService.saveMember(member2))
                .withMessage("이미 가입된 회원입니다.");

        assertThatIllegalStateException()
                .isThrownBy(() -> memberService.saveMember(member2))
                .withMessage("이미 가입된 회원입니다.");
    }
}