package com.github.marceloleite2604.cryptotrader.service.mercadobitcoin;

import com.github.marceloleite2604.cryptotrader.model.Active;
import com.github.marceloleite2604.cryptotrader.model.Instrument;
import com.github.marceloleite2604.cryptotrader.model.Ticker;
import com.github.marceloleite2604.cryptotrader.model.account.Account;
import com.github.marceloleite2604.cryptotrader.model.account.Balance;
import com.github.marceloleite2604.cryptotrader.model.account.Position;
import com.github.marceloleite2604.cryptotrader.model.candles.Candle;
import com.github.marceloleite2604.cryptotrader.model.candles.CandlesRequest;
import com.github.marceloleite2604.cryptotrader.model.orderbook.OrderBook;
import com.github.marceloleite2604.cryptotrader.model.orders.Order;
import com.github.marceloleite2604.cryptotrader.model.orders.PlaceOrderRequest;
import com.github.marceloleite2604.cryptotrader.model.orders.RetrieveOrdersRequest;
import com.github.marceloleite2604.cryptotrader.model.trades.Trade;
import com.github.marceloleite2604.cryptotrader.model.trades.TradesRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class MercadoBitcoinService {

  private final InstrumentService instrumentService;

  private final OrderBookService orderBookService;

  private final TickersService tickersService;

  private final CandlesService candlesService;

  private final TradesService tradesService;

  private final AccountsService accountsService;

  private final BalanceService balanceService;

  private final PositionService positionService;

  private final OrderService orderService;

  @Cacheable("default")
  public Set<Instrument> retrieveAllInstruments() {
    return instrumentService.retrieveAll();
  }

  public OrderBook retrieveOrderBook(Active active) {
    return retrieveOrderBook(active, null);
  }

  public OrderBook retrieveOrderBook(Active active, Integer limit) {
    return orderBookService.retrieve(active.getSymbol(), limit);
  }

  public Ticker retrieveTicker(Active active) {
    return retrieveTickers(active).get(active.getSymbol());
  }

  public Map<String, Ticker> retrieveTickers(Active... actives) {
    final var symbols = Arrays.stream(actives)
      .map(Active::getSymbol)
      .toList()
      .toArray(String[]::new);
    return tickersService.retrieve(symbols);
  }

  public List<Candle> retrieveCandles(CandlesRequest candlesRequest) {
    return candlesService.retrieve(candlesRequest);
  }

  public List<Trade> retrieveTrades(TradesRequest tradesRequest) {
    return tradesService.retrieve(tradesRequest);
  }

  @Cacheable("default")
  public List<Account> retrieveAccounts() {
    return accountsService.retrieve();
  }

  public Account retrieveAccount() {
    return retrieveAccounts().get(0);
  }

  public List<Balance> retrieveBalances(String accountId, Active active) {
    return balanceService.retrieve(accountId, active.getSymbol());
  }

  public Balance retrieveBalance(String accountId, Active active) {

    return retrieveBalances(accountId, active)
      .stream()
      .filter(balance -> active.equals(balance.getActive()))
      .findFirst()
      .orElseThrow(() -> {
        final var message = String.format("Could not find balance for %s active.", active.getName());
        return new IllegalStateException(message);
      });
  }

  public List<Position> retrievePositions(String accountId, Active active) {
    return positionService.retrieve(accountId, active.getSymbol());
  }

  public List<Order> retrieveOrders(RetrieveOrdersRequest retrieveOrdersRequest) {
    return orderService.retrieve(retrieveOrdersRequest);
  }

  public Order retrieveOrder(String accountId, Active active, String orderId) {
    return orderService.retrieveOrder(accountId, active.getSymbol(), orderId);
  }

  public Optional<String> placeOrder(PlaceOrderRequest placeOrderRequest) {
    return orderService.placeOrder(placeOrderRequest);
  }

  public boolean cancelOrder(String accountId, Active active, String orderId) {
    return orderService.cancelOrder(accountId, active.getSymbol(), orderId);
  }
}
