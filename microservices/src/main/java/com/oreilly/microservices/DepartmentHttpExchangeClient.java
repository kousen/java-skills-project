package com.oreilly.microservices;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import java.util.List;

import static com.oreilly.microservices.MicroserviceRecords.*;

public interface DepartmentHttpExchangeClient {

    @GetExchange("/api/departments/{id}")
    MicroserviceDepartment getDepartment(@PathVariable Long id);
    
    @GetExchange("/api/departments")
    List<MicroserviceDepartment> getAllDepartments();
}
