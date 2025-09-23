package com.udb.desafio2.service;

import com.udb.desafio2.dto.SubscriptionRequestDTO;
import com.udb.desafio2.entity.User;
import com.udb.desafio2.entity.Subscription;
import com.udb.desafio2.repository.SubscriptionRepository;
import com.udb.desafio2.repository.UserRepository;
import com.udb.desafio2.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }

    public Subscription createSubscription(SubscriptionRequestDTO subscriptionRequest) {
        // Validar que el usuario existe
        User user = userRepository.findById(subscriptionRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + subscriptionRequest.getUserId()));

        // Validar fechas
        validateSubscriptionDates(subscriptionRequest);

        // Crear la suscripción
        Subscription subscription = new Subscription();
        subscription.setType(subscriptionRequest.getType());
        subscription.setStartDate(subscriptionRequest.getStartDate());
        subscription.setEndDate(subscriptionRequest.getEndDate());
        subscription.setUser(user);

        return subscriptionRepository.save(subscription);
    }

    @Transactional(readOnly = true)
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Subscription getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Subscription> getSubscriptionsByUser(Long userId) {
        // Validar que el usuario existe
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        return subscriptionRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Subscription> getActiveSubscriptions() {
        return subscriptionRepository.findActiveSubscriptions(LocalDate.now());
    }

    @Transactional(readOnly = true)
    public List<Subscription> getActiveSubscriptionsByUser(Long userId) {
        // Validar que el usuario existe
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        return subscriptionRepository.findActiveSubscriptionsByUser(userId, LocalDate.now());
    }

    public void deleteSubscription(Long id) {
        if (!subscriptionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Subscription not found with ID: " + id);
        }

        subscriptionRepository.deleteById(id);
    }

    private void validateSubscriptionDates(SubscriptionRequestDTO subscriptionRequest) {
        if (subscriptionRequest.getEndDate().isBefore(subscriptionRequest.getStartDate())) {
            throw new RuntimeException("End date must be after start date");
        }
    }
}