package com.course.server.service.admin;

import com.alibaba.fastjson.JSON;
import com.course.server.domain.User;
import com.course.server.domain.UserExample;
import com.course.server.dto.LoginUserDto;
import com.course.server.dto.ResourceDto;
import com.course.server.dto.UserDto;
import com.course.server.dto.PageDto;
import com.course.server.exception.BusinessException;
import com.course.server.exception.BusinessExceptionCode;
import com.course.server.mapper.UserMapper;
import com.course.server.mapper.my.MyUserMapper;
import com.course.server.util.CopyUtil;
import com.course.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private MyUserMapper myUserMapper;

    public void  list(PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        UserExample userExample = new UserExample();
        //查询所有的user
        List<User> userList = userMapper.selectByExample(userExample);
        //包含了Total等信息的，pageHelper自带的
        PageInfo<User> pageInfo = new PageInfo<>(userList);

        pageDto.setTotal(pageInfo.getTotal());
        List<UserDto> userDtoList = CopyUtil.copyList(userList, UserDto.class);
        pageDto.setList(userDtoList);
    }

    public void save(UserDto userDto){
        //将userDto转换成user
        //User user = new User();
        //BeanUtils.copyProperties(userDto,user);
        User user = CopyUtil.copy(userDto,User.class);
        if (StringUtils.isEmpty(userDto.getId())){
            this.insert(user);
        }else {
            this.update(user);
        }
    }

    private void insert(User user){

        user.setId(UuidUtil.getShortUuid());
        User userDb = this.selectByLoginName(user.getLoginName());
        if (userDb != null){
            throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
        }
        userMapper.insert(user);
    }

    private void update(User user){
        user.setPassword(null);
        userMapper.updateByPrimaryKeySelective(user);
    }

    public void delete(String id) {
        userMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据登录名查询用户信息
     * @param loginName
     * @return
     */
    public User selectByLoginName(String loginName){
        UserExample userExample = new UserExample();
        userExample.createCriteria().andLoginNameEqualTo(loginName);
        List<User> userList = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(userList)){
            return null;
        }else {
            return userList.get(0);
        }
    }

    /**
     * 重置密码
     * @param userDto
     */
    public void savePassword(UserDto userDto){
        User user = new User();
        user.setId(userDto.getId());
        user.setPassword(userDto.getPassword());
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 登录
     * @param userDto
     */
    public LoginUserDto login(UserDto userDto) {
        //登录验证思考：是否是根据用户名+密码到数据中去查找记录？
        //用户名+密码去数据库查找的话，程序不知道是用户名不对，还是密码不对。
        //程序应该要能知道，比如我如果发现有大量的用户名不对的报错，说明有人正在不断的探测我系统的用户名
        User user = selectByLoginName(userDto.getLoginName());
        if (user == null){
            LOG.info("用户名不存在:{}",userDto.getLoginName());
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        }else {
            if (user.getPassword().equals(userDto.getPassword())){
                //登录成功
                LoginUserDto loginUserDto = CopyUtil.copy(user,LoginUserDto.class);
                //为登录用户读取权限
                setAuth(loginUserDto);
                return loginUserDto;
            }else {
                LOG.info("密码不对，输入密码:{},数据库密码:{}",userDto.getPassword(),user.getPassword());
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }
    }

    /**
     * 为登录用户读取权限
     */
    public void setAuth(LoginUserDto loginUserDto){
        List<ResourceDto> resourceDtoList = myUserMapper.findResources(loginUserDto.getId());
        loginUserDto.setResources(resourceDtoList);

        //整理所有有权限的请求，用于接口拦截
        HashSet<String> requestSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(resourceDtoList)){
            for (int i = 0, l = resourceDtoList.size(); i < l; i++){
                ResourceDto resourceDto = resourceDtoList.get(i);
                String arrayString = resourceDto.getRequest();
                List<String> requestList = JSON.parseArray(arrayString,String.class);
                if (!CollectionUtils.isEmpty(requestList)){
                    requestSet.addAll(requestList);
                }
            }
        }
        LOG.info("有权限的请求:{}",requestSet);
        loginUserDto.setRequests(requestSet);

    }
}
