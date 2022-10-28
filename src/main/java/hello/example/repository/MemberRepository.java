package hello.example.repository;

import hello.example.domain.member.Member;

import java.util.List;

public interface MemberRepository {

    void save(Member member);

    Member findOne(Long id);
    List<Member> findByEmail(String email);

    List<Member> findAll();
}
