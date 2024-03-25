package com.board.board.service;

import com.board.board.controller.dto.user.AddUserRequest;
import com.board.board.controller.dto.user.LoginRequest;
import com.board.board.domain.entity.UserEntity;
import com.board.board.domain.repository.UserRepository;
import com.board.board.util.Encryption;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(AddUserRequest request, String checkPwd) {
        // 아이디 중복 체크
        UserEntity checkId = userRepository.findById(request.getUserId());

        if (checkId != null) {
            throw new RuntimeException("아이디가 존재합니다.");
        }

        // 비밀번호 체크
        String originPwd = request.getPassword();
        if (!originPwd.equals(checkPwd)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 암호화
        Encryption encryption = new Encryption();
        String encryptPwd = encryption.getEncrypt(request.getPassword(), encryption.salt);
        AddUserRequest update = new AddUserRequest(request.getUserId(), request.getName(), encryptPwd);

        userRepository.registerUser(update.toEntity());
    }

    public void loginUser(LoginRequest request) {
//        아이디 확인
        UserEntity checkId = userRepository.findById(request.getUserId());

        if (checkId == null){
            throw new RuntimeException("가입된 아이디 정보가 없습니다.");
        }

        // 비밀번호 암호화 및 일치 여부 확인
        Encryption encryption = new Encryption();
        String encryptPwd = encryption.getEncrypt(request.getPassword(), encryption.salt);

        if(!checkId.getPassword().equals(encryptPwd)){
            throw new RuntimeException("비밀번호가 틀립니다.");
        }
    }
}
