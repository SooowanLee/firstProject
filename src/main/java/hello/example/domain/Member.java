package hello.example.domain;

import hello.example.constant.Role;
import hello.example.dto.MemberFormDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity{

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

    @Builder
    public Member(String name, String email, String password, String address, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = role;
    }

    private Member(String name, String email, String password, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = Role.ADMIN;
    }

    public static Member createMember(MemberFormDto memberFormDTO, PasswordEncoder passwordEncoder) {
        return new Member(memberFormDTO.getName(),
                memberFormDTO.getEmail(),
                passwordEncoder.encode(memberFormDTO.getPassword()),
                memberFormDTO.getAddress());
    }
}
