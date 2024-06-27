package com.example.sharediary.heart.service;

import com.example.sharediary.diary.domain.Diary;
import com.example.sharediary.diary.repository.DiaryRepository;
import com.example.sharediary.heart.domain.Heart;
import com.example.sharediary.heart.dto.request.HeartRequestDto;
import com.example.sharediary.heart.repository.HeartRepository;
import com.example.sharediary.member.domain.Member;
import com.example.sharediary.member.dto.request.LoginMemberResponseDto;
import com.example.sharediary.member.dto.request.TokenRequestDto;
import com.example.sharediary.member.dto.response.TokenResponseDto;
import com.example.sharediary.member.infrastructure.TokenProvider;
import com.example.sharediary.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;
    private final HeartRepository heartRepository;

    @Transactional
    public void insertHeart(final Long memberId, final Long diaryId) {

        // 사용자 이름을 통해 사용자 객체 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자."));
        // 다이어리 ID를 통해 다이어리 객체 조회
        final Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 다이어리입니다."));

        // 이미 좋아요를 누른 경우 예외 발생
        if (heartRepository.existsByMemberAndDiary(member, diary)) {
            throw new IllegalArgumentException("이미 좋아요를 누른 다이어리입니다.");
        }
        // 새로운 Heart 엔티티 생성 및 저장
        Heart heart = new Heart(member, diary);
        heartRepository.save(heart);
    }

    @Transactional
    public void deleteHeart(final Long memberId, final Long diaryId) {
        // 사용자 이름을 통해 사용자 객체 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자."));
        // 다이어리 ID를 통해 다이어리 객체 조회
        final Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 다이어리입니다."));

        if (!heartRepository.existsByMemberAndDiary(member, diary)) {
            throw new IllegalArgumentException("좋아요를 누르지 않은 다이어리입니다.");
        }
        heartRepository.deleteByMemberAndDiary(member, diary);

    }
}
