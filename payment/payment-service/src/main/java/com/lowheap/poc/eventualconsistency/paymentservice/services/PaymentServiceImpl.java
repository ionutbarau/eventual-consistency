package com.lowheap.poc.eventualconsistency.paymentservice.services;

import com.lowheap.poc.eventualconsistency.lib.common.annotations.CompensationFor;
import com.lowheap.poc.eventualconsistency.lib.common.annotations.Pivot;
import com.lowheap.poc.eventualconsistency.lib.common.exceptions.Messages;
import com.lowheap.poc.eventualconsistency.paymentservice.client.payment.dto.PaymentDto;
import com.lowheap.poc.eventualconsistency.paymentservice.client.payment.events.PaymentAuthorizationFailedEvent;
import com.lowheap.poc.eventualconsistency.paymentservice.client.payment.events.PaymentAuthorizationFailedEventPayload;
import com.lowheap.poc.eventualconsistency.paymentservice.client.payment.events.PaymentAuthorizedEvent;
import com.lowheap.poc.eventualconsistency.paymentservice.client.payment.events.PaymentAuthorizedEventPayload;
import com.lowheap.poc.eventualconsistency.paymentservice.model.*;
import com.lowheap.poc.eventualconsistency.paymentservice.repositories.PaymentEventsRepository;
import com.lowheap.poc.eventualconsistency.paymentservice.repositories.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.lowheap.poc.eventualconsistency.customerservice.client.events.CustomerEventType.CUSTOMER_PENDING_REGISTRATION_EVENT;
import static com.lowheap.poc.eventualconsistency.lib.common.constants.AggregateType.PAYMENT;
import static com.lowheap.poc.eventualconsistency.lib.common.constants.SagaConstants.CUSTOMER_REGISTRATION_SAGA;
import static com.lowheap.poc.eventualconsistency.paymentservice.client.payment.events.PaymentEventType.PAYMENT_AUTHORIZED_EVENT;
import static com.lowheap.poc.eventualconsistency.paymentservice.client.payment.events.PaymentEventType.PAYMENT_NOT_AUTHORIZED_EVENT;

@Service
@Transactional
@Slf4j
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final PaymentEventsRepository paymentEventsRepository;
    private final ModelMapper modelMapper;

    @Value("${authorization.fee}")
    private Double authorizationFee;
    @Value("${company.name}")
    private String companyName;
    @Value("${company.bankAccount}")
    private String companyBankAccount;
    @Value("${company.bankName}")
    private String companyBankName;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentEventsRepository paymentEventsRepository,ModelMapper modelMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentEventsRepository = paymentEventsRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Pivot(sagas = {CUSTOMER_REGISTRATION_SAGA})
    public Payment authorizeCustomer(Customer customer) {
        Payment payment = buildPaymentFromCustomer(customer, true);
        if (payment.getPayer().getCard().getBalance() < authorizationFee) {
            //do compensation
            return failCustomerAuthorization(customer, Messages.NO_BALANCE_LEFT);
        }
        payment = paymentRepository.save(payment);
        PaymentAuthorizedEventPayload payload = PaymentAuthorizedEventPayload.builder().customerId(customer.getId()).payment(modelMapper.map(payment, PaymentDto.class)).build();
        //save the new event to be sent
        PaymentAuthorizedEvent paymentAuthorizedEvent = PaymentAuthorizedEvent.builder().payload(payload).type(PAYMENT_AUTHORIZED_EVENT)
                .aggregateId(payment.getId()).aggregateType(PAYMENT).build();
        paymentEventsRepository.save(paymentAuthorizedEvent);
        log.info("Customer payment authorized");
        return payment;
    }

    @Override
    @CompensationFor(sagas = {CUSTOMER_REGISTRATION_SAGA}, compensatableEvents = {CUSTOMER_PENDING_REGISTRATION_EVENT})
    public Payment failCustomerAuthorization(Customer customer, String reason) {
        Payment payment = buildPaymentFromCustomer(customer, false);
        payment = paymentRepository.save(payment);
        PaymentAuthorizationFailedEvent paymentAuthorizationFailedEvent = PaymentAuthorizationFailedEvent.builder()
                .payload(PaymentAuthorizationFailedEventPayload.builder().customerId(customer.getId()).reason(reason).payment(modelMapper.map(payment, PaymentDto.class)).build())
                .type(PAYMENT_NOT_AUTHORIZED_EVENT).aggregateId(payment.getId()).aggregateType(PAYMENT).build();
        paymentEventsRepository.save(paymentAuthorizationFailedEvent);
        log.info("Customer payment not authorized");
        return payment;
    }


    private Payment buildPaymentFromCustomer(Customer customer, boolean authorized) {
        String name = new StringBuilder(customer.getFirstName()).append(" ").append(customer.getLastName()).toString();
        Card card = modelMapper.map(customer.getCard(), Card.class);
        Payer payer = Payer.builder().name(name).card(card).build();
        Payee company = Payee.builder().name(companyName).bankAccount(companyBankAccount).bankName(companyBankName).build();
        return Payment.builder().payer(payer).payee(company).amount(authorizationFee).authorized(authorized).build();
    }
}
