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

import br.edu.fatecsjc.lgnspringapi.dto.MemberDTO;
import br.edu.fatecsjc.lgnspringapi.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/member")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Member and Marathons")
@SecurityRequirement(name = "bearerAuth")
public class MemberResource {
	@Autowired
    private MemberService memberService;

    @GetMapping
    @Operation(
            description = "Get all members and marathons",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized/Invalid token", responseCode = "403"),
                    @ApiResponse(description = "Unknown error", responseCode = "400"),
            }
    )
    public ResponseEntity<List<MemberDTO>> getAllGroups() {
        return ResponseEntity.ok(memberService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(
            description = "Get a members and marathons by member ID",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized/Invalid token", responseCode = "403"),
                    @ApiResponse(description = "Unknown error", responseCode = "400"),
            }
    )
    public ResponseEntity<MemberDTO> getGroupById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    @Operation(
            description = "Update a member and marathons by member ID",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "201"),
                    @ApiResponse(description = "Unauthorized/Invalid token", responseCode = "403"),
                    @ApiResponse(description = "Unknown error", responseCode = "400"),
            }
    )
    public ResponseEntity<MemberDTO> update(@PathVariable Long id, @RequestBody MemberDTO body) {
        return ResponseEntity.status(HttpStatusCode.valueOf(201))
                .body(memberService.save(id, body));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    @Operation(
            description = "Register a new member and marathons",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "201"),
                    @ApiResponse(description = "Unauthorized/Invalid token", responseCode = "403"),
                    @ApiResponse(description = "Unknown error", responseCode = "400"),
            }
    )
    public ResponseEntity<MemberDTO> register(@RequestBody MemberDTO body) {
        return ResponseEntity.status(HttpStatusCode.valueOf(201))
                .body(memberService.save(body));
    }

    @DeleteMapping ("/{id}")
    @Operation(
            description = "Delete a member and marathons by member ID",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "204"),
                    @ApiResponse(description = "Unauthorized/Invalid token", responseCode = "403"),
                    @ApiResponse(description = "Unknown error", responseCode = "400"),
            }
    )
    public ResponseEntity<Void> update(@PathVariable Long id) {
        memberService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
