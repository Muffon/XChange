package com.xeiam.xchange.btce.v3.service.polling;

import java.io.IOException;
import java.math.BigDecimal;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.btce.v3.BTCEAdapters;
import com.xeiam.xchange.btce.v3.dto.account.BTCEAccountInfo;
import com.xeiam.xchange.currency.Currency;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.exceptions.NotAvailableFromExchangeException;
import com.xeiam.xchange.service.polling.account.PollingAccountService;


/**
 * @author Matija Mazi
 */
public class BTCEAccountService extends BTCEAccountServiceRaw implements PollingAccountService {

  /**
   * Constructor
   *
   * @param exchange
   */
  public BTCEAccountService(Exchange exchange) {

    super(exchange);
  }

  @Override
  public AccountInfo getAccountInfo() throws IOException {

    BTCEAccountInfo info = getBTCEAccountInfo(null, null, null, null, null, null, null);
    return BTCEAdapters.adaptAccountInfo(info);
  }
 
  @Override
<<<<<<< HEAD
  public String withdrawFunds(String currency, BigDecimal amount, String address) throws IOException {
     String s = withdrawFunds(currency, amount, address);
=======
  public String withdrawFunds(Currency currency, BigDecimal amount, String address) throws IOException {
     String s = withdraw(currency, amount, address);
>>>>>>> 5399dbf6c48735a4e15e653f076829d44029a076
     return s;
  }

  @Override
  public String requestDepositAddress(Currency currency, String... args) throws IOException {

    throw new NotAvailableFromExchangeException();
  }

}
