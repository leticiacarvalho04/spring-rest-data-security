package br.edu.fatecsjc.lgnspringapi.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "organization")
public class Organization {
    @Id
    @SequenceGenerator(initialValue = 1, allocationSize = 1, name = "organization_id_gen", sequenceName = "organization_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "organization_id_gen")
    private Long id;

    private String name;
    private String number;
    private String street;
    private String neighborhood;
    private String cep;
    private String municipality;
    private String state;
    private String institutionName;
    private String hostCountry;

    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Group> groups = new ArrayList<>();
}