package com.co.ke.walletservice.moneypal.service;

import com.co.ke.walletservice.moneypal.model.UserWallet;
import com.co.ke.walletservice.moneypal.repository.WalletRepository;
import com.co.ke.walletservice.moneypal.wrapper.Body1Response;
import com.co.ke.walletservice.moneypal.wrapper.EmailResponseWrapper;
import com.co.ke.walletservice.moneypal.wrapper.GeneralResponseWrapper;
import com.co.ke.walletservice.moneypal.wrapper.Wrapper2;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${userServiceUrl}")
    public String userServiceUrl;

    @Value("${emailServiceUrl}")
    public String emailServiceUrl;

    public GeneralResponseWrapper createWallet(String email) {
        GeneralResponseWrapper generalResponseWrapper = new GeneralResponseWrapper();
        try {

            UserWallet wallet = new UserWallet();
            wallet.setUseremail(email.trim().toUpperCase());
            wallet.setAmount(0.0);
            walletRepository.save(wallet);
            generalResponseWrapper.setResponseBody(wallet);
            generalResponseWrapper.setResponseCode(201);
            generalResponseWrapper.setResponseMessage("Wallet created successfully");
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
            generalResponseWrapper.setResponseCode(500);
            generalResponseWrapper.setResponseMessage("Internal Server Error");

        }
        return generalResponseWrapper;
    }

    public GeneralResponseWrapper fundWallet(String email, double amount) {
        GeneralResponseWrapper generalResponseWrapper = new GeneralResponseWrapper();
        try {
            log.info("Check if User with that ID exist");
            //   String url=userServiceUrl + "/validateUserName/" + userName;
            String url = userServiceUrl + "/getUser/" + email;
            //  HttpEntity <String> entity = new HttpEntity<String>();
            ResponseEntity<Wrapper2> generalResponseWrapper1 = restTemplate.getForEntity(url, Wrapper2.class);
            int res = generalResponseWrapper1.getStatusCodeValue();
            if (res == 200 && generalResponseWrapper1.getBody() != null) {
                //  generalResponseWrapper=generalResponseWrapper1.getBody();
                Wrapper2 wrapper2 = generalResponseWrapper1.getBody();
                Body1Response b1 = wrapper2.getResponseBody();
                //  Optional<UserWallet> userWallet=walletRepository.findByUserid2(b1.getId().toString());
                Optional<UserWallet> userWallet = walletRepository.findByUseremail(email.trim().toUpperCase());
                if (userWallet.isPresent()) {
                    System.out.println("wallet found");
                    UserWallet wallet = userWallet.get();
                    double newAmount = wallet.getAmount() + amount;
                    wallet.setAmount(newAmount);
                    walletRepository.save(wallet);


                    // send email to the user after the account has been funded
                    String emailTo = email;
                    String messageBody = "Dear : " + email + ",\n You have Deposited :" + amount +
                            "in your wallet.\n Your wallet balance is :" + newAmount
                            + ".\nThank you for using MoneyPal.";

                    ResponseEntity<EmailResponseWrapper> emailResponse = restTemplate.getForEntity(emailServiceUrl + emailTo + "/" + messageBody, EmailResponseWrapper.class);
                    if (emailResponse.getStatusCodeValue() == 200 && generalResponseWrapper1.getBody() != null) {
                        generalResponseWrapper.setResponseMessage("Email Sent.User Walled Updated Successfully, New Wallet Amount : " + newAmount);
                    } else {
                        generalResponseWrapper.setResponseMessage("Email Sending Failed.User Walled Updated Successfully, New Wallet Amount : " + newAmount);

                    }

                    generalResponseWrapper.setResponseBody(wallet);
                    generalResponseWrapper.setResponseCode(200);
                } else {
                    generalResponseWrapper.setResponseBody(email);
                    generalResponseWrapper.setResponseCode(404);
                    generalResponseWrapper.setResponseMessage("User Wallet NOT  Found");
                }

            } else {
                generalResponseWrapper.setResponseCode(400);
                generalResponseWrapper.setResponseMessage("Validation for the User Failed");
            }

        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
            generalResponseWrapper.setResponseCode(500);
            generalResponseWrapper.setResponseMessage("Internal Server Error");

        }
        return generalResponseWrapper;

    }


    public GeneralResponseWrapper billingTotheWallet(String emailFrom, String emailTo, float amount) {
        GeneralResponseWrapper generalResponseWrapper = new GeneralResponseWrapper();
        try {
            Optional<UserWallet> userWallet = walletRepository.findByUseremail(emailFrom.trim().toUpperCase());
            Optional<UserWallet> userWallet1 = walletRepository.findByUseremail(emailTo.trim().toUpperCase());
            if (userWallet.isPresent() && userWallet1.isPresent()) {
                UserWallet wallet = userWallet.get();
                double newAmount = wallet.getAmount() - amount;
                wallet.setAmount(newAmount);
                walletRepository.save(wallet);
                String emailToSend = "Dear Customer,\nYou have Sent : " + amount + " to : " +emailTo+".\nYour Wallet Balance is : " + newAmount;
                ResponseEntity<EmailResponseWrapper> emailResponse = restTemplate.getForEntity(emailServiceUrl + emailTo + "/" + emailToSend, EmailResponseWrapper.class);
                if (emailResponse.getStatusCodeValue() == 200 && emailResponse.getBody() != null) {
                    System.out.println("Email sent to Sender of the Amount,Billing successful");                }
                else {
                    System.out.println("Email NOT sent to Sender of the Amount,Billing successful");

                }
                UserWallet wallet2 = userWallet1.get();
                double newAmount2 = wallet2.getAmount() + amount;
                wallet2.setAmount(newAmount2);
                walletRepository.save(wallet2);
                String emailToSend3= "Dear Customer,\nYou have Received : " + amount + " From : " +emailFrom+".\nYour Wallet Balance is : " + newAmount2;
                ResponseEntity<EmailResponseWrapper> emailResponse2 = restTemplate.getForEntity(emailServiceUrl + emailTo + "/" + emailToSend3, EmailResponseWrapper.class);
                if (emailResponse2.getStatusCodeValue() == 200 && emailResponse2.getBody() != null) {
                    System.out.println("Email sent to Receiver,Billing Successful");
                } else {
                    System.out.println("Email NOT sent to Receiver,Billing successful");

                }
                List<UserWallet> wallets = Stream.of(wallet, wallet2).collect(Collectors.toList());
                ;
                generalResponseWrapper.setResponseBody(wallets);
                generalResponseWrapper.setResponseCode(200);
                generalResponseWrapper.setResponseMessage("Wallet Billing Operation Successful");
            } else {
                //deducting the wallet failed
                boolean one = userWallet.isPresent();
                boolean two = userWallet1.isPresent();
                generalResponseWrapper.setResponseCode(400);
                String message = null;
                String message1 = null;
                if (!one) message = "Wallet not Found for : " + emailFrom;
                if (!two) message1 = "Wallet not Found for : " + emailTo;
                generalResponseWrapper.setResponseMessage(message + "." + message1 + " .Billing Operation on Wallet Failed");
            }

        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
            generalResponseWrapper.setResponseCode(500);
            generalResponseWrapper.setResponseMessage("Internal Server Error");

        }
        return generalResponseWrapper;

    }

    public GeneralResponseWrapper findWalletDetails(String emailAddress) {
        GeneralResponseWrapper generalResponseWrapper = new GeneralResponseWrapper();
        try {
            log.info("Check WALLET BALANCE");
            Optional<UserWallet> wallet = Optional.of(walletRepository.findByUseremail(emailAddress)).get();
            if (wallet.isPresent()) {
                UserWallet userWallet = wallet.get();
                generalResponseWrapper.setResponseCode(200);
                generalResponseWrapper.setResponseMessage("Wallet Details Successfully fetched");
                generalResponseWrapper.setResponseBody(userWallet);
            } else {
                generalResponseWrapper.setResponseCode(404);
                generalResponseWrapper.setResponseMessage("Wallet Not Found for the user");
            }

        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
            generalResponseWrapper.setResponseCode(500);
            generalResponseWrapper.setResponseMessage("Internal Server Error");

        }
        return generalResponseWrapper;
    }
}
