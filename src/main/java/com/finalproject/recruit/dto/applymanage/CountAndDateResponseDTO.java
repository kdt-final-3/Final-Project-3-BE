package com.finalproject.recruit.dto.applymanage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CountAndDateResponseDTO {

    Long totalCount;
    Long todayCount;
    String process;
    String processFinish;
}
