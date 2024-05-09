package ga.backend.contract.service;

import ga.backend.contract.entity.Contract;
import ga.backend.contract.repository.ContractRepository;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ContractService {
    private final ContractRepository contractRespository;

    // CREATE
    public Contract createContract(Contract contract) {
        return contractRespository.save(contract);
    }

    // READ
    public Contract findContract(long contractPk) {
        Contract contract = verifiedContract(contractPk);
        return contract;
    }

    // UPDATE
    public Contract patchContract(Contract contract) {
        Contract findContract = verifiedContract(contract.getPk());
        Optional.ofNullable(contract.getName()).ifPresent(findContract::setName);
        Optional.ofNullable(contract.getMemo()).ifPresent(findContract::setMemo);
        Optional.ofNullable(contract.getContractDate()).ifPresent(findContract::setContractDate);
        Optional.ofNullable(contract.getDelYn()).ifPresent(findContract::setDelYn);
        return contractRespository.save(findContract);
    }

    // DELETE
    public void deleteContract(long contractPk) {
        Contract contract = verifiedContract(contractPk);
        contractRespository.delete(contract);
    }

    // 검증
    public Contract verifiedContract(long contractPk) {
        Optional<Contract> contract = contractRespository.findById(contractPk);
        return contract.orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMPANY_NOT_FOUND));
    }
}
