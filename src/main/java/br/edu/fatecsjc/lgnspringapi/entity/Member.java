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
@Table(name = "members")
public class Member {
    @Id
    @SequenceGenerator(initialValue = 1, allocationSize = 1, name = "membersidgen", sequenceName = "members_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "membersidgen")
    private Long id;
    private String name;
    private Integer age;
    @ManyToOne
    @JoinColumn(name="group_id", nullable=false)
    private Group group;
    
    @ManyToMany
    @JoinTable(
      name = "member_marathon", 
      joinColumns = @JoinColumn(name = "member_id"), 
      inverseJoinColumns = @JoinColumn(name = "marathon_id"))
    private List<Marathons> marathons;
}
