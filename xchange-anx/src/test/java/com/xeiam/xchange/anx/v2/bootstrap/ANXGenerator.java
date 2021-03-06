package com.xeiam.xchange.anx.v2.bootstrap;

import static com.xeiam.xchange.currency.Currency.AUD;
import static com.xeiam.xchange.currency.Currency.BGC;
import static com.xeiam.xchange.currency.Currency.BTC;
import static com.xeiam.xchange.currency.Currency.CAD;
import static com.xeiam.xchange.currency.Currency.CHF;
import static com.xeiam.xchange.currency.Currency.CNY;
import static com.xeiam.xchange.currency.Currency.DOGE;
import static com.xeiam.xchange.currency.Currency.EGD;
import static com.xeiam.xchange.currency.Currency.EUR;
import static com.xeiam.xchange.currency.Currency.GBP;
import static com.xeiam.xchange.currency.Currency.HKD;
import static com.xeiam.xchange.currency.Currency.JPY;
import static com.xeiam.xchange.currency.Currency.LTC;
import static com.xeiam.xchange.currency.Currency.NMC;
import static com.xeiam.xchange.currency.Currency.NZD;
import static com.xeiam.xchange.currency.Currency.PPC;
import static com.xeiam.xchange.currency.Currency.SGD;
import static com.xeiam.xchange.currency.Currency.START;
import static com.xeiam.xchange.currency.Currency.STR;
import static com.xeiam.xchange.currency.Currency.USD;
import static com.xeiam.xchange.currency.Currency.XRP;
import static com.xeiam.xchange.currency.CurrencyPair.DOGE_BTC;
import static com.xeiam.xchange.currency.CurrencyPair.LTC_BTC;
import static com.xeiam.xchange.currency.CurrencyPair.STR_BTC;
import static com.xeiam.xchange.currency.CurrencyPair.XRP_BTC;
import static java.lang.System.out;
import static java.math.BigDecimal.ONE;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xeiam.xchange.anx.v2.dto.meta.ANXMarketMetaData;
import com.xeiam.xchange.anx.v2.dto.meta.ANXMetaData;
import com.xeiam.xchange.currency.Currency;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.meta.CurrencyMetaData;

public class ANXGenerator {

  static ObjectMapper mapper = new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true)
      .setSerializationInclusion(JsonInclude.Include.NON_NULL);

  static Set<Currency> cryptos = new HashSet<Currency>(Arrays.asList(BTC, LTC, DOGE, STR, XRP, START, EGD));
  static Currency[] fiats = { USD, EUR, GBP, HKD, AUD, CAD, NZD, SGD, JPY, CNY };

  // counter currencies for STARTCoin - all fiats but CNY
  static Currency[] fiatsStart = { USD, EUR, GBP, HKD, AUD, CAD, NZD, SGD, JPY };

  static CurrencyPair[] pairsOther = { LTC_BTC, DOGE_BTC, STR_BTC, XRP_BTC };

  // base currency -> min order size
  static Map<Currency, BigDecimal> minAmount = new HashMap<Currency, BigDecimal>();
  static Map<Currency, BigDecimal> maxAmount = new HashMap<Currency, BigDecimal>();
  static Map<Currency, CurrencyMetaData> currencyMap = new TreeMap<Currency, CurrencyMetaData>();

  static Set<CurrencyPair> pairs = new HashSet<CurrencyPair>();

  static {
    minAmount.put(BTC, ONE.movePointLeft(2));
    minAmount.put(LTC, ONE.movePointLeft(1));
    minAmount.put(DOGE, ONE.movePointRight(4));
    minAmount.put(XRP, ONE.movePointLeft(2));
    minAmount.put(STR, ONE.movePointLeft(2));
    minAmount.put(START, null);
    minAmount.put(EGD, null);

    maxAmount.put(BTC, ONE.movePointRight(5));
    maxAmount.put(LTC, ONE.movePointRight(7));
    maxAmount.put(DOGE, ONE.movePointRight(10));
    maxAmount.put(XRP, ONE.movePointRight(5));
    maxAmount.put(STR, ONE.movePointRight(5));
    maxAmount.put(START, null);
    maxAmount.put(EGD, null);

    for (Currency crypto : cryptos) {
      currencyMap.put(crypto, new CurrencyMetaData(8));
    }

    currencyMap.put(CNY, new CurrencyMetaData(8));
    for (Currency fiat : fiats) {
      if (!currencyMap.containsKey(fiat))
        currencyMap.put(fiat, new CurrencyMetaData(2));
    }

    // extra currencies available, but not traded
    currencyMap.put(CHF, new CurrencyMetaData(2));
    currencyMap.put(NMC, new CurrencyMetaData(8));
    currencyMap.put(BGC, new CurrencyMetaData(8));
    currencyMap.put(PPC, new CurrencyMetaData(8));

    Collections.addAll(pairs, pairsOther);

    for (Currency base : Arrays.asList(BTC, EGD))
      for (Currency counter : fiats)
        pairs.add(new CurrencyPair(base, counter));

    for (Currency counter : fiatsStart) {
      pairs.add(new CurrencyPair(START, counter));
    }
  }

  static BigDecimal fee = new BigDecimal(".006");

  public static void main(String[] args) throws IOException {
    new ANXGenerator().run();
  }

  private void run() throws IOException {
    Map<CurrencyPair, ANXMarketMetaData> map = new TreeMap<CurrencyPair, ANXMarketMetaData>();

    for (CurrencyPair pair : pairs) {
      handleCurrencyPair(map, pair);
    }
    // TODO add RateLimits, fees
    ANXMetaData metaData = new ANXMetaData(map, currencyMap, null, null, null, null, null);

    mapper.writeValue(out, metaData);
    out.println();
    out.flush();
  }

  private void handleCurrencyPair(Map<CurrencyPair, ANXMarketMetaData> map, CurrencyPair currencyPair) {
    int amountScale = amountScale(currencyPair);
    BigDecimal minimumAmount = scaled(minAmount.get(currencyPair.base.getCurrencyCode()), amountScale);
    BigDecimal maximumAmount = scaled(maxAmount.get(currencyPair.base.getCurrencyCode()), amountScale);
    ANXMarketMetaData mmd = new ANXMarketMetaData(fee, minimumAmount, maximumAmount, priceScale(currencyPair));
    map.put(currencyPair, mmd);
  }

  BigDecimal scaled(BigDecimal value, int scale) {
    return value == null ? null : value.setScale(scale, RoundingMode.UNNECESSARY);
  }

  private int amountScale(CurrencyPair currencyPair) {
    return currencyMap.get(currencyPair.base.getCurrencyCode()).scale;
  }

  int priceScale(CurrencyPair pair) {
    if (LTC_BTC.equals(pair) || (BTC.equals(pair.base.getCurrencyCode()) && !cryptos.contains(pair.counter.getCurrencyCode())))
      return 5;
    else
      return 8;
  }

}
