package com.gym.gymmaster.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gym.gymmaster.common.BackMsg;
import com.gym.gymmaster.entity.Employee;
import com.gym.gymmaster.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Value("${gym.path}")
    private String basePath;
    @PostMapping("/register")
    public BackMsg<String> register(@RequestBody Employee employee){
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee employee1 = employeeService.getOne(queryWrapper);
        if (employee1 != null){
            return BackMsg.error("employee name already exists");
        }
        if (employee.getProfile() == null){
            employee.setProfile(basePath+"picture/default.png");
        }
        employeeService.save(employee);
        return BackMsg.success("registered successfully");
    }
    @GetMapping("/login")
    public BackMsg<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee employee1 = employeeService.getOne(queryWrapper);
        if(employee1 == null){
            return BackMsg.error("Login Failed, Wrong username or password");
        }
        if(!employee1.getPassword().equals(employee.getPassword())){
            return BackMsg.error("Login Failed, Wrong username or password");
        }

        request.getSession().setAttribute("employee",employee.getEid());
        return BackMsg.success(employee);
    }

    @PostMapping("/add")
    public BackMsg<String> add(@RequestBody Employee employee){
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        if(employeeService.getOne(queryWrapper)!=null){
            return BackMsg.error("username already existed!");
        }

        if (employee.getProfile() == null){
            employee.setProfile(basePath+"picture/default.png");
        }
        employeeService.save(employee);
        return BackMsg.success("new employee added successfully");
    }
    @GetMapping("/page")
    public BackMsg<Page> page(int page, int pageSize, String name){



        Page pageInfo = new Page(page,pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getUsername,name);
        queryWrapper.orderByDesc(Employee::getEid);
        employeeService.page(pageInfo,queryWrapper);
        return BackMsg.success(pageInfo);
    }

    // used to truly update the data in the database
    @PutMapping("/update")
    public BackMsg<String> update(@RequestBody Employee employee){

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getEid,employee.getEid());
        employeeService.update(employee,queryWrapper);
        return BackMsg.success("updated successfully!");
    }
    // used to return all the editable information of the employee
    @GetMapping("/info")
    public BackMsg<Employee> getbyId(@RequestBody Employee employee){
        Employee emp = employeeService.getById(employee.getEid());
        return BackMsg.success(emp);
    }
    @DeleteMapping
    public BackMsg<String> delet(@RequestBody Employee employee){
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getEid,employee.getEid());
        employeeService.remove(queryWrapper);
        return BackMsg.success("remove the employe successfully");
    }
    @PostMapping("/logout")
    public BackMsg<String> logout(HttpServletRequest request){
        //clear the session to indicate logout
        request.getSession().removeAttribute("employee");
        return BackMsg.success("successfully log out");
    }

}
