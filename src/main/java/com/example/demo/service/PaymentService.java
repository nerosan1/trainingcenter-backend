package com.example.demo.service;

import com.example.demo.dto.payment.CreatePaymentRequest;
import com.example.demo.dto.payment.PaymentResponse;
import com.example.demo.entity.Enrollment;
import com.example.demo.entity.EnrollmentStatus;
import com.example.demo.entity.Payment;
import com.example.demo.entity.PaymentStatus;
import com.example.demo.repository.AuditLogRepository;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;

    public PaymentService(PaymentRepository paymentRepository, EnrollmentRepository enrollmentRepository,
            UserRepository userRepository, AuditLogRepository auditLogRepository) {
        this.paymentRepository = paymentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request, Integer actorId) {
        Enrollment enrollment = enrollmentRepository.findById(request.getEnrollmentId())
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        if (enrollment.getStatus() != EnrollmentStatus.PENDING) {
            throw new RuntimeException("Enrollment must be in PENDING status");
        }

        // Check if payment already exists
        var existingPayment = paymentRepository.findByEnrollmentEnrollmentId(request.getEnrollmentId());
        if (existingPayment.isPresent()) {
            throw new RuntimeException("Payment already exists for this enrollment");
        }

        Payment payment = new Payment();
        payment.setEnrollment(enrollment);
        payment.setAmount(request.getAmount());
        payment.setMethod(request.getMethod());
        payment.setStatus(PaymentStatus.WAITING_CONFIRM);

        Payment saved = paymentRepository.save(payment);
        logAction(actorId, "CREATE_PAYMENT", "Payment", saved.getPaymentId());

        return toResponse(saved);
    }

    public PaymentResponse getPaymentById(Integer id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return toResponse(payment);
    }

    public PaymentResponse getPaymentByEnrollment(Integer enrollmentId) {
        Payment payment = paymentRepository.findByEnrollmentEnrollmentId(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return toResponse(payment);
    }

    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<PaymentResponse> getPendingPayments() {
        return paymentRepository.findAll().stream()
                .filter(p -> p.getStatus() == PaymentStatus.WAITING_CONFIRM)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PaymentResponse confirmPayment(Integer paymentId, Integer actorId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.WAITING_CONFIRM) {
            throw new RuntimeException("Payment is not in WAITING_CONFIRM status");
        }

        payment.setStatus(PaymentStatus.PAID);
        payment.setPaidAt(Instant.now());

        // Auto-approve enrollment
        Enrollment enrollment = payment.getEnrollment();
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollmentRepository.save(enrollment);

        Payment saved = paymentRepository.save(payment);
        logAction(actorId, "CONFIRM_PAYMENT", "Payment", saved.getPaymentId());

        return toResponse(saved);
    }

    @Transactional
    public PaymentResponse cancelPayment(Integer paymentId, Integer actorId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(PaymentStatus.CANCELLED);

        Payment saved = paymentRepository.save(payment);
        logAction(actorId, "CANCEL_PAYMENT", "Payment", saved.getPaymentId());

        return toResponse(saved);
    }

    @Transactional
    public PaymentResponse failPayment(Integer paymentId, Integer actorId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(PaymentStatus.FAILED);

        Payment saved = paymentRepository.save(payment);
        logAction(actorId, "FAIL_PAYMENT", "Payment", saved.getPaymentId());

        return toResponse(saved);
    }

    private void logAction(Integer userId, String action, String entity, Integer entityId) {
        if (userId != null) {
            try {
                var actor = userRepository.findById(userId).orElse(null);
                if (actor != null) {
                    var log = new com.example.demo.entity.AuditLog();
                    log.setUser(actor);
                    log.setAction(action);
                    log.setEntity(entity);
                    log.setEntityId(entityId);
                    auditLogRepository.save(log);
                }
            } catch (Exception ignored) {
            }
        }
    }

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getPaymentId(),
                payment.getEnrollment().getEnrollmentId(),
                payment.getAmount(),
                payment.getMethod().name(),
                payment.getStatus().name(),
                payment.getPaidAt());
    }
}
