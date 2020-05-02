package com.course.server.service.admin;

import com.course.server.domain.${Domain};
import com.course.server.domain.${Domain}Example;
import com.course.server.dto.${Domain}Dto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.${Domain}Mapper;
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
public class ${Domain}Service {

    @Resource
    private ${Domain}Mapper ${domain}Mapper;

    public void  list(PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        ${Domain}Example ${domain}Example = new ${Domain}Example();
        //查询所有的${domain}
        List<${Domain}> ${domain}List = ${domain}Mapper.selectByExample(${domain}Example);
        //包含了Total等信息的，pageHelper自带的
        PageInfo<${Domain}> pageInfo = new PageInfo<>(${domain}List);

        pageDto.setTotal(pageInfo.getTotal());
        List<${Domain}Dto> ${domain}DtoList = CopyUtil.copyList(${domain}List, ${Domain}Dto.class);
        pageDto.setList(${domain}DtoList);
    }

    public void save(${Domain}Dto ${domain}Dto){
        //将${domain}Dto转换成${domain}
        //${Domain} ${domain} = new ${Domain}();
        //BeanUtils.copyProperties(${domain}Dto,${domain});
        ${Domain} ${domain} = CopyUtil.copy(${domain}Dto,${Domain}.class);
        if (StringUtils.isEmpty(${domain}Dto.getId())){
            this.insert(${domain});
        }else {
            this.update(${domain});
        }
    }

    private void insert(${Domain} ${domain}){
        ${domain}.setId(UuidUtil.getShortUuid());
        ${domain}Mapper.insert(${domain});
    }

    private void update(${Domain} ${domain}){
        ${domain}Mapper.updateByPrimaryKey(${domain});
    }

    public void delete(String id) {
        ${domain}Mapper.deleteByPrimaryKey(id);
    }
}
