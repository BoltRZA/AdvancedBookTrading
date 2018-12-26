package Agents;

import InvestorBehaviours.FSM4Investor;
import jade.core.Agent;
import jade.core.behaviours.DataStore;

public class AgentInvestor extends Agent {
    @Override
    protected void setup() {
        super.setup();
        DataStore dataStore = new DataStore();
        addBehaviour(new FSM4Investor(this, dataStore));

    }
}
