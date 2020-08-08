package com.course.server.service.admin;

import com.course.server.domain.RoleResource;
import com.course.server.domain.RoleResourceExample;
import com.course.server.dto.RoleResourceDto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.RoleResourceMapper;
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
public class RoleResourceService {

    @Resource
    private RoleResourceMapper roleResourceMapper;

    public void  list(PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        RoleResourceExample roleResourceExample = new RoleResourceExample();
        //查询所有的roleResource
        List<RoleResource> roleResourceList = roleResourceMapper.selectByExample(roleResourceExample);
        //包含了Total等信息的，pageHelper自带的
        PageInfo<RoleResource> pageInfo = new PageInfo<>(roleResourceList);

        pageDto.setTotal(pageInfo.getTotal());
        List<RoleResourceDto> roleResourceDtoList = CopyUtil.copyList(roleResourceList, RoleResourceDto.class);
        pageDto.setList(roleResourceDtoList);
    }

    public void save(RoleResourceDto roleResourceDto){
        //将roleResourceDto转换成roleResource
        //RoleResource roleResource = new RoleResource();
        //BeanUtils.copyProperties(roleResourceDto,roleResource);
        RoleResource roleResource = CopyUtil.copy(roleResourceDto,RoleResource.class);
        if (StringUtils.isEmpty(roleResourceDto.getId())){
            this.insert(roleResource);
        }else {
            this.update(roleResource);
        }
    }

    private void insert(RoleResource roleResource){

        roleResource.setId(UuidUtil.getShortUuid());
        roleResourceMapper.insert(roleResource);
    }

    private void update(RoleResource roleResource){
        roleResourceMapper.updateByPrimaryKey(roleResource);
    }

    public void delete(String id) {
        roleResourceMapper.deleteByPrimaryKey(id);
    }
}
