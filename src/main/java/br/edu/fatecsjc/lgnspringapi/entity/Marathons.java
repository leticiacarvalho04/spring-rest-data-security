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
@Table(name = "marathons")
public class Marathons {
	@Id
    @SequenceGenerator(initialValue = 1, allocationSize = 1, name = "groupsidgen", sequenceName = "groups_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "groupsidgen")
	private Long id;
	private String identification;
	private Double weight;
	private Integer score;
	
	@ManyToMany(mappedBy = "marathons", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Member> members;
}
