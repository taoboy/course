package com.course.server.service.admin;

import com.course.server.domain.File;
import com.course.server.domain.FileExample;
import com.course.server.dto.FileDto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.FileMapper;
import com.course.server.util.CopyUtil;
import com.course.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@Service
public class FileService {

    @Resource
    private FileMapper fileMapper;

    public void  list(PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        FileExample fileExample = new FileExample();
        //查询所有的file
        List<File> fileList = fileMapper.selectByExample(fileExample);
        //包含了Total等信息的，pageHelper自带的
        PageInfo<File> pageInfo = new PageInfo<>(fileList);

        pageDto.setTotal(pageInfo.getTotal());
        List<FileDto> fileDtoList = CopyUtil.copyList(fileList, FileDto.class);
        pageDto.setList(fileDtoList);
    }

    public void save(FileDto fileDto){
        //将fileDto转换成file
        //File file = new File();
        //BeanUtils.copyProperties(fileDto,file);
        File file = CopyUtil.copy(fileDto,File.class);
        File fileDb = selectByKey(fileDto.getKey());
        if (fileDb == null){
            this.insert(file);
        }else {
            fileDb.setShardIndex(fileDto.getShardIndex());
            this.update(fileDb);
        }
    }

    private void insert(File file){
                Date now = new Date();

        file.setCreatedAt(now);
        file.setUpdatedAt(now);
        file.setId(UuidUtil.getShortUuid());
        fileMapper.insert(file);
    }

    private void update(File file){
        file.setUpdatedAt(new Date());
        fileMapper.updateByPrimaryKey(file);
    }

    public void delete(String id) {
        fileMapper.deleteByPrimaryKey(id);
    }

    public File selectByKey(String key){
        FileExample example = new FileExample();
        example.createCriteria().andKeyEqualTo(key);
        List<File> fileList = fileMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(fileList)){
            return null;
        }else {
            return fileList.get(0);
        }
    }

    /**
     * 根据文件标识查询数据库记录
     */
    public FileDto findByKey(String key) {
        return CopyUtil.copy(selectByKey(key), FileDto.class);
    }
}
