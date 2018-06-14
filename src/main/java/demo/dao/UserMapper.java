package demo.dao;

import javax.annotation.Resource;

import demo.model.User;

@Resource
public interface UserMapper {

	 User getUserByUsername(String username);
	
}
