package com.timmy.employee_management_system.Repository;

import com.timmy.employee_management_system.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository  extends JpaRepository<Employee, Long> {
    List<Employee> findByDepartment(String department);
    Optional<Employee> findByEmail(String email);
    List<Employee> findByActiveTrue();

    @Query("SELECT e FROM Employee e WHERE e.salary BETWEEN :min AND :max")
    List<Employee> findBySalaryRange(
            @Param("min") BigDecimal min,
            @Param("max") BigDecimal max);


//    @Query("SELECT e FROM Employee e WHERE (:min IS NULL OR e.salary >= :min) " +
//            "AND (:max IS NULL OR e.salary <= :max)")
//    List<Employee> findBySalaryRange(@Param("min") Double min,
//                                     @Param("max") Double max);

    @Query("SELECT e FROM Employee e WHERE " +
            "(:department IS NULL OR e.department = :department) AND " +
            "(:active IS NULL OR e.active = :active)")
    List<Employee> findByDepartmentAndActive(
            @Param("department") String department,
            @Param("active") Boolean active
    );
}
