package TraderBehavoiors;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.FSMBehaviour;

public class FSM4Trader extends FSMBehaviour {
    private Agent agent;
    private final static String START = "start";
    private final static String END = "end";


    public FSM4Trader(Agent agent, DataStore dataStore) {
        setDataStore(dataStore);
        this.agent = agent;
        registerFirstState(new Waiting4Request(agent,dataStore), START);
        registerLastState(new Trading(agent, dataStore), END);
        registerDefaultTransition(START, END);
    }

}
