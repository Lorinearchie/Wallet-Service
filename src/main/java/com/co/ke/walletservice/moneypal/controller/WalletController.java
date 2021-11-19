package com.co.ke.walletservice.moneypal.controller;

import com.co.ke.walletservice.moneypal.service.WalletService;
import com.co.ke.walletservice.moneypal.wrapper.GeneralResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/create/{email}")
    public ResponseEntity<GeneralResponseWrapper> createwallet(@PathVariable String email){
        GeneralResponseWrapper generalResponseWrapper=walletService.createWallet(email);
        return ResponseEntity.ok().body(generalResponseWrapper);
    }

    @GetMapping("/update/{email}/{amount}")
    public ResponseEntity<GeneralResponseWrapper> wallet(@PathVariable String email,@PathVariable float amount){
        GeneralResponseWrapper generalResponseWrapper=walletService.fundWallet(email,amount);
        return ResponseEntity.ok().body(generalResponseWrapper);
    }

    @GetMapping("/details/{emailAddress}")
    public ResponseEntity<GeneralResponseWrapper> walletDetails(@PathVariable String emailAddress){
        GeneralResponseWrapper generalResponseWrapper=walletService.findWalletDetails(emailAddress);
        return ResponseEntity.ok().body(generalResponseWrapper);
    }

    @GetMapping("/wallet-billing/{emailAddress1}/{emailAddress2}/{amount}")
    public ResponseEntity<GeneralResponseWrapper> walletBilling(@PathVariable String emailAddress1,@PathVariable String emailAddress2,@PathVariable float amount){
        GeneralResponseWrapper generalResponseWrapper=walletService.billingTotheWallet(emailAddress1,emailAddress2,amount);
        return ResponseEntity.ok().body(generalResponseWrapper);
    }

}
