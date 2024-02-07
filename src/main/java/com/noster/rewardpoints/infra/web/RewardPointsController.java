package com.noster.rewardpoints.infra.web;

import com.noster.rewardpoints.usecases.AcceptTransaction;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RewardPointsController {

    private final AcceptTransaction acceptTransaction;

    public RewardPointsController(AcceptTransaction acceptTransaction) {
        this.acceptTransaction = acceptTransaction;
    }
}
