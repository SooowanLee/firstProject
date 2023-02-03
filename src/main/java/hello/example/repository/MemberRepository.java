package hello.example.repository;

import hello.example.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

//    Member save(Member member);
//
//    Member findById(Long id);
    Member findByEmail(String email);
//
//    List<Member> findAll();
}
