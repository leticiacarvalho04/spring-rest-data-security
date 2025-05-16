package br.edu.fatecsjc.lgnspringapi.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "member")
public class Member {
    @Id
    @SequenceGenerator(initialValue = 1, allocationSize = 1, name = "member_id_gen", sequenceName = "member_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_id_gen")
    private Long id;

    private String name;
    private String email;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "member_marathon",
        joinColumns = @JoinColumn(name = "member_id"),
        inverseJoinColumns = @JoinColumn(name = "marathon_id")
    )
    private List<Marathons> marathons;
}