package com.rkc.zds.service;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
@Service
public interface TagReadService {
    List<String> all();
}
