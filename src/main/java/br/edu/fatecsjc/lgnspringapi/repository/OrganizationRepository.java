package br.edu.fatecsjc.lgnspringapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.fatecsjc.lgnspringapi.entity.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {}
