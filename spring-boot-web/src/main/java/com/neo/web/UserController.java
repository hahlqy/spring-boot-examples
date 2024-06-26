package com.neo.web;

import java.util.List;

import com.neo.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neo.model.User;
import com.neo.repository.UserRepository;

@RestController
public class UserController {
	
	@Autowired
	private UserRepository userRepository;

    @RequestMapping("/add")
    public User saveUser(String key) {
        User user=new User();
        user.setUserName(key);
        user.setEmail("ityouknow"+key+"@126.com");
        user.setNickName("微笑-"+key);
        user.setPassWord("123456-"+key);
        user.setRegTime(DateUtils.getDateYyyyMmDd());
        try{
            userRepository.save(user);
        }catch (Exception e){
            System.out.println("插入用户"+key+"失败，原因是："+e.getMessage());
        }
        return user;
    }
	
    @RequestMapping("/getUserByName")
    public User getUserByName(String userName) {
    	User user=userRepository.findByUserName(userName);
    	System.out.println("若下面没出现“无缓存的时候调用”字样且能打印出数据表示测试成功");  
        return user;
    }
    
    @RequestMapping("/getUsers")
    public List<User> getUsers() {
    	List<User> users=userRepository.findAll();
    	System.out.println("若下面没出现“无缓存的时候调用”字样且能打印出数据表示测试成功");  
        return users;
    }
}