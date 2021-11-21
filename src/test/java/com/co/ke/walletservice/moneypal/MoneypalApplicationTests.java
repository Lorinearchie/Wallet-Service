package com.co.ke.walletservice.moneypal;

import com.co.ke.walletservice.moneypal.repository.WalletRepository;
import com.co.ke.walletservice.moneypal.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MoneypalApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("WRITING TEST");
    }

    // test for creating the wallet
    @Test
    void createUserWalletTest() {
        System.out.println("TO BE REWRITTEN A FRESH");
    }
    // test for getting wallet details of a user
    @Test
    void getUserWalletDetailsTest() {
        System.out.println("TO BE REWRITTEN A FRESH");

    }

    // test to top up a wallet
    @Test
    void topUpUserWalletTest() {
        System.out.println("TO BE REWRITTEN A FRESH");
    }

    //

    @Test
    void testBillingOfUserWallet(){
        System.out.println("TO BE REWRITTEN A FRESH");
    }

}
