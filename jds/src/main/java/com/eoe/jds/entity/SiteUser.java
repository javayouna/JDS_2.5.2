package com.eoe.jds.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true) //유일한 값이 들어감
    private String username;

    private String password;

    @Column(unique = true) //유일한 값이 들어감
    private String email;

    private LocalDateTime createDate;
}


