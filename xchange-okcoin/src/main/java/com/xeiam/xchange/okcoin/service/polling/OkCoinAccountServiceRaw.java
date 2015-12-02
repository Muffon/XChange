package com.xeiam.xchange.okcoin.service.polling;

import java.io.IOException;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.okcoin.dto.account.OkCoinFuturesUserInfoCross;
import com.xeiam.xchange.okcoin.dto.account.OkCoinUserInfo;

public class OkCoinAccountServiceRaw extends OKCoinBaseTradePollingService {

  /**
   * Constructor
   *
   * @param exchange
   */
  protected OkCoinAccountServiceRaw(Exchange exchange) {

    super(exchange);
  }

  public OkCoinUserInfo getUserInfo() throws IOException {

    OkCoinUserInfo userInfo = okCoin.getUserInfo(apikey, signatureCreator);

    return returnOrThrow(userInfo);
  }

  public OkCoinFuturesUserInfoCross getFutureUserInfo() throws IOException {

    //    OkCoinUserInfo userInfo = okCoin.getFuturesUserInfoFixed(apikey, signatureCreator);
    OkCoinFuturesUserInfoCross futuresUserInfoCross = okCoin.getFuturesUserInfoCross(apikey, signatureCreator);

    return returnOrThrow(futuresUserInfoCross);
  }
<<<<<<< HEAD
=======
    
  public OKCoinWithdraw withdraw(String assetPairs, Currency assets, String key, BigDecimal amount) throws IOException {
    OKCoinWithdraw withdrawResult = okCoin.withdraw(exchange.getExchangeSpecification().getApiKey(),
            assets.toString(), signatureCreator, "0.0001", tradepwd, key, amount.toString());
        
            
    return  returnOrThrow(withdrawResult);
  }
>>>>>>> parent of 5399dbf... - repair okcoin withdraw

}
