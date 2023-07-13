package com.github.marceloleite2604.cryptotrader.service.actionexecutor;

import com.github.marceloleite2604.cryptotrader.model.Action;

import java.util.List;

public interface ActionExecutor {

  void execute(List<Action> actions);
}
