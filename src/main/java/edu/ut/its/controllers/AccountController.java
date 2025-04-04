package edu.ut.its.controllers;

import edu.ut.its.models.dtos.responses.AccountDetailResponse;
import edu.ut.its.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

}
