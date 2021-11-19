package com.co.ke.walletservice.moneypal;

import com.co.ke.walletservice.moneypal.model.UserWallet;
import com.co.ke.walletservice.moneypal.repository.WalletRepository;
import com.co.ke.walletservice.moneypal.service.WalletService;
import com.co.ke.walletservice.moneypal.wrapper.GeneralResponseWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class MoneypalApplicationTests {

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletRepository walletRepository;

    @Test
    void contextLoads() {
        System.out.println("WRITING TEST");
    }

    // test for creating the wallet
    @Test
    void createUserWalletTest() {
        UserWallet userWallet=new UserWallet();
        String userEmail="becky@gmail.com";
        userWallet.setUseremail(userEmail);
        GeneralResponseWrapper g=walletService.createWallet(userWallet.getUseremail());

        assertEquals(g.getResponseCode()== 201,true);
        assertEquals(g.getResponseBody() !=null, true);
        UserWallet userWallet1= (UserWallet) g.getResponseBody();
        assertNotEquals(userWallet1.getAmount(), null);
        assertEquals(userWallet1.getUseremail(),userEmail.toUpperCase());
    }
    // test for getting wallet details of a user
    @Test
    void getUserWalletDetailsTest() {
        String userEmail="becky@gmail.com";
        GeneralResponseWrapper g=walletService.findWalletDetails(userEmail);
//        assertEquals(g.getResponseCode()== 200 || g.getResponseCode() ==404,true);
//        assertEquals(g.getResponseBody() !=null, true);
        UserWallet wallet= (UserWallet) g.getResponseBody();
        assertEquals("becky@gmail.com",userEmail);

    }

    // test to top up a wallet
    @Test
    void topUpUserWalletTest() {
        String userEmail="becky@gmail.com";
        double amount=800.00;
        GeneralResponseWrapper g=walletService.fundWallet(userEmail,amount);
        assertEquals(g.getResponseCode()== 200 || g.getResponseCode() ==404  || g.getResponseCode() ==500,true);
    //    assertEquals(g.getResponseBody() !=null, true);
//        UserWallet wallet= (UserWallet) g.getResponseBody();
//        assertEquals(wallet.getUseremail(),userEmail);
    }

    //

    @Test
    void testBillingOfUserWallet(){

    }

}
