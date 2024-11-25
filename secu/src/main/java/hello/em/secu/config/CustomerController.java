package hello.em.secu.config;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import hello.em.secu.model.Customer;

@RestController
@EnableMethodSecurity
public class CustomerController {
    private final List<Customer> customers = List.of(
            Customer.builder().id("001").name("Tran Dong Phuong").email("bikumnotme23@gmail.com")
                    .phoneNumber("0123456789").build(),
            Customer.builder().id("002").name("Le Thi Bao Phuong").email("phuongtdp2301@gmail.com")
                    .phoneNumber("0123456789").build()
    );

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hi, Welcome back!");
    }

    @GetMapping("/customer/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Customer>> getCustomerList() {
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/customer/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCustomerById(@PathVariable("id") String id) {
        List<Customer> filteredCustomers = customers.stream()
                .filter(customer -> customer.getId().equals(id))
                .toList();

        if (filteredCustomers.isEmpty()) {
            return ResponseEntity.status(404).body("Customer not found with id: " + id);
        }
        return ResponseEntity.ok(filteredCustomers.get(0));
    }
}
