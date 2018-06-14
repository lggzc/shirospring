package demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.dao.UserMapper;
import demo.model.User;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;
	
	public User getUserByName(String userName){
		return userMapper.getUserByUsername(userName);
	}
	
}
