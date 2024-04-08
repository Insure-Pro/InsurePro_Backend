package ga.backend.team.mapper;

import ga.backend.team.dto.TeamRequestDto;
import ga.backend.team.dto.TeamResponseDto;
import ga.backend.team.entity.Team;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TeamMapper {
    Team teamPostDtoToTeam(TeamRequestDto.Post post);
    Team teamPatchDtoToTeam(TeamRequestDto.Patch patch);
    TeamResponseDto.Response teamToTeamResponseDto(Team team, Long companyPk);
    default List<TeamResponseDto.Response> teamToTeamListResponseDto(List<Team> teams) {
        List<TeamResponseDto.Response> responses = new ArrayList<>();
        for(Team team : teams) responses.add(teamToTeamResponseDto(team, team.getCompany().getPk()));
        return responses;
    }
}