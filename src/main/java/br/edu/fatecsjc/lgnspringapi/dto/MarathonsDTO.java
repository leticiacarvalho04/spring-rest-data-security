package br.edu.fatecsjc.lgnspringapi.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.edu.fatecsjc.lgnspringapi.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MarathonsDTO {
	@Schema(hidden = true)
	private Long id;
	private String identification;
	private Double weight;
	private int score;
	private List<Member> members;
}
