package br.edu.fatecsjc.lgnspringapi.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MarathonsDTO {
    private Long id;
    private String identification;
    private Double weight;
    private int score;
    private List<MemberDTO> members; 
}
