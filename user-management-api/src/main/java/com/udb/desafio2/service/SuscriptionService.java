package com.udb.desafio2.service;

import com.udb.desafio2.entity.User;
import com.udb.desafio2.repository.SuscriptionRepository;
import com.udb.desafio2.dto.SubscriptionRequestDTO;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SuscriptionService {
    private final  SuscriptionRepository suscriptionRepository;
    public SuscriptionService(SuscriptionRepository suscriptionRepository) {this.suscriptionRepository = suscriptionRepository;}

    public List<SubscriptionRequestDTO> findAllByUserId(Long id){}
}
