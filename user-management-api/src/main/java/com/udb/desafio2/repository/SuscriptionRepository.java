package com.udb.desafio2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.udb.desafio2.entity.Subscription;

@Repository
public interface SuscriptionRepository extends JpaRepository<Subscription, Long> {

}
