package com.training.springbootbuyitem.service.interfaces;

import com.training.springbootbuyitem.entity.model.CustomSessionManagerImpl;
import com.training.springbootbuyitem.entity.model.User;

public interface IUserService extends ICrudService<User>{

    String userInfo(CustomSessionManagerImpl customSessionManager);
}
