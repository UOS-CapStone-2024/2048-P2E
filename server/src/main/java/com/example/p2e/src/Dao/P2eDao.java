package com.example.p2e.src.Dao;

import com.example.p2e.src.Dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Repository
public class P2eDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Transactional
    public String createUser(PostUser postUser) {
        String createUserQuery = "insert into User (web3,nickname,point) VALUES (?,?,0)";
        Object[] createUserParams = new Object[]{postUser.getWeb3(), postUser.getNickname()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);


        String success = "회원가입 성공";
        return success;
    }

    @Transactional
    public GetLoginRes logIn(GetLoginReq getLoginReq) {
        String getUserQuery = "select * from User where web3 = ?";
        String insertUserQuery = "INSERT INTO User (web3, nickname, point, revive, delete) VALUES (?, 'anonymous', 0, 0, 0)";
        String getUserParams = getLoginReq.getWeb3(); // 클라이언트로부터 받은 id(web3)

        try {
            // 유저 조회 시도를 먼저 한다.
            int ranking = getRanking(getLoginReq);

            // 유저가 존재하면 해당 유저의 정보를 반환한다.
            return this.jdbcTemplate.queryForObject(getUserQuery,
                    (rs, rowNum) -> new GetLoginRes(
                            rs.getString("nickname"),
                            ranking,
                            rs.getInt("point"),
                            rs.getInt("revive"),
                            rs.getInt("delete")
                    ),
                    getUserParams
            );
        } catch (EmptyResultDataAccessException e) {
            // 유저가 존재하지 않으면 새로운 유저 정보를 삽입한다.
            this.jdbcTemplate.update(insertUserQuery, getUserParams);

            // 삽입 후 다시 해당 유저를 조회하여 정보를 반환한다.
            int ranking = getRanking(getLoginReq);
            return this.jdbcTemplate.queryForObject(getUserQuery,
                    (rs, rowNum) -> new GetLoginRes(
                            rs.getString("nickname"),
                            ranking,
                            rs.getInt("point"),
                            rs.getInt("revive"),
                            rs.getInt("delete")
                    ),
                    getUserParams
            );
        }
    }


    /*@Transactional
    public GetLoginRes logIn(GetLoginReq getLoginReq) {
        String getUserQuery = "select * from User where web3 = ?"; // 해당 id를 만족하는 User의 정보들을 조회한다.
        String getUserParams = getLoginReq.getWeb3(); // 주입될 id값을 클라이언트의 요청에서 주어진 정보를 통해 가져온다.

        int ranking = getRanking(getLoginReq);

        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetLoginRes(
                        rs.getString("nickname"),
                        ranking,
                        rs.getInt("point"),
                        rs.getInt("revive"),
                        rs.getInt("delete")
                ), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getUserParams
        ); // 한 개의 회원정보를 얻기 위한 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }*/

    public int getRanking(GetLoginReq getLoginReq) {
        String getRankingQuery = "select rank() over (order by point desc) as rank from User where web3 = ?";
        String getUserParams = getLoginReq.getWeb3();

        return this.jdbcTemplate.queryForObject(getRankingQuery,
                (rs, rowNum) -> rs.getInt("rank"),
                getUserParams
        );
    }

    @Transactional
    public String modifyNickname(PostUser postUser) {
        String modifyNicknameQuery = "update User set nickname = ? where web3 = ?";
        Object[] modifyNicknameParams = new Object[]{postUser.getNickname(), postUser.getWeb3()};
        this.jdbcTemplate.update(modifyNicknameQuery, modifyNicknameParams);


        String success = "닉네임 변경 성공";
        return success;
    }
}
