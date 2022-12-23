package com.example.web3j.service;

import static com.example.web3j.service.Web3Provider.web3j;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthChainId;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.utils.Convert;

public class EthereumService {

    public static BigInteger getEthChainId() throws Exception {
        EthChainId ethChainId = web3j.ethChainId().send();
        return ethChainId.getChainId();
    }

    public static BigDecimal getEthGasPrice() throws Exception {
        EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
        return Convert.fromWei(ethGasPrice.getGasPrice().toString(), Convert.Unit.GWEI);
    }

    public static BigDecimal getEthGetBalance(final String walletAddress) throws Exception {
        EthGetBalance ethBalance = web3j.ethGetBalance(walletAddress, DefaultBlockParameterName.LATEST).send();
        return Convert.fromWei(ethBalance.getBalance().toString(), Convert.Unit.ETHER);
    }
}
