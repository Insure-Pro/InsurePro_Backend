package ga.backend.team.mapper;

import ga.backend.team.dto.TeamRequestDto;
import ga.backend.team.dto.TeamResponseDto;
import ga.backend.team.entity.Team;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeamMapper {
    Team teamPostDtoToTeam(TeamRequestDto.Post post);
    Team teamPatchDtoToTeam(TeamRequestDto.Patch patch);
    TeamResponseDto.Response teamToTeamResponseDto(Team team, Long companyPk);
    List<TeamResponseDto.Response> teamToTeamListResponseDto(List<Team> teams);
}