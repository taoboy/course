package com.course.server.mapper.my;

import com.course.server.dto.SortDto;
import org.apache.ibatis.annotations.Param;

public interface MyCourseMapper {

    int updateTime(@Param("courseId")String courseId);

    void updateSort(SortDto sortDto);

    void moveSortsForward(SortDto sortDto);

    void moveSortsBackward(SortDto sortDto);
}
