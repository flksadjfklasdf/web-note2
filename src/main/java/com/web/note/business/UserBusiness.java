package com.web.note.business;

import com.web.note.dao.FileResourceMapper;
import com.web.note.dao.ImageMapper;
import com.web.note.dao.UserMapper;
import com.web.note.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserBusiness {
    @Autowired
    public UserMapper userMapper;
    @Autowired
    public FileResourceMapper fileResourceMapper;
    @Autowired
    public ImageMapper imageMapper;

    public int getAvailableSpace(User user) {
        String userId = user.getUserId();
        User userDB = userMapper.selectByPrimaryKey(userId);
        int totalStorage = userDB.getStorageLimit();

        int fs = fileResourceMapper.countFileSizeByUser(userId);
        int is = imageMapper.countImageSizeByUser(userId);

        return totalStorage - fs - is;

    }
}
