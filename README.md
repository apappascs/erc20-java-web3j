<a href="https://idx.google.com/import?url=https%3A%2F%2Fgithub.com%2Fapappascs%2Ferc20-java-web3j">
  <picture>
    <source
      media="(prefers-color-scheme: dark)"
      srcset="https://cdn.idx.dev/btn/open_dark_32.svg">
    <source
      media="(prefers-color-scheme: light)"
      srcset="https://cdn.idx.dev/btn/open_light_32.svg">
    <img
      height="32"
      alt="Open in IDX"
      src="https://cdn.idx.dev/btn/open_purple_32.svg">
  </picture>
</a>

# ERC-20 Java Web3j Service
This Java service utilizes [Web3j](https://web3j.io) to manage [ERC-20](https://eips.ethereum.org/EIPS/eip-20) tokens on the Ethereum network. Web3j is a Java implementation of the Web3 library, which allows for interaction with Ethereum nodes using various methods such as JSON-RPC, HTTP, WebSockets, and IPC. It is a type-safe and reactive library that simplifies working with smart contracts and integrating with Ethereum clients. Using this service, you can easily work with the Ethereum blockchain without the need to write integration code.

Detailed documentation can be found in this article: [Interacting with the Ethereum Network using Java and Web3j](https://medium.com/@apappascs/interacting-with-the-ethereum-network-using-java-and-web3j-cea2fe5d8aef)


## Installing
- Clone the repository: ```git clone https://github.com/apappascs/erc20-java-web3j.git```
- Navigate to the project directory: ```cd erc20-java-web3j```
- Build the project: ```./gradlew build```

## Usage
To set up the web3 provider using a public Ethereum RPC endpoint, use the following code:

```
public class Web3Provider {

    private static final String PUBLIC_RPC = "https://cloudflare-eth.com";
    public static final Web3j web3j = Web3j.build(new HttpService(PUBLIC_RPC));
}
```
You can then use the web3j object to interact with the Ethereum network, as described in the [Web3j documentation](https://docs.web3j.io).

If you are looking for a list of Ethereum providers and public endpoints, you can check out the [Awesome List of RPC Nodes and Providers](https://github.com/arddluma/awesome-list-rpc-nodes-providers). This resource provides a list of Ethereum providers and public Ethereum RPC endpoints that you can use to connect to the Ethereum network.




The ERC-20 Java Service provides several methods for interacting with ERC-20 tokens on the Ethereum network. Here are some examples of how to use these methods:

**getName(String contractAddress)**: This method returns the name of the ERC-20 token associated with the given contract address.
```
String name = Erc20Service.getName(CONTRACT_ADDRESS);
logger.info(String.format("ERC-20 Token with contract: %s, has name:", CONTRACT_ADDRESS, name));
```
**getSymbol(String contractAddress)**: This method returns the symbol of the ERC-20 token associated with the given contract address.
```
String symbol = Erc20Service.getSymbol(CONTRACT_ADDRESS);
logger.info(String.format("ERC-20 Token with contract: %s, has symbol:", CONTRACT_ADDRESS, symbol));
```
**getTotalSupply(String contractAddress)**: This method returns the total supply of the ERC-20 token associated with the given contract address.
```
BigDecimal totalSupply = Erc20Service.getTotalSupply(CONTRACT_ADDRESS);
logger.info(String.format("Max Total Supply: %s of ERC-20 Token: %s", erc20TotalSupply, CONTRACT_ADDRESS));
```
**getBalanceOf(String contractAddress, String owner)**: This method returns the balance of the ERC-20 token associated with the given contract address for the given owner.
```
BigDecimal balanceOf = Erc20Service.getBalanceOf(CONTRACT_ADDRESS, WALLET_ADDRESS);
logger.info(String.format("ERC-20 token balance: %s of wallet: %s ", erc20BalanceOf, WALLET_ADDRESS));
```
**ethCall(String contractAddress, Function function, List<Type> inputParameters, List<TypeReference<Type>> outputParameters):** This method allows you to call a method on a smart contract to query a value. There is no transaction cost associated with this function, this is because it does not change the state of any smart contract method's called, it simply returns the value from them.
```
List<Type> result = Erc20Service.ethCall(CONTRACT_ADDRESS, FUNC_TOTALSUPPLY, Arrays.asList(),
                Arrays.asList(new TypeReference<Uint256>() {}));
logger.info(String.format("Result Type: %s, Value: %s", result.get(0).getTypeAsString(), result.get(0).getValue()));
```
You can find all the examples in the Erc20ServiceTest class.

## Web3j - Querying the state of a smart contract
To [query the state of a smart contract using Web3j](https://docs.web3j.io/latest/transactions/transactions_and_smart_contracts/#querying-the-state-of-a-smart-contract), you can use the eth_call JSON-RPC method. This method allows you to call a method on a smart contract to query its state without creating a transaction on the blockchain.

To use eth_call, you will need to create a Function object that represents the method you want to call. You can do this by specifying the name of the method, a list of input Type objects representing the parameters of the method, and a list of TypeReference objects representing the return types of the method.

Once you have created the Function object, you can use the FunctionEncoder class to encode it into a hexadecimal string that can be used in the eth_call request. You can then use the web3j.ethCall method to send the eth_call request, specifying the Transaction object that represents the call, the contract address, and the encoded function as arguments.

To get the response from the eth_call request, you can call the sendAsync method and use the FunctionReturnDecoder class to decode the response value into a list of Type objects representing the return values of the function.

Here is an example of how to query the state of a smart contract using Web3j:

```
// Solidity Types in smart contract functions
List<Type> inputParameters = Arrays.asList(new Type(value));
List<TypeReference<Type>> outputParameters = Arrays.asList(new TypeReference<Type>() {});
Function function = new Function("functionName", inputParameters, outputParameters);
String encodedFunction = FunctionEncoder.encode(function);

EthCall response = web3j.ethCall(Transaction.createEthCallTransaction(fromWalletAddress, contractAddress, encodedFunction),DefaultBlockParameterName.LATEST).sendAsync().get();

List<Type> result = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
```
Note: If an invalid function call is made, or a null result is obtained, the return value will be an instance of Collections.emptyList().
