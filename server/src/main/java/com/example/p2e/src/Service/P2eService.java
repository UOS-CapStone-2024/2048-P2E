package com.example.p2e.src.Service;

import com.example.p2e.config.BaseException;
import com.example.p2e.config.BaseResponse;
import com.example.p2e.src.Dao.P2eDao;
import com.example.p2e.src.Dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.p2e.config.BaseResponseStatus.*;

@Service
public class P2eService {
    private final P2eDao p2eDao;

    @Autowired
    public P2eService(P2eDao p2eDao) {
        this.p2eDao = p2eDao;
    }

    @Transactional
    public String createUser(PostUser postUser) throws Exception{
        try {
            String success = p2eDao.createUser(postUser);
            return success;
        } catch (Exception ignored) {
            throw new BaseException(REQUEST_ERROR);
        }
    }

    @Transactional
    public GetLoginRes logIn(GetLoginReq getLoginReq) throws BaseException {
        try {
            GetLoginRes getLoginRes = p2eDao.logIn(getLoginReq);
            return getLoginRes;
        } catch (Exception ignored) {
            throw new BaseException(REQUEST_ERROR);
        }
    }

    @Transactional
    public String modifyNickname(PostNickname postNickname) throws Exception{
        try {
            String success = p2eDao.modifyNickname(postNickname);
            return success;
        } catch (Exception ignored) {
            throw new BaseException(REQUEST_ERROR);
        }
    }
}
