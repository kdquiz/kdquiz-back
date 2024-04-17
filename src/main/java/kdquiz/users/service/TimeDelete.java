package kdquiz.users.service;
//
import kdquiz.users.repository.EmailCheckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TimeDelete {
    private final EmailCheckRepository emailCheckRepository;
    @Scheduled(fixedRate = 120000) // 2분마다 실행
    @Transactional
    public void deleteOldData() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime timeThreshold = now.minusMinutes(5);
        System.out.println("삭제 실행2");
        // 5분 이상된 데이터 삭제
        emailCheckRepository.deleteByCreatedAtBefore(timeThreshold);
    }
}
