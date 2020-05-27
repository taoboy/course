package com.course.server.service.admin;

import com.course.server.domain.CourseCategory;
import com.course.server.domain.CourseCategoryExample;
import com.course.server.dto.CategoryDto;
import com.course.server.dto.CourseCategoryDto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.CourseCategoryMapper;
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
public class CourseCategoryService {

    @Resource
    private CourseCategoryMapper courseCategoryMapper;

    public void  list(PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        CourseCategoryExample courseCategoryExample = new CourseCategoryExample();
        //查询所有的courseCategory
        List<CourseCategory> courseCategoryList = courseCategoryMapper.selectByExample(courseCategoryExample);
        //包含了Total等信息的，pageHelper自带的
        PageInfo<CourseCategory> pageInfo = new PageInfo<>(courseCategoryList);

        pageDto.setTotal(pageInfo.getTotal());
        List<CourseCategoryDto> courseCategoryDtoList = CopyUtil.copyList(courseCategoryList, CourseCategoryDto.class);
        pageDto.setList(courseCategoryDtoList);
    }

    public void save(CourseCategoryDto courseCategoryDto){
        //将courseCategoryDto转换成courseCategory
        //CourseCategory courseCategory = new CourseCategory();
        //BeanUtils.copyProperties(courseCategoryDto,courseCategory);
        CourseCategory courseCategory = CopyUtil.copy(courseCategoryDto,CourseCategory.class);
        if (StringUtils.isEmpty(courseCategoryDto.getId())){
            this.insert(courseCategory);
        }else {
            this.update(courseCategory);
        }
    }

    private void insert(CourseCategory courseCategory){

        courseCategory.setId(UuidUtil.getShortUuid());
        courseCategoryMapper.insert(courseCategory);
    }

    private void update(CourseCategory courseCategory){
        courseCategoryMapper.updateByPrimaryKey(courseCategory);
    }

    public void delete(String id) {
        courseCategoryMapper.deleteByPrimaryKey(id);
    }

    public void saveBatch(String courseId, List<CategoryDto> dtoList){
        CourseCategoryExample example = new CourseCategoryExample();
        example.createCriteria().andCourseIdEqualTo(courseId);
        courseCategoryMapper.deleteByExample(example);
        for (int i = 0; i < dtoList.size(); i++) {
            CategoryDto categoryDto = dtoList.get(i);
            CourseCategory courseCategory = new CourseCategory();
            courseCategory.setId(UuidUtil.getShortUuid());
            courseCategory.setCourseId(courseId);
            courseCategory.setCategoryId(categoryDto.getId());
            courseCategoryMapper.insert(courseCategory);
        }
    }
}
