package br.edu.fatecsjc.lgnspringapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "marathon")
public class Marathons {
    @Id
    @SequenceGenerator(initialValue = 1, allocationSize = 1, name = "marathon_id_gen", sequenceName = "marathon_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "marathon_id_gen")
    private Long id;

    private String identification;
    private Double weight;
    private Integer score;

    @ManyToMany(mappedBy = "marathons")
    private List<Member> members;
}
