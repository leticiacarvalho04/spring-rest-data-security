package br.edu.fatecsjc.lgnspringapi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "group")
public class Group {
    @Id
    @SequenceGenerator(initialValue = 1, allocationSize = 1, name = "group_id_gen", sequenceName = "group_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_id_gen")
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Member> members;
}
