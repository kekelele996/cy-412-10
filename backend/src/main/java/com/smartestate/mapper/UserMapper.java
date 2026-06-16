package com.smartestate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartestate.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
