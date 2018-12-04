package InvestorBehaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;

public class Requesting extends OneShotBehaviour {
    private Agent agent;

    public Requesting(Agent agent ) {
        this.agent = agent;
    }

    @Override
    public void action() {
        List<AID> traders = new ArrayList<AID>();
        traders.add(new AID("Trader1",false));
        traders.add(new AID("Trader2",false));
        traders.add(new AID("Trader3",false));
        traders.add(new AID("Trader4",false));

        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.setProtocol("stuffBuying");
        for (AID rec:traders){
            request.addReceiver(rec);
        }
        agent.send(request);
    }
}

