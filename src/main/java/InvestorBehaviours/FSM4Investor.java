package InvestorBehaviours;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.FSMBehaviour;

public class FSM4Investor extends FSMBehaviour {
    private Agent agent;

    public FSM4Investor(Agent agent, DataStore dataStore) {
        setDataStore(dataStore);
        this.agent = agent;
    }
}
