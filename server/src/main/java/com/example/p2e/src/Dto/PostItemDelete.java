package com.example.p2e.src.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostItemDelete {
    private String web3;
    private int deleteblock;
}