package com.example.web3j.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;

class EthereumServiceTest {

    // Binance 33 address
    private static final String WALLET_ADDRESS = "0x50d669f43b484166680ecc3670e4766cdb0945ce";
    private static final Logger logger = Logger.getLogger(Erc20ServiceTest.class.getName());

    @Test
    void testGetEthChainId() throws Exception {
        BigInteger ethChainId = EthereumService.getEthChainId();
        logger.info(String.format("Ethereum chain id: %s", ethChainId));
        assertThat(ethChainId).isEqualTo(1);
    }

    @Test
    void testGetEthGasPrice() throws Exception {
        BigDecimal ethGasPrice = EthereumService.getEthGasPrice();
        logger.info(String.format("Ethereum Gas Price: %s", ethGasPrice));
        assertThat(ethGasPrice).isGreaterThan(BigDecimal.valueOf(0));
    }

    @Test
    void testGetEthGetBalance() throws Exception {
        BigDecimal ethBalance = EthereumService.getEthGetBalance(WALLET_ADDRESS);
        logger.info(String.format("Ethereum balance: %s of wallet: %s", ethBalance, WALLET_ADDRESS));
        assertThat(ethBalance).isGreaterThan(BigDecimal.valueOf(0));
    }
}