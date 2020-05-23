package com.course.server.service.admin;

import com.course.server.domain.Course;
import com.course.server.domain.CourseExample;
import com.course.server.dto.CourseDto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.CourseMapper;
import com.course.server.mapper.my.MyCourseMapper;
import com.course.server.util.CopyUtil;
import com.course.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@Service
public class CourseService {

    private static final Logger LOG = LoggerFactory.getLogger(CourseService.class);

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private MyCourseMapper myCourseMapper;

    public void  list(PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        CourseExample courseExample = new CourseExample();
        courseExample.setOrderByClause("sort asc");
        //查询所有的course
        List<Course> courseList = courseMapper.selectByExample(courseExample);
        //包含了Total等信息的，pageHelper自带的
        PageInfo<Course> pageInfo = new PageInfo<>(courseList);

        pageDto.setTotal(pageInfo.getTotal());
        List<CourseDto> courseDtoList = CopyUtil.copyList(courseList, CourseDto.class);
        pageDto.setList(courseDtoList);
    }

    public void save(CourseDto courseDto){
        //将courseDto转换成course
        //Course course = new Course();
        //BeanUtils.copyProperties(courseDto,course);
        Course course = CopyUtil.copy(courseDto,Course.class);
        if (StringUtils.isEmpty(courseDto.getId())){
            this.insert(course);
        }else {
            this.update(course);
        }
    }

    private void insert(Course course){
        Date now = new Date();
        course.setCreatedAt(now);
        course.setUpdatedAt(now);
        course.setId(UuidUtil.getShortUuid());
        courseMapper.insert(course);
    }

    private void update(Course course){
        course.setUpdatedAt(new Date());
        courseMapper.updateByPrimaryKey(course);
    }

    public void delete(String id) {
        courseMapper.deleteByPrimaryKey(id);
    }

    public void updateTime(String courseId){
        LOG.info("更新课程时长：{}",courseId);
        myCourseMapper.updateTime(courseId);
    }
}
