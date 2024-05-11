package ga.backend.contract.controller;

import ga.backend.contract.dto.ContractRequestDto;
import ga.backend.contract.dto.ContractResponseDto;
import ga.backend.contract.entity.Contract;
import ga.backend.contract.mapper.ContractMapper;
import ga.backend.contract.service.ContractService;
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
@RequestMapping(Version.currentUrl)
@Validated
@AllArgsConstructor
public class ContractController {
    private final ContractService contractService;
    private final ContractMapper contractMapper;

    // CREATE
    @PostMapping("/contract")
    public ResponseEntity postContract(@Valid @RequestBody ContractRequestDto.Post post,
                                       @RequestParam(value = "customerPk", required = false) Long customerPk,
                                       @RequestParam(value = "schedulePk", required = false) Long schedulePk) {
        Contract contract = contractMapper.contractPostDtoToContract(post);
        contract = contractService.createContract(contract, customerPk, schedulePk);
        ContractResponseDto.Response response = contractMapper.contractToContractResponseDto(contract);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/contract/{contract-pk}")
    public ResponseEntity getContract(@Positive @PathVariable("contract-pk") long contractPk) {
        Contract contract = contractService.findContract(contractPk);
        ContractResponseDto.Response response = contractMapper.contractToContractResponseDto(contract);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Customer 및 schedule별 contract 리스트 조회
    @GetMapping("/contracts")
    public ResponseEntity getContracts(@RequestParam(value = "customerPk", required = false) Long customerPk,
                                       @RequestParam(value = "schedulePk", required = false) Long schedulePk) {
        List<Contract> contracts;
        if (customerPk != null) contracts = contractService.findContractByCustomerPk(customerPk);
        else contracts = contractService.findContractBySchedulePk(schedulePk);
        List<ContractResponseDto.Response> responses = contractMapper.contractsToContractResponseDtos(contracts);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    //  추가 가능한 contract 리스트 조회
    @GetMapping("/contracts/addable")
    public ResponseEntity getContracts(@RequestParam(value = "customerPk", required = false) Long customerPk) {
        List<Contract> contracts = contractService.findContractAddable(customerPk);
        List<ContractResponseDto.Response> responses = contractMapper.contractsToContractResponseDtos(contracts);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // UPDATE
    @PatchMapping("/contract/{contract-pk}")
    public ResponseEntity patchContract(@Positive @PathVariable("contract-pk") long contractPk,
                                           @Valid @RequestBody ContractRequestDto.Patch patch) {
        patch.setPk(contractPk);
        Contract contract = contractService.patchContract(
                contractMapper.contractPatchDtoToContract(patch),
                patch.getCustomerPk(),
                patch.getSchedulePk()
        );
        ContractResponseDto.Response response = contractMapper.contractToContractResponseDto(contract);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/contract/{contract-pk}")
    public ResponseEntity deleteContract(@Positive @PathVariable("contract-pk") long contractPk) {
        contractService.deleteContract(contractPk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // schedule에서 contract 삭제
    @DeleteMapping("/contract/{contract-pk}/schedule")
    public ResponseEntity deleteContractBySchedulePk(@Positive @PathVariable("contract-pk") long contractPk) {
        contractService.deleteContractBySchedule(contractPk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
