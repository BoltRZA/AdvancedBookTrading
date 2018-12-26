package InvestorBehaviours;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.FSMBehaviour;

public class FSM4Investor extends FSMBehaviour {
    private final static String START = "start";
    private final static String END = "end";
    private final static String NotanEnd = "notanend";

    private Agent agent;

    public FSM4Investor(Agent agent, DataStore dataStore) {
        setDataStore(dataStore);
        this.agent = agent;
        registerFirstState(new Requesting(agent, dataStore), START);
        registerState(new Waiting4Response(agent, dataStore), NotanEnd);
        registerLastState(new WaitingForResponse(agent, dataStore), END);
        registerDefaultTransition(START, NotanEnd);
        registerDefaultTransition(NotanEnd, END);
    }
}
