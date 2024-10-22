package com.example.p2e.src.Controller;

import com.example.p2e.config.BaseException;
import com.example.p2e.config.BaseResponse;
import com.example.p2e.src.Dto.*;
import com.example.p2e.src.Service.P2eService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Map;

import static com.example.p2e.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/p2e")
public class P2eController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private final P2eService p2eService;

    public P2eController(P2eService p2eService) {
        this.p2eService = p2eService;
    }
    
    //회원가입
    @PostMapping("/createUser")
    @ResponseBody
    public BaseResponse<String> createUser(@RequestBody PostUser postUser) throws Exception {
        try {
            String result = p2eService.createUser(postUser);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //로그인
    /*@ResponseBody
    @GetMapping("/log-in")
    public BaseResponse<GetLoginRes> logIn(@RequestBody GetLoginReq getLoginReq) {
        try {
            GetLoginRes getLoginRes = p2eService.logIn(getLoginReq);
            return new BaseResponse<>(getLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }*/

    //시그니처 로그인 및 회원가입
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<GetLoginRes> login(@RequestBody Map<String, String> request) {
        String address = request.get("address");
        String message = request.get("message");
        String signature = request.get("signature");
        GetLoginReq getLoginReq = new GetLoginReq (address);

        try {
            String signerAddress = verifyMessage(message, signature);

            if (signerAddress.equalsIgnoreCase(address)) {
                GetLoginRes getLoginRes = p2eService.logIn(getLoginReq);
                return new BaseResponse<>(getLoginRes);
            } else {
                return new BaseResponse<>(MESSAGE_ERROR);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    //메세지 인증
    public String verifyMessage(String message, String signature) throws BaseException {
        try {
            byte[] messageHash = Hash.sha3(message.getBytes());  // 메시지 해싱

            byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
            Sign.SignatureData sigData = new Sign.SignatureData(
                    signatureBytes[64], // v
                    java.util.Arrays.copyOfRange(signatureBytes, 0, 32), // r
                    java.util.Arrays.copyOfRange(signatureBytes, 32, 64) // s
            );

            // 서명에서 공개 키를 복구
            BigInteger publicKey = Sign.signedMessageToKey(messageHash, sigData);
            return "0x" + Keys.getAddress(publicKey);  // 서명된 공개 키를 통해 주소 반환
        } catch (Exception e) {
            // 예외 발생 시 BaseException으로 처리
            throw new BaseException(VERIFICATION_ERROR);
        }
    }

    //닉네임 변경
    @PostMapping("/modifyNickname")
    @ResponseBody
    public BaseResponse<String> modifyNickname(@RequestBody PostUser postUser) throws Exception {
        try {
            String result = p2eService.modifyNickname(postUser);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //점수 기록
    @PostMapping("/recordPoint")
    @ResponseBody
    public BaseResponse<String> recordPoint(@RequestBody PostPoint postPoint) throws Exception {
        try {
            String result = p2eService.recordPoint(postPoint);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //부활 사용
    @PostMapping("/useRevive")
    @ResponseBody
    public BaseResponse<PostItemRevive> useRevive(@RequestBody PostItemRevive postItemRevive) throws BaseException {
        try {
            return new BaseResponse<>(p2eService.useRevive(postItemRevive));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //블록 제거 사용
    @PostMapping("/useDelete")
    @ResponseBody
    public BaseResponse<PostItemDelete> useDelete(@RequestBody PostItemDelete postItemDelete) throws BaseException {
        try {
            return new BaseResponse<>(p2eService.useDelete(postItemDelete));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
