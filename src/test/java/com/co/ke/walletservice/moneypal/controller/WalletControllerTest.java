package com.co.ke.walletservice.moneypal.controller;

import com.co.ke.walletservice.moneypal.model.UserWallet;
import com.co.ke.walletservice.moneypal.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@WebMvcTest
//@SpringBootTest
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletRepository walletRepository;

    @Test
    void createwallet() throws Exception {
        String email="hhhhhh@gmail.com";
        RequestBuilder requestBuilder= MockMvcRequestBuilders.post("/wallet/create/"+email)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result=mockMvc.perform(requestBuilder).andReturn();  // perform the builfd then return the result
        assertEquals(200,result.getResponse().getStatus());// expect status as 200
    }

    @Test
    void wallet() {
    }

    @Test
    void walletDetails() throws Exception {
//        String email="";
//       //GIVEN
//        Mockito.when(walletRepository.findByUseremail(email));
//        //WHEN
//        mockMvc.
//                perform(MockMvcRequestBuilders.get("/wallet/details/" + email)
//                        .accept(MediaType.APPLICATION_JSON)
//                ).andExpect(status().isOk());
//        //THEN
//        verify(walletRepository.findByUseremail(email));
        System.out.println("TO BE COMPLETED");

    }

    @Test
    void walletBilling() {
        System.out.println("TO BE COMPLETED");
    }
}