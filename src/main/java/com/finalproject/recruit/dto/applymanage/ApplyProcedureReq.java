package com.finalproject.recruit.dto.applymanage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplyProcedureReq {

    List<Long> applyIds;
    String procedure;
}
