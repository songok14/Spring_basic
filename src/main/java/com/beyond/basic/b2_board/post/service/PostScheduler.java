package com.beyond.basic.b2_board.post.service;

import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@Transactional
public class PostScheduler {
    private final PostRepository postRepository;

    @Autowired
    public PostScheduler(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // 스케줄러 서버를 2대 이상 운영하게 될 경우 중복 데이터가 발생할 수 있어
    // 레디스 등의 솔루션을 사용하여 스케줄 제어

    // cron의 각 자리는 "초 분 시간 일 월 요일"을 의미
    // * * * * * * 매월 매일 매시간 매분 매초 마다
    // 0 0 * * * * 매월 매일 매시간 0분 0초 마다
    // 0 0 11 * * * 매월 매일 11시 0분 0초 마다
    // 0 0/1 * * * * 매월, 매일, 매시, 1분 마다
    @Scheduled(cron = "0 0/5 * * * *")
    public void postSchedule() {
        log.info(("=====예약 스케줄러 시작====="));
        List<Post> postList = postRepository.findByAppointment("Y");
        LocalDateTime now = LocalDateTime.now();
        for (Post p : postList) {
            if (p.getAppointmentTime().isBefore(now)) {
                p.updateAppointment("N");
            }
        }
        log.info(("=====예약 스케줄러 끝====="));
    }
}
