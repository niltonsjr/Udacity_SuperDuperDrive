package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userId = #{userId}")
    List<File> getAllFiles(int userId);

    @Select("SELECT fileId, filename, contenttype, filesize, userid, filedata FROM FILES " +
            "WHERE fileid = #{fileId}")
    File getFileById(int fileId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " +
            "VALUES (#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insertFile(File file);

    @Update("UPDATE FILES SET fileid = #{fileId}, filename = #{fileName}, contenttype = #{contentType}, filesize = #{fileSize}, userid = #{userId}, filedata = #{fileData}" +
            "WHERE fileid = #{fileId}")
    void updateFile(File file);

    @Delete("DELETE FROM FILES WHERE fileid = #{fileId}")
    void deleteFile(int fileId);
}
