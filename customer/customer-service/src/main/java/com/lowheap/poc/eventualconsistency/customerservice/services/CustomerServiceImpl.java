package com.lowheap.poc.eventualconsistency.customerservice.services;

import com.lowheap.poc.eventualconsistency.customerservice.client.dto.CustomerDto;
import com.lowheap.poc.eventualconsistency.customerservice.client.events.*;
import com.lowheap.poc.eventualconsistency.customerservice.model.Customer;
import com.lowheap.poc.eventualconsistency.customerservice.model.RegistrationStatus;
import com.lowheap.poc.eventualconsistency.customerservice.repositories.CustomerEventsRepository;
import com.lowheap.poc.eventualconsistency.customerservice.repositories.CustomerRepository;
import com.lowheap.poc.eventualconsistency.lib.common.annotations.*;
import com.lowheap.poc.eventualconsistency.lib.common.exceptions.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.lowheap.poc.eventualconsistency.customerservice.client.events.CustomerEventType.*;
import static com.lowheap.poc.eventualconsistency.lib.common.constants.AggregateType.CUSTOMER;
import static com.lowheap.poc.eventualconsistency.lib.common.constants.SagaConstants.CUSTOMER_REGISTRATION_SAGA;
import static com.lowheap.poc.eventualconsistency.lib.common.exceptions.Messages.CUSTOMER_NOT_FOUND;

@Service
@Transactional
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerEventsRepository customerEventsRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerEventsRepository customerEventsRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.customerEventsRepository = customerEventsRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Compensable(sagas = {CUSTOMER_REGISTRATION_SAGA})
    @SagaStart(sagas = {CUSTOMER_REGISTRATION_SAGA})
    public Customer createPendingCustomerRegistration(Customer customer) {
        customer.setStatus(RegistrationStatus.REGISTRATION_PENDING);
        customer = customerRepository.save(customer);
        CustomerPendingRegistrationEventPayload payload = CustomerPendingRegistrationEventPayload.builder().customerDto(modelMapper.map(customer, CustomerDto.class)).build();
        CustomerPendingRegistrationEvent customerPendingRegistrationEvent = CustomerPendingRegistrationEvent.builder().aggregateId(customer.getId()).aggregateType(CUSTOMER).type(CUSTOMER_PENDING_REGISTRATION_EVENT).payload(payload).build();
        customerEventsRepository.save(customerPendingRegistrationEvent);
        return customer;
    }

    @Override
    @Retriable(sagas = {CUSTOMER_REGISTRATION_SAGA})
    @SagaEnd(sagas = {CUSTOMER_REGISTRATION_SAGA})
    public void approveRegistration(String customerId) {
        log.info("Registration success");
        updateRegistrationStatus(customerId, RegistrationStatus.REGISTRATION_SUCCESS, null);
    }

    @Override
    @CompensationFor(sagas = {CUSTOMER_REGISTRATION_SAGA}, compensatableOperations = {"createPendingCustomerRegistration"})
    @Retriable(sagas = {CUSTOMER_REGISTRATION_SAGA})
    @SagaEnd(sagas = {CUSTOMER_REGISTRATION_SAGA})
    public void rejectRegistration(String customerId, String reason) {
        log.info("Registration failed");
        updateRegistrationStatus(customerId, RegistrationStatus.REGISTRATION_FAILED, reason);
    }

    @Override
    public Customer findById(String id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isEmpty()){
            throw new BusinessException(CUSTOMER_NOT_FOUND);
        }
        return customer.get();
    }

    private void updateRegistrationStatus(String customerId, RegistrationStatus status, String failReason){
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if(customerOptional.isEmpty()){
            //the message should be put on dlq
            throw new IllegalArgumentException(CUSTOMER_NOT_FOUND);
        }
        Customer customer = customerOptional.get();
        customer.setStatus(status);
        customer = customerRepository.save(customer);
        CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);
        if(status == RegistrationStatus.REGISTRATION_SUCCESS) {
            CustomerRegisteredEventPayload payload = CustomerRegisteredEventPayload.builder().customerDto(customerDto).build();
            CustomerRegisteredEvent customerRegisteredEvent = CustomerRegisteredEvent.builder().aggregateId(customer.getId()).aggregateType(CUSTOMER).type(CUSTOMER_REGISTERED_EVENT).payload(payload).build();
            customerEventsRepository.save(customerRegisteredEvent);
        }else{
            CustomerNotRegisteredEventPayload payload = CustomerNotRegisteredEventPayload.builder().reason(failReason).customerDto(customerDto).build();
            CustomerNotRegisteredEvent customerNotRegisteredEvent = CustomerNotRegisteredEvent.builder().aggregateId(customer.getId()).aggregateType(CUSTOMER).type(CUSTOMER_NOT_REGISTERED_EVENT).payload(payload).build();
            customerEventsRepository.save(customerNotRegisteredEvent);
        }
    }
}
