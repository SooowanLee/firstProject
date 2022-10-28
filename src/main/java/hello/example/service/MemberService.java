package hello.example.service;

import hello.example.domain.member.Member;
import hello.example.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 이메일로 회원을 조회
     * 만약 가입된 회원이면 IllegalStateException 생성
     * 아니면 회원가입
     */

    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);//중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByEmail(member.getEmail());

        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }
}

