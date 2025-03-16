package br.edu.fatecsjc.lgnspringapi.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.edu.fatecsjc.lgnspringapi.entity.Group;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrganizationDTO {
	@Schema(hidden = true)
	private Long id;
	private String name;
	private String number;
	private String street;
	private String neighborhood;
	private String CEP;
	private String municipality;
	private String state;
	private String institutionName;
	private String hostCountry;
	private List<Group> group;
}
