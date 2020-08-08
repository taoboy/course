package com.course.server.service.admin;

import com.alibaba.fastjson.JSON;
import com.course.server.domain.Resource;
import com.course.server.domain.ResourceExample;
import com.course.server.dto.ResourceDto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.ResourceMapper;
import com.course.server.util.CopyUtil;
import com.course.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceService {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceService.class);

    @javax.annotation.Resource
    private ResourceMapper resourceMapper;

    public void  list(PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        ResourceExample resourceExample = new ResourceExample();
        //查询所有的resource
        List<Resource> resourceList = resourceMapper.selectByExample(resourceExample);
        //包含了Total等信息的，pageHelper自带的
        PageInfo<Resource> pageInfo = new PageInfo<>(resourceList);

        pageDto.setTotal(pageInfo.getTotal());
        List<ResourceDto> resourceDtoList = CopyUtil.copyList(resourceList, ResourceDto.class);
        pageDto.setList(resourceDtoList);
    }

    public void save(ResourceDto resourceDto){
        //将resourceDto转换成resource
        //Resource resource = new Resource();
        //BeanUtils.copyProperties(resourceDto,resource);
        Resource resource = CopyUtil.copy(resourceDto,Resource.class);
        if (StringUtils.isEmpty(resourceDto.getId())){
            this.insert(resource);
        }else {
            this.update(resource);
        }
    }

    private void insert(Resource resource){
        resourceMapper.insert(resource);
    }

    private void update(Resource resource){
        resourceMapper.updateByPrimaryKey(resource);
    }

    public void delete(String id) {
        resourceMapper.deleteByPrimaryKey(id);
    }

    @Transactional
    public void saveJson(String json) {
        List<ResourceDto> jsonList = JSON.parseArray(json,ResourceDto.class);
        List<ResourceDto> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(jsonList)){
            for (ResourceDto d: jsonList){
                //得到第一层节点，父节点都是空的
                d.setParent("");
                //add方法是一个递归函数，
                // 功能是将d节点下额子节点添加到list中,如果子节点下还有子节点，则递归调用add方法
                add(list,d);
            }
        }
        LOG.info("供{}条",list.size());

        resourceMapper.deleteByExample(null);
        for (int i = 0; i < list.size(); i++){
            this.insert(CopyUtil.copy(list.get(i), Resource.class));
        }
    }

    /**
     * 递归，将树型结构的节点全部取出来，放到List
     */
    public void add(List<ResourceDto> list, ResourceDto dto){
        list.add(dto);
        if (!CollectionUtils.isEmpty(dto.getChildren())){
            for (ResourceDto d : dto.getChildren()){
                d.setParent(dto.getId());
                add(list,d);
            }
        }
    }
}
