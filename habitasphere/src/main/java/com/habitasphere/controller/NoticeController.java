package com.habitasphere.controller;

import com.habitasphere.dto.NoticeRequestDto;
import com.habitasphere.dto.NoticeResponseDto;
import com.habitasphere.service.NoticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(
            NoticeService noticeService
    ) {
        this.noticeService = noticeService;
    }

    @PostMapping
    public ResponseEntity<NoticeResponseDto>
    createNotice(
            @RequestBody NoticeRequestDto request
    ) {

        return ResponseEntity.ok(
                noticeService.createNotice(request)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoticeResponseDto>
    updateNotice(
            @PathVariable Long id,
            @RequestBody NoticeRequestDto request
    ) {

        return ResponseEntity.ok(
                noticeService.updateNotice(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String>
    deleteNotice(
            @PathVariable Long id
    ) {

        noticeService.deleteNotice(id);

        return ResponseEntity.ok(
                "Notice deactivated successfully"
        );
    }

    @GetMapping
    public ResponseEntity<List<NoticeResponseDto>>
    getActiveNotices() {

        return ResponseEntity.ok(
                noticeService.getActiveNotices()
        );
    }
}