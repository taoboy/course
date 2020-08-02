package com.course.system.controller.admin;


import com.course.server.dto.UserDto;
import com.course.server.dto.PageDto;
import com.course.server.dto.ResponseDto;
import com.course.server.exception.ValidatorException;
import com.course.server.service.admin.UserService;
import com.course.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/admin/user")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private static final String BUSINESS_NAME = "用户";

    @Resource
    private UserService userService;

    @PostMapping("/list")
    public ResponseDto list(@RequestBody PageDto pageDto){

        ResponseDto responseDto = new ResponseDto();
        userService.list(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    @PostMapping("/save")
    public ResponseDto save(@RequestBody UserDto userDto){
        //保存校验
                ValidatorUtil.require(userDto.getLoginName(), "登陆名");
                ValidatorUtil.length(userDto.getLoginName(), "登陆名", 1, 50);
                ValidatorUtil.length(userDto.getName(), "昵称", 1, 50);
                ValidatorUtil.require(userDto.getPassword(), "密码");

        ResponseDto responseDto = new ResponseDto();
        userService.save(userDto);
        responseDto.setContent(userDto);
        return responseDto;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id){

        ResponseDto responseDto = new ResponseDto();
        userService.delete(id);
        return responseDto;
    }
}