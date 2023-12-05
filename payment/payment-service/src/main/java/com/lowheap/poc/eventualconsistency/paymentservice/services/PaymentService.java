package com.lowheap.poc.eventualconsistency.paymentservice.services;


import com.lowheap.poc.eventualconsistency.customerservice.client.dto.CustomerDto;
import com.lowheap.poc.eventualconsistency.paymentservice.model.Customer;
import com.lowheap.poc.eventualconsistency.paymentservice.model.Payment;

public interface PaymentService {

    Payment authorizeCustomer(Customer customer);

    Payment failCustomerAuthorization(Customer customer, String reason);
}
