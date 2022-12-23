package com.example.web3j.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.web3j.contracts.eip20.generated.ERC20.FUNC_TOTALSUPPLY;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.tx.exceptions.ContractCallException;

class Erc20ServiceTest {

    // Binance 33 address
    private static final String WALLET_ADDRESS = "0x50d669f43b484166680ecc3670e4766cdb0945ce";
    private static final Logger logger = Logger.getLogger(Erc20ServiceTest.class.getName());
    // Information about Polygon Matic: https://etherscan.io/token/0x7d1afa7b718fb893db30a3abc0cfc608aacfebb0
    private static final long POLYGON_MAX_TOTAL_SUPPLY = 10000000000L;
    private static final String POLYGON_NAME = "Matic Token";
    private static final String POLYGON_SYMBOL = "MATIC";
    private static final String POLYGON_CONTRACT_ADDRESS = "0x7D1AfA7B718fb893dB30A3aBc0Cfc608AaCfeBB0";

    @Test
    void testGetName() throws Exception {
        String name = Erc20Service.getName(POLYGON_CONTRACT_ADDRESS);
        logger.info(String.format("ERC-20 Token with contract: %s, has name:", POLYGON_CONTRACT_ADDRESS, name));
        assertThat(name).isEqualTo(POLYGON_NAME);
    }

    @Test
    void testGetSymbol() throws Exception {
        String symbol = Erc20Service.getSymbol(POLYGON_CONTRACT_ADDRESS);
        logger.info(String.format("ERC-20 Token with contract: %s, has symbol:", POLYGON_CONTRACT_ADDRESS, symbol));
        assertThat(symbol).isEqualTo(POLYGON_SYMBOL);
    }

    @Test
    void testGetTotalSupply() throws Exception {
        BigDecimal totalSupply = Erc20Service.getTotalSupply(POLYGON_CONTRACT_ADDRESS);
        logger.info(String.format("Max Total Supply: %s of ERC-20 Token: %s", totalSupply, POLYGON_CONTRACT_ADDRESS));
        assertThat(totalSupply).isEqualTo(BigDecimal.valueOf(POLYGON_MAX_TOTAL_SUPPLY));
    }

    @Test
    void testGetBalanceOf() throws Exception {
        BigDecimal balanceOf = Erc20Service.getBalanceOf(POLYGON_CONTRACT_ADDRESS, WALLET_ADDRESS);
        logger.info(String.format("ERC-20 token balance: %s of wallet: %s ", balanceOf, WALLET_ADDRESS));
        assertThat(balanceOf).isGreaterThan(BigDecimal.valueOf(0));
    }

    @Test
    void testEthCall() throws Exception {
        List<Type> result = Erc20Service.ethCall(POLYGON_CONTRACT_ADDRESS, FUNC_TOTALSUPPLY, Arrays.asList(),
                Arrays.asList(new TypeReference<Uint256>() {}));
        logger.info(String.format("Result Type: %s, Value: %s", result.get(0).getTypeAsString(), result.get(0).getValue()));
        assertThat(result).isNotEmpty();
    }

    @Test
    void testEthCallShouldThrowException() {
        ContractCallException exception = Assertions.assertThrows(ContractCallException.class,
                () -> Erc20Service.ethCall(POLYGON_CONTRACT_ADDRESS, String.format("totalSupply%s", UUID.randomUUID()), Arrays.asList(),
                        Arrays.asList(new TypeReference<Uint256>() {})));

        Assertions.assertEquals("Empty value (0x) returned from contract", exception.getMessage());
    }
}