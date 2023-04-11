package com.finalproject.recruit.service;

import com.finalproject.recruit.dto.applymanage.ApplyResponseDTO;
import com.finalproject.recruit.exception.applyManage.ApplyManageException;
import com.finalproject.recruit.parameter.ApplyProcedure;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

@ExtendWith(MockitoExtension.class)
class CalcKeywordTest {

    @InjectMocks
    private ApplyManageService applyManageService;


    /**
     * 키워드가 정상적으로 들어왔을 때
     * */
    @Test
    public void calcScore(){
        ApplyResponseDTO dto = new ApplyResponseDTO(
                1L,
                "지원자1",
                "010-1111-2222",
                "apply@test.com",
                ApplyProcedure.서류제출,
                true,
                false,
                LocalDateTime.now(),
                false,
                true,
                "잘 웃어요,꼼꼼해요,집이 가까워요,통찰력이 좋아요,설명을 잘해요",
                0,
                List.of()
        );


        HashSet<String> keywordStandard= new HashSet<>(Arrays.asList("잘 웃어요","센스 좋아요","집이 가까워요","결근 안해요","일 잘해요"));


        applyManageService.calcKeywordScore(dto, keywordStandard);


        then(dto.getScore()).isEqualTo(2);
        then(dto.getKeywordList()).isEqualTo(List.of("잘 웃어요","꼼꼼해요","집이 가까워요","통찰력이 좋아요","설명을 잘해요"));
    }

    /**
     * 키워드가 빈문자열 일때
     * */
    @Test
    public void noKeyWordCalcScore(){
        ApplyResponseDTO dto = new ApplyResponseDTO(
                1L,
                "지원자1",
                "010-1111-2222",
                "apply@test.com",
                ApplyProcedure.서류제출,
                true,
                false,
                LocalDateTime.now(),
                false,
                true,
                "",
                0,
                List.of()
        );


        HashSet<String> keywordStandard= new HashSet<>(Arrays.asList("잘 웃어요","센스 좋아요","집이 가까워요","결근 안해요","일 잘해요"));


        applyManageService.calcKeywordScore(dto, keywordStandard);


        then(dto.getScore()).isEqualTo(0);
        then(dto.getKeywordList()).isEqualTo(List.of(""));
    }

    /**
     * 키워드가 null 일때
     * */
    @Test
    public void calcKeywordException(){
        ApplyResponseDTO dto = new ApplyResponseDTO(
                1L,
                "지원자1",
                "010-1111-2222",
                "apply@test.com",
                ApplyProcedure.서류제출,
                true,
                false,
                LocalDateTime.now(),
                false,
                true,
                null,
                0,
                List.of()
        );


        HashSet<String> keywordStandard= new HashSet<>(Arrays.asList("잘 웃어요","센스 좋아요","집이 가까워요","결근 안해요","일 잘해요"));

        thenThrownBy(() -> applyManageService.calcKeywordScore(dto, keywordStandard))
                .isInstanceOf(ApplyManageException.class);

    }
}