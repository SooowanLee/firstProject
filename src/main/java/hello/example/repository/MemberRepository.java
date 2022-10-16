package hello.example.repository;

import hello.example.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findByEmail(String email) {
        return em.find(Member.class, email);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }
}
