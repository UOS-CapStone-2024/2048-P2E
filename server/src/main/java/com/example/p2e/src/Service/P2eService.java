package com.example.p2e.src.Service;

import com.example.p2e.config.BaseException;
import com.example.p2e.src.Dao.P2eDao;
import com.example.p2e.src.Dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.p2e.config.BaseResponseStatus.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class P2eService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
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
        } catch (Exception e) {
            logger.error("Error while creating user: ", e);
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
    public String modifyNickname(PostUser postUser) throws Exception{
        try {
            String success = p2eDao.modifyNickname(postUser);
            return success;
        } catch (Exception ignored) {
            throw new BaseException(REQUEST_ERROR);
        }
    }

    @Transactional
    public String recordPoint(PostPoint postPoint) throws Exception{
        try {
            String success = p2eDao.recordPoint(postPoint);
            return success;
        } catch (Exception e) {
            logger.error("Error while recording point: ", e);
            throw new BaseException(REQUEST_ERROR);
        }
    }

    @Transactional
    public PostItemRevive useRevive(PostItemRevive postItemRevive) throws BaseException {
        if (postItemRevive.getRevive() != 1){
            throw new BaseException(ITEM_ERROR);
        }
        if (p2eDao.getRevive(postItemRevive.getWeb3()) < 1){
            throw new BaseException(ITEM_NUM_ERROR);
        }
        PostItemRevive postItemRes = p2eDao.useRevive(postItemRevive);
        return postItemRes;
    }
}
