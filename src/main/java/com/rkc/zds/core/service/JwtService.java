package com.rkc.zds.core.service;

import com.rkc.zds.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface JwtService {
    String toToken(UserDto user);

    Optional<String> getSubFromToken(String token);
}
