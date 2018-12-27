package InvestorBehaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;

public class Requesting extends OneShotBehaviour {
    private Agent agent;
    private double tradersCounter;

    public Requesting(Agent agent, DataStore dataStore) {
        this.agent = agent;
        setDataStore(dataStore);
    }

    @Override
    public void action() {
        List<AID> traders = new ArrayList<AID>();
        traders.add(new AID("Trader1",false));
        traders.add(new AID("Trader2",false));
        traders.add(new AID("Trader3",false));
        traders.add(new AID("Trader4",false));
        tradersCounter = traders.size();
        getDataStore().put("tradersCounter", tradersCounter);
        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.setProtocol("stuffBuying");
        for (AID rec:traders){
            request.addReceiver(rec);
        }
        agent.send(request);
        System.out.println("Agent " + agent.getLocalName() + " said: I want some stuff!");
    }
}

