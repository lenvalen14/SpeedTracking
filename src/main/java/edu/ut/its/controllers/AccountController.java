package edu.ut.its.controllers;

import edu.ut.its.components.JwtTokenUtils;
import edu.ut.its.models.dtos.TokenDTO;
import edu.ut.its.models.dtos.requests.AccountForgotPasswordRequest;
import edu.ut.its.models.dtos.requests.AccountLoginRequest;
import edu.ut.its.models.dtos.requests.AccountRegisterRequest;
import edu.ut.its.models.dtos.requests.AccountUpdateRequest;
import edu.ut.its.models.dtos.responses.AccountDetailResponse;
import edu.ut.its.models.entities.Account;
import edu.ut.its.models.entities.Token;
import edu.ut.its.models.enums.AccountRole;
import edu.ut.its.response.ResponseWrapper;
import edu.ut.its.services.AccountService;
import edu.ut.its.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/generate-secret-key")
    public ResponseEntity<String> generateSecretKey(){
        return ResponseEntity.ok(jwtTokenUtils.generateSecretKey());
    }

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

    @PostMapping("/register")
    public ResponseEntity<ResponseWrapper<AccountDetailResponse>> createAccount(
            @Valid @RequestBody AccountRegisterRequest accountRegisterRequest)
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

    @PostMapping("/login")
    public ResponseEntity<ResponseWrapper<?>> login( @RequestBody @Valid AccountLoginRequest accountLoginRequest) {
        try {
            String token = accountService.login(accountLoginRequest);

            if (token == null) {
                return new ResponseEntity<>(new ResponseWrapper<>("Login failed", null), HttpStatus.UNAUTHORIZED);
            }

            Account userDetail = accountService.getUserDetailsFromToken(token);

            if (userDetail == null) {
                return new ResponseEntity<>(new ResponseWrapper<>("Login failed", null), HttpStatus.UNAUTHORIZED);
            }

            Token jwt = tokenService.addToken(userDetail, token);
            TokenDTO tokenDTO = new TokenDTO();
            tokenDTO.setToken(jwt.getToken());
            tokenDTO.setRefreshToken(jwt.getRefreshToken());
            tokenDTO.setRefreshExpirationDate(jwt.getRefreshExpirationDate());
            tokenDTO.setExpirationDate(jwt.getExpirationDate());
            tokenDTO.setTokenId(jwt.getTokenId());
            List<AccountRole> roles = userDetail.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(AccountRole::valueOf)
                    .toList();

            tokenDTO.setRole(userDetail.getRole());
            tokenDTO.setTokenType(jwt.getTokenType());
            tokenDTO.setAccountId(userDetail.getAccountId());
            tokenDTO.setEmail(userDetail.getEmail());

            ResponseWrapper<TokenDTO> responseWrapper = new ResponseWrapper<>("Login successful", tokenDTO);
            return new ResponseEntity<>(responseWrapper, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();

            ResponseWrapper<String> responseWrapper = new ResponseWrapper<>("An error occurred during login", e.getMessage());
            return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{requestID}")
    public ResponseEntity<ResponseWrapper<AccountDetailResponse>> updateAccount(
            @Valid @RequestBody AccountUpdateRequest accountUpdateRequest,
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

    @PutMapping("/forgot-password/{email}")
    public ResponseEntity<ResponseWrapper<AccountDetailResponse>> forgotPassword(
            @PathVariable String email,
            @Valid @RequestBody AccountForgotPasswordRequest request
    ){
        try {
            AccountDetailResponse account = accountService.forgotPassword(email, request);

            ResponseWrapper<AccountDetailResponse> responseWrapper =
                    new ResponseWrapper<>("Reset password successfully", account);

            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        } catch (RuntimeException ex) {
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
