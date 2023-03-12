package com.market.aaa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;



@Builder
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(indexes = @Index(columnList = "refreshToken", name = "refresh_token_index"))
public class Members {

    @Id
    @Column(updatable = false, unique = true, nullable = false)
    private String memberId;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Column(nullable = false)
    private String company;

    @Column(unique = true)
    private String refreshToken;

    @Column(nullable = false)
    private int loginFailCount;

}
