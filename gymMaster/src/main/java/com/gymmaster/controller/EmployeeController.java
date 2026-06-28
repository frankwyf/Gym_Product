package com.gymmaster.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gymmaster.common.BackMsg;
import com.gymmaster.entity.Employee;
import com.gymmaster.exception.BusinessException;
import com.gymmaster.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @Value("${gym.path}")
    private String basePath;

    @PostMapping("/register")
    public BackMsg<String> register(@Valid @RequestBody Employee employee) {
        LambdaQueryWrapper<Employee> qw = new LambdaQueryWrapper<Employee>()
                .eq(Employee::getUsername, employee.getUsername());
        if (employeeService.getOne(qw) != null) {
            throw new BusinessException("Username already exists.");
        }
        if (employee.getProfile() == null) {
            employee.setProfile("default.png");
        }
        employeeService.save(employee);
        return BackMsg.success("Registered successfully.");
    }

    @PostMapping("/add")
    public BackMsg<String> add(@Valid @RequestBody Employee employee) {
        LambdaQueryWrapper<Employee> qw = new LambdaQueryWrapper<Employee>()
                .eq(Employee::getUsername, employee.getUsername());
        if (employeeService.getOne(qw) != null) {
            throw new BusinessException("Username already exists.");
        }
        if (employee.getProfile() == null) {
            employee.setProfile("default.png");
        }
        employeeService.save(employee);
        return BackMsg.success("New employee added successfully.");
    }

    @GetMapping("/page")
    public BackMsg<Page<Employee>> page(int page, int pageSize, String name) {
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Employee> qw = new LambdaQueryWrapper<Employee>()
                .like(StringUtils.isNotEmpty(name), Employee::getUsername, name)
                .orderByDesc(Employee::getEid);
        employeeService.page(pageInfo, qw);
        return BackMsg.success(pageInfo);
    }

    @PutMapping("/update")
    public BackMsg<String> update(@RequestBody Employee employee) {
        LambdaQueryWrapper<Employee> qw = new LambdaQueryWrapper<Employee>()
                .eq(Employee::getEid, employee.getEid());
        employeeService.update(employee, qw);
        return BackMsg.success("Updated successfully.");
    }

    @GetMapping("/info")
    public BackMsg<Employee> getById(int eid) {
        Employee emp = employeeService.getById(eid);
        if (emp == null) throw new BusinessException("Employee not found.");
        return BackMsg.success(emp);
    }

    @DeleteMapping
    public BackMsg<String> delete(@RequestBody Employee employee) {
        LambdaQueryWrapper<Employee> qw = new LambdaQueryWrapper<Employee>()
                .eq(Employee::getEid, employee.getEid());
        employeeService.remove(qw);
        return BackMsg.success("Employee removed.");
    }

    @PostMapping("/logout")
    public BackMsg<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return BackMsg.success("successfully log out");
    }
}
