package edu.ut.its.mappers;

import edu.ut.its.models.dtos.requests.AccountRegisterRequest;
import edu.ut.its.models.dtos.requests.AccountUpdateRequest;
import edu.ut.its.models.dtos.responses.AccountDetailResponse;
import edu.ut.its.models.entities.Account;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDetailResponse toAccountDTO(Account account);

    Account toAccount(AccountRegisterRequest request);

    void updateAccountFromRequest(AccountUpdateRequest request, @MappingTarget Account account);

}
