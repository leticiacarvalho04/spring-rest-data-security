package br.edu.fatecsjc.lgnspringapi.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.fatecsjc.lgnspringapi.dto.MarathonsDTO;
import br.edu.fatecsjc.lgnspringapi.service.MarathonsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/marathons")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Marathons and Members")
@SecurityRequirement(name = "bearerAuth")
public class MarathonsResource {
	@Autowired
	private MarathonsService marathonService;
	
	@GetMapping
	@Operation (
			description = "Get all marathons and members",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized/Invalid token", responseCode = "403"),
                    @ApiResponse(description = "Unknown error", responseCode = "400"),
			}
		)
	public ResponseEntity<List<MarathonsDTO>> getAllMarathons() {
		return ResponseEntity.ok(marathonService.getAll());
	}
	
	@GetMapping("/{id}")
	@Operation (
			description = "Get a marathons and members by group ID",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized/Invalid token", responseCode = "403"),
                    @ApiResponse(description = "Unknown error", responseCode = "400"),
			}
		)
	public ResponseEntity<MarathonsDTO> getMarathonsById(@PathVariable Long id) {
		return ResponseEntity.ok(marathonService.findById(id));
	}
	
	@PutMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    @Operation(
            description = "Update a group and members by group ID",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "201"),
                    @ApiResponse(description = "Unauthorized/Invalid token", responseCode = "403"),
                    @ApiResponse(description = "Unknown error", responseCode = "400"),
            }
    )
	public ResponseEntity<MarathonsDTO> update(@PathVariable Long id, @RequestBody MarathonsDTO body){
		return ResponseEntity.status(HttpStatusCode.valueOf(201))
				.body(marathonService.save(id, body));
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('admin:create')")
    @Operation(
            description = "Register a new marathon and members",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "201"),
                    @ApiResponse(description = "Unauthorized/Invalid token", responseCode = "403"),
                    @ApiResponse(description = "Unknown error", responseCode = "400"),
            }
    )
	public ResponseEntity<MarathonsDTO> register(@RequestBody MarathonsDTO body){
		return ResponseEntity.status(HttpStatusCode.valueOf(201))
				.body(marathonService.save(body));
	}
	
	@DeleteMapping ("/{id}")
    @Operation(
            description = "Delete a group and members by group ID",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "204"),
                    @ApiResponse(description = "Unauthorized/Invalid token", responseCode = "403"),
                    @ApiResponse(description = "Unknown error", responseCode = "400"),
            }
    )
	public ResponseEntity<Void> update(@PathVariable Long id){
		marathonService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
