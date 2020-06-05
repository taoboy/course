package com.course.server.service.admin;

import com.course.server.domain.Teacher;
import com.course.server.domain.TeacherExample;
import com.course.server.domain.Teacher;
import com.course.server.domain.TeacherExample;
import com.course.server.dto.TeacherDto;
import com.course.server.dto.TeacherDto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.TeacherMapper;
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
public class TeacherService {

    @Resource
    private TeacherMapper teacherMapper;

    public void  list(PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        TeacherExample teacherExample = new TeacherExample();
        //查询所有的teacher
        List<Teacher> teacherList = teacherMapper.selectByExample(teacherExample);
        //包含了Total等信息的，pageHelper自带的
        PageInfo<Teacher> pageInfo = new PageInfo<>(teacherList);

        pageDto.setTotal(pageInfo.getTotal());
        List<TeacherDto> teacherDtoList = CopyUtil.copyList(teacherList, TeacherDto.class);
        pageDto.setList(teacherDtoList);
    }

    public void save(TeacherDto teacherDto){
        //将teacherDto转换成teacher
        //Teacher teacher = new Teacher();
        //BeanUtils.copyProperties(teacherDto,teacher);
        Teacher teacher = CopyUtil.copy(teacherDto,Teacher.class);
        if (StringUtils.isEmpty(teacherDto.getId())){
            this.insert(teacher);
        }else {
            this.update(teacher);
        }
    }

    private void insert(Teacher teacher){

        teacher.setId(UuidUtil.getShortUuid());
        teacherMapper.insert(teacher);
    }

    private void update(Teacher teacher){
        teacherMapper.updateByPrimaryKey(teacher);
    }

    public void delete(String id) {
        teacherMapper.deleteByPrimaryKey(id);
    }

    public List<TeacherDto> all(){

        TeacherExample teacherExample = new TeacherExample();
        //查询所有的teacher
        List<Teacher> teacherList = teacherMapper.selectByExample(teacherExample);
        return CopyUtil.copyList(teacherList,TeacherDto.class);
    }
}
