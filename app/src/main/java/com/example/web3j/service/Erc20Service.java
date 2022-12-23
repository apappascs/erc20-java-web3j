package com.example.web3j.service;

import static com.example.web3j.service.Web3Provider.web3j;
import static org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.exceptions.ContractCallException;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

public class Erc20Service {

    private static final String FROM_ADDRESS = "0x0000000000000000000000000000000000000000";
    private static final ContractGasProvider contractGasProvider = new DefaultGasProvider();
    private static final TransactionManager readOnlyTransactionManager = new ReadonlyTransactionManager(web3j, FROM_ADDRESS);

    private static final ERC20 loadERC20Contract(final String contractAddress) {
        return ERC20.load(contractAddress, web3j, readOnlyTransactionManager, contractGasProvider);
    }

    public static String getName(final String contractAddress) throws Exception {
        return loadERC20Contract(contractAddress).name().send();
    }

    public static String getSymbol(final String contractAddress) throws Exception {
        return loadERC20Contract(contractAddress).symbol().send();
    }

    public static BigDecimal getTotalSupply(final String contractAddress) throws Exception {
        BigInteger totalSupply = loadERC20Contract(contractAddress).totalSupply().send();
        return Convert.fromWei(totalSupply.toString(), Convert.Unit.ETHER);
    }

    public static BigDecimal getBalanceOf(final String contractAddress, final String walletAddress) throws Exception {
        BigInteger balanceOf = loadERC20Contract(contractAddress).balanceOf(walletAddress).send();
        return Convert.fromWei(balanceOf.toString(), Convert.Unit.ETHER);
    }

    public static List<Type> ethCall(final String contractAddress, String name, List<Type> inputParameters, List<TypeReference<?>> outputParameters)
            throws IOException {
        final Function function = new Function(name, inputParameters, outputParameters);

        String encodedFunction = FunctionEncoder.encode(function);
        EthCall response = getEthCallRequest(contractAddress, encodedFunction).send();
        List<Type> result = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
        if (result.isEmpty()) {
            throw new ContractCallException("Empty value (0x) returned from contract");
        }
        return result;
    }

    private static Request<?, EthCall> getEthCallRequest(final String contractAddress, final String encodedFunction) {
        return web3j.ethCall(createEthCallTransaction(FROM_ADDRESS, contractAddress, encodedFunction), DefaultBlockParameterName.LATEST);
    }
}
