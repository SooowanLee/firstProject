package hello.example.domain.member;

import hello.example.constant.Role;
import hello.example.domain.BaseEntity;
import hello.example.dto.MemberFormDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Member(String name, String email, String password, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = Role.USER;
    }

    public static Member createMember(MemberFormDTO memberFormDTO, PasswordEncoder passwordEncoder) {
        return new Member(memberFormDTO.getName(),
                memberFormDTO.getEmail(),
                passwordEncoder.encode(memberFormDTO.getPassword()),
                memberFormDTO.getAddress());
    }
}
