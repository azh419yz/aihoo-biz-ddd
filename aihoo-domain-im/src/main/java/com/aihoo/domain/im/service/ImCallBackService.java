package com.aihoo.domain.im.service;

import com.aihoo.domain.im.dto.ImCallbackReqDto;

public interface ImCallBackService {
    void callBack(ImCallbackReqDto request);
}
