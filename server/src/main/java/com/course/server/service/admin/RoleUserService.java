package com.course.server.service.admin;

import com.course.server.domain.RoleUser;
import com.course.server.domain.RoleUserExample;
import com.course.server.dto.RoleUserDto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.RoleUserMapper;
import com.course.server.util.CopyUtil;
import com.course.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoleUserService {

    @Resource
    private RoleUserMapper roleUserMapper;

    public void  list(PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        RoleUserExample roleUserExample = new RoleUserExample();
        //查询所有的roleUser
        List<RoleUser> roleUserList = roleUserMapper.selectByExample(roleUserExample);
        //包含了Total等信息的，pageHelper自带的
        PageInfo<RoleUser> pageInfo = new PageInfo<>(roleUserList);

        pageDto.setTotal(pageInfo.getTotal());
        List<RoleUserDto> roleUserDtoList = CopyUtil.copyList(roleUserList, RoleUserDto.class);
        pageDto.setList(roleUserDtoList);
    }

    public void save(RoleUserDto roleUserDto){
        //将roleUserDto转换成roleUser
        //RoleUser roleUser = new RoleUser();
        //BeanUtils.copyProperties(roleUserDto,roleUser);
        RoleUser roleUser = CopyUtil.copy(roleUserDto,RoleUser.class);
        if (StringUtils.isEmpty(roleUserDto.getId())){
            this.insert(roleUser);
        }else {
            this.update(roleUser);
        }
    }

    private void insert(RoleUser roleUser){

        roleUser.setId(UuidUtil.getShortUuid());
        roleUserMapper.insert(roleUser);
    }

    private void update(RoleUser roleUser){
        roleUserMapper.updateByPrimaryKey(roleUser);
    }

    public void delete(String id) {
        roleUserMapper.deleteByPrimaryKey(id);
    }
}
