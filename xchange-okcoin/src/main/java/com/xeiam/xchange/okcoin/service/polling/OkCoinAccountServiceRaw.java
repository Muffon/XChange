package com.xeiam.xchange.okcoin.service.polling;

import java.io.IOException;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.currency.Currency;
import com.xeiam.xchange.okcoin.OkCoinAdapters;
import com.xeiam.xchange.okcoin.dto.account.OkCoinFuturesUserInfoCross;
import com.xeiam.xchange.okcoin.dto.account.OkCoinUserInfo;
import com.xeiam.xchange.okcoin.dto.account.OKCoinWithdraw;
import java.math.BigDecimal;

public class OkCoinAccountServiceRaw extends OKCoinBaseTradePollingService {
    private final String tradepwd;

  /**
   * Constructor
   *
   * @param exchange
   */
  protected OkCoinAccountServiceRaw(Exchange exchange) {

    super(exchange);
    
    tradepwd = (String) exchange.getExchangeSpecification().getExchangeSpecificParametersItem("tradepwd");    
  }

  public OkCoinUserInfo getUserInfo() throws IOException {

    OkCoinUserInfo userInfo = okCoin.getUserInfo(apikey, signatureCreator);

    return returnOrThrow(userInfo);
  }

  public OkCoinFuturesUserInfoCross getFutureUserInfo() throws IOException {
    
    OkCoinFuturesUserInfoCross futuresUserInfoCross = okCoin.getFuturesUserInfoCross(apikey, signatureCreator);

    return returnOrThrow(futuresUserInfoCross);
  }
    
  public OKCoinWithdraw withdraw(String assetPairs, Currency assets, String key, BigDecimal amount) throws IOException {
    OKCoinWithdraw withdrawResult = okCoin.withdraw(exchange.getExchangeSpecification().getApiKey(),
            assets.toString(), signatureCreator, "0.0001", tradepwd, key, amount.toString());
        
            
    return  returnOrThrow(withdrawResult);
  }

}
