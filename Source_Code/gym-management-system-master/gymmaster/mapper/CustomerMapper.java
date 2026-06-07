package com.gym.gymmaster.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.gymmaster.entity.Customer;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
}
