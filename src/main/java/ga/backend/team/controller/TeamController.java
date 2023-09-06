package ga.backend.team.controller;

import com.nimbusds.jose.shaded.json.JSONObject;
import ga.backend.company.dto.CompanyResponseDto;
import ga.backend.team.dto.TeamRequestDto;
import ga.backend.team.dto.TeamResponseDto;
import ga.backend.team.entity.Team;
import ga.backend.team.mapper.TeamMapper;
import ga.backend.team.service.TeamService;
import ga.backend.util.Version;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(Version.currentUrl + "/teams")
@Validated
@AllArgsConstructor
public class TeamController {
    private final TeamService teamService;
    private final TeamMapper teamMapper;

    // CREATE
    @PostMapping
    public ResponseEntity postTeam(@Valid @RequestBody TeamRequestDto.Post post) {
        Team team = teamService.createTeam(teamMapper.teamPostDtoToTeam(post));
        TeamResponseDto.Response response = teamMapper.teamToTeamResponseDto(team);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping
    public ResponseEntity getTeamList (@Positive @RequestParam(value = "pk", required = false) Long teamPk,
                                       @RequestParam(value = "name", required = false) String teamName) {
        List<Team> teams = teamService.findTeams(teamPk, teamName);

        // 응답
        List<TeamResponseDto.Response> teamResponse = teamMapper.teamToTeamListResponseDto(teams);
        JSONObject response = new JSONObject();
        response.put("teams", teamResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // UPDATE
    @PatchMapping("/{team-pk}")
    public ResponseEntity patchTeam(@Positive @PathVariable("team-pk") long teamPk,
                                        @Valid @RequestBody TeamRequestDto.Patch patch) {
        patch.setPk(teamPk);
        Team team = teamService.patchTeam(teamMapper.teamPatchDtoToTeam(patch));
        TeamResponseDto.Response response = teamMapper.teamToTeamResponseDto(team);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/{team-pk}")
    public ResponseEntity deleteTeam(@Positive @PathVariable("team-pk") long teamPk) {
        teamService.deleteTeam(teamPk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
