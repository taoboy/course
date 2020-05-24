package com.course.server.service.admin;

import com.course.server.domain.Category;
import com.course.server.domain.CategoryExample;
import com.course.server.dto.CategoryDto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.CategoryMapper;
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
public class CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    public List<CategoryDto> all(){

        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        //查询所有的category
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        //包含了Total等信息的，pageHelper自带的
        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        List<CategoryDto> categoryDtoList = CopyUtil.copyList(categoryList, CategoryDto.class);
        return categoryDtoList;
    }

    public void  list(PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        //查询所有的category
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        //包含了Total等信息的，pageHelper自带的
        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);

        pageDto.setTotal(pageInfo.getTotal());
        List<CategoryDto> categoryDtoList = CopyUtil.copyList(categoryList, CategoryDto.class);
        pageDto.setList(categoryDtoList);
    }
    public void save(CategoryDto categoryDto){
        //将categoryDto转换成category
        //Category category = new Category();
        //BeanUtils.copyProperties(categoryDto,category);
        Category category = CopyUtil.copy(categoryDto,Category.class);
        if (StringUtils.isEmpty(categoryDto.getId())){
            this.insert(category);
        }else {
            this.update(category);
        }
    }

    private void insert(Category category){
        
        category.setId(UuidUtil.getShortUuid());
        categoryMapper.insert(category);
    }

    private void update(Category category){
        categoryMapper.updateByPrimaryKey(category);
    }

    public void delete(String id) {
        categoryMapper.deleteByPrimaryKey(id);
    }
}
