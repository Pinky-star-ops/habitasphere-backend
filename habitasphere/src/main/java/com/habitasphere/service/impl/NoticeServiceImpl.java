package com.habitasphere.service.impl;

import com.habitasphere.dto.NoticeRequestDto;
import com.habitasphere.dto.NoticeResponseDto;
import com.habitasphere.entity.Notice;
import com.habitasphere.entity.User;
import com.habitasphere.repository.NoticeRepository;
import com.habitasphere.repository.UserRepository;
import com.habitasphere.service.NoticeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    public NoticeServiceImpl(
            NoticeRepository noticeRepository,
            UserRepository userRepository
    ) {
        this.noticeRepository = noticeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public NoticeResponseDto createNotice(
            NoticeRequestDto request
    ) {

        String email =
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found"));

        Notice notice = Notice.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .type(request.getType())
                .priority(request.getPriority())
                .expiryDate(request.getExpiryDate())
                .createdAt(LocalDateTime.now())
                .active(true)
                .createdBy(user)
                .build();

        return mapToDto(
                noticeRepository.save(notice)
        );
    }

    @Override
    public NoticeResponseDto updateNotice(
            Long id,
            NoticeRequestDto request
    ) {

        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Notice not found"));

        notice.setTitle(request.getTitle());
        notice.setContent(request.getContent());
        notice.setType(request.getType());
        notice.setPriority(request.getPriority());
        notice.setExpiryDate(request.getExpiryDate());

        return mapToDto(
                noticeRepository.save(notice)
        );
    }

    @Override
    public void deleteNotice(Long id) {

        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Notice not found"));

        notice.setActive(false);

        noticeRepository.save(notice);
    }

    @Override
    public List<NoticeResponseDto> getActiveNotices() {

        return noticeRepository.findActiveNotices(
        LocalDate.now()
)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private NoticeResponseDto mapToDto(
            Notice notice
    ) {

        return NoticeResponseDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .type(notice.getType())
                .priority(notice.getPriority())
                .createdAt(notice.getCreatedAt())
                .expiryDate(notice.getExpiryDate())
                .active(notice.isActive())
                .createdBy(
                        notice.getCreatedBy() != null
                                ? notice.getCreatedBy().getName()
                                : null
                )
                .build();
    }
}