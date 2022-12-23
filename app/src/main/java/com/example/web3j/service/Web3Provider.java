package com.example.web3j.service;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

public class Web3Provider {

    private static final String PUBLIC_RPC = "https://cloudflare-eth.com";
    public static final Web3j web3j = Web3j.build(new HttpService(PUBLIC_RPC));
}
