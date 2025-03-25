package br.edu.fatecsjc.lgnspringapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.fatecsjc.lgnspringapi.entity.Marathons;
import br.edu.fatecsjc.lgnspringapi.entity.Member;

@Repository
public interface MarathonsRepository extends JpaRepository<Marathons, Long> {
	void deleteByMembers(Member member);
}
