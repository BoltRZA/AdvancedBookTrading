package Agents;

import InvestorBehaviours.FSM4Investor;
import jade.core.Agent;

public class AgentInvestor extends Agent {
    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new FSM4Investor(this));
    }
}
