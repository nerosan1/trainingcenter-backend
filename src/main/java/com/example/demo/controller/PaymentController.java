package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.payment.CreatePaymentRequest;
import com.example.demo.dto.payment.PaymentResponse;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<?> createPayment(@Valid @RequestBody CreatePaymentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Integer actorId = userDetails != null ? userDetails.getUserId() : null;
            PaymentResponse response = paymentService.createPayment(request, actorId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPayments() {
        try {
            List<PaymentResponse> payments = paymentService.getAllPayments();
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable Integer id) {
        try {
            PaymentResponse response = paymentService.getPaymentById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/enrollment/{enrollmentId}")
    public ResponseEntity<?> getPaymentByEnrollment(@PathVariable Integer enrollmentId) {
        try {
            PaymentResponse response = paymentService.getPaymentByEnrollment(enrollmentId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<?> getPendingPayments() {
        try {
            List<PaymentResponse> payments = paymentService.getPendingPayments();
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<?> confirmPayment(@PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Integer actorId = userDetails != null ? userDetails.getUserId() : null;
            PaymentResponse response = paymentService.confirmPayment(id, actorId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelPayment(@PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Integer actorId = userDetails != null ? userDetails.getUserId() : null;
            PaymentResponse response = paymentService.cancelPayment(id, actorId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/{id}/fail")
    public ResponseEntity<?> failPayment(@PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Integer actorId = userDetails != null ? userDetails.getUserId() : null;
            PaymentResponse response = paymentService.failPayment(id, actorId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}
