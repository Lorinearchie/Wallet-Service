package com.co.ke.walletservice.moneypal.repository;

import com.co.ke.walletservice.moneypal.model.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<UserWallet, Long> {
    Optional<UserWallet> findByUseremail(String email);
}
