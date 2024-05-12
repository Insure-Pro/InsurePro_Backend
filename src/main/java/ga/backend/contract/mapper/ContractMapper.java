package ga.backend.contract.mapper;

import ga.backend.contract.dto.*;
import ga.backend.contract.entity.Contract;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContractMapper {
    Contract contractPostDtoToContract(ContractRequestDto.Post post);
    Contract contractPatchDtoToContract(ContractRequestDto.Patch patch);
    ContractResponseDto.Response contractToContractResponseDto(Contract contract);
    List<ContractResponseDto.Response> contractsToContractResponseDtos(List<Contract> contracts);
}