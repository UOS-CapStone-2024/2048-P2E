package com.example.p2e.src.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetLoginRes {
    private String nickname;
    private int ranking;
    private int point;
    private int revive;
    private int deleteblock;
}