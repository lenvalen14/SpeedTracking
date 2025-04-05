package edu.ut.its.controllers;

import edu.ut.its.models.dtos.requests.AccountRegisterRequest;
import edu.ut.its.models.dtos.requests.AccountUpdateRequest;
import edu.ut.its.models.dtos.responses.AccountDetailResponse;
import edu.ut.its.responese.ResponseWrapper;
import edu.ut.its.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping()
    public ResponseEntity<ResponseWrapper<Page<AccountDetailResponse>>> getAllAccountDetail(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<AccountDetailResponse> responses = accountService.getAllAccounts(pageable);

        ResponseWrapper<Page<AccountDetailResponse>> responseWrapper;

        if(responses != null && !responses.isEmpty())
        {
            responseWrapper = new ResponseWrapper<>("Data Account Successfully", responses);
            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
        else
        {
            responseWrapper = new ResponseWrapper<>("No Data Account Found", null);
            return new ResponseEntity<>(responseWrapper, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{requestID}")
    public ResponseEntity<ResponseWrapper<AccountDetailResponse>> getAccountDetailByRequestID(
            @PathVariable String requestID)
    {
        AccountDetailResponse account = accountService.getAccountById(requestID);
        ResponseWrapper<AccountDetailResponse> responseWrapper;

        if (account != null)
        {
            responseWrapper = new ResponseWrapper<>("Data Account Successfully", account);
            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
        else
        {
            responseWrapper = new ResponseWrapper<>("No Data Account Found", null);
            return new ResponseEntity<>(responseWrapper, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    public ResponseEntity<ResponseWrapper<AccountDetailResponse>> createAccount(
            @RequestBody AccountRegisterRequest accountRegisterRequest)
    {
        try {
            AccountDetailResponse account = accountService.createAccount(accountRegisterRequest);

            ResponseWrapper<AccountDetailResponse> responseWrapper =
                    new ResponseWrapper<>("Create Account Successfully", account);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseWrapper);
        } catch (RuntimeException ex) {
            ResponseWrapper<AccountDetailResponse> responseWrapper =
                    new ResponseWrapper<>(ex.getMessage(), null);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
        }
    }

    @PutMapping("/{requestID}")
    public ResponseEntity<ResponseWrapper<AccountDetailResponse>> updateAccount(
            @RequestBody AccountUpdateRequest accountUpdateRequest,
            @PathVariable String requestID    )
    {
        try {
            AccountDetailResponse account = accountService.updateAccount(requestID, accountUpdateRequest);

            ResponseWrapper<AccountDetailResponse> responseWrapper =
                    new ResponseWrapper<>("Update Account Successfully", account);

            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
        catch (RuntimeException ex) {
            ResponseWrapper<AccountDetailResponse> responseWrapper =
                    new ResponseWrapper<>(ex.getMessage(), null);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
        }
    }

    @DeleteMapping("/{requestID}")
    public ResponseEntity<ResponseWrapper<Boolean>> deleteAccount(
            @PathVariable String requestID)
    {
        try {
            Boolean deleted = accountService.deleteAccount(requestID);

            ResponseWrapper<Boolean> responseWrapper =
                    new ResponseWrapper<>("Delete Account Successfully", deleted);

            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
        catch (RuntimeException ex) {
            ResponseWrapper<Boolean> responseWrapper =
                    new ResponseWrapper<>(ex.getMessage(), null);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseWrapper);
        }
    }
}
