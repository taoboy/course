package com.course.server.service.admin;

import com.course.server.domain.Chapter;
import com.course.server.domain.ChapterExample;
import com.course.server.dto.ChapterDto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.ChapterMapper;
import com.course.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChapterService {

    @Resource
    private ChapterMapper chapterMapper;

    public void  list(PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        ChapterExample chapterExample = new ChapterExample();
        //查询所有的chapter
        List<Chapter> chapterList = chapterMapper.selectByExample(chapterExample);
        //包含了Total等信息的，pageHelper自带的
        PageInfo<Chapter> pageInfo = new PageInfo<>(chapterList);

        pageDto.setTotal(pageInfo.getTotal());

        //将chapterList的内容复制到chapterDtoList中
        List<ChapterDto> chapterDtoList = new ArrayList<ChapterDto>();
        for (int i = 0, l =chapterList.size(); i < l; i++) {
            Chapter chapter = chapterList.get(i);
            ChapterDto chapterDto = new ChapterDto();
            BeanUtils.copyProperties(chapter,chapterDto);
            chapterDtoList.add(chapterDto);
        }
//        List<ChapterDto> chapterDtoList = CopyUtil.copyList(chapterList, ChapterDto.class);
        pageDto.setList(chapterDtoList);
    }

    public void save(ChapterDto chapterDto){
        chapterDto.setId(UuidUtil.getShortUuid());
        //将chapterDto转换成chapter
        Chapter chapter = new Chapter();
        BeanUtils.copyProperties(chapterDto,chapter);
       chapterMapper.insert(chapter);
    }
}
