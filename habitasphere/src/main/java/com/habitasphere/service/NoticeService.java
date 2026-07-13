package com.habitasphere.service;

import com.habitasphere.dto.NoticeRequestDto;
import com.habitasphere.dto.NoticeResponseDto;

import java.util.List;

public interface NoticeService {

    NoticeResponseDto createNotice(NoticeRequestDto request);

    NoticeResponseDto updateNotice(Long id,
                                   NoticeRequestDto request);

    void deleteNotice(Long id);

    List<NoticeResponseDto> getActiveNotices();
    NoticeResponseDto getNoticeById(Long id);
}