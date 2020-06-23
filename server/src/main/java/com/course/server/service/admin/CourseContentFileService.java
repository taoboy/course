package com.course.server.service.admin;

import com.course.server.domain.CourseContentFile;
import com.course.server.domain.CourseContentFileExample;
import com.course.server.dto.CourseContentFileDto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.CourseContentFileMapper;
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
public class CourseContentFileService {

    @Resource
    private CourseContentFileMapper courseContentFileMapper;

    public List<CourseContentFileDto> list(String courseId){
        CourseContentFileExample example = new CourseContentFileExample();
        CourseContentFileExample.Criteria criteria = example.createCriteria();
        criteria.andCourseIdEqualTo(courseId);
        List<CourseContentFile> fileList = courseContentFileMapper.selectByExample(example);
        return CopyUtil.copyList(fileList,CourseContentFileDto.class);
    }

    public void save(CourseContentFileDto courseContentFileDto){
        //将courseContentFileDto转换成courseContentFile
        //CourseContentFile courseContentFile = new CourseContentFile();
        //BeanUtils.copyProperties(courseContentFileDto,courseContentFile);
        CourseContentFile courseContentFile = CopyUtil.copy(courseContentFileDto,CourseContentFile.class);
        if (StringUtils.isEmpty(courseContentFileDto.getId())){
            this.insert(courseContentFile);
        }else {
            this.update(courseContentFile);
        }
    }

    private void insert(CourseContentFile courseContentFile){

        courseContentFile.setId(UuidUtil.getShortUuid());
        courseContentFileMapper.insert(courseContentFile);
    }

    private void update(CourseContentFile courseContentFile){
        courseContentFileMapper.updateByPrimaryKey(courseContentFile);
    }

    public void delete(String id) {
        courseContentFileMapper.deleteByPrimaryKey(id);
    }
}
