package InvestorBehaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.ActionExecutor;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.List;

public class Waiting4Response extends Behaviour {
    private Agent agent;
    public static double foundMinimal = 100000000;
    private AID agentMinimal;
    private List<AID> confirmedTraders = new ArrayList<AID>();
    private boolean bhvDone = false;
    private double rcvCNT;

    public Waiting4Response(Agent agent, DataStore dataStore) {
        setDataStore(dataStore);
        this.agent = agent;

    }

    @Override
    public void onStart() {
        this.rcvCNT = (Double) getDataStore().get("tradersCounter");
    }

    @Override
    public void action() {

//        rcvCNT = Double.parseDouble(getDataStore().get("tradersCounter").toString());
        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
                MessageTemplate.MatchProtocol("stuffBuying"));

        ACLMessage message = agent.receive(mt);

        if (message != null){
            confirmedTraders.add(message.getSender());
            rcvCNT--;
            System.out.println("Agent " + agent.getLocalName() + " said: I've received start price from:" +
                    message.getSender().getLocalName() + " and it is - " + message.getContent());
            double price = Double.parseDouble(message.getContent());

            if (price < foundMinimal){
                agentMinimal = message.getSender();
                foundMinimal = price;
            }
        }else {
            block();
        }
        if (rcvCNT == 0){
            bhvDone = true;
        }
    }

    @Override
    public boolean done() {
        return bhvDone;
    }

    @Override
    public int onEnd() {
        if (bhvDone && agentMinimal != null){
            System.out.println("Agent " + agent.getLocalName() + " said: The round 1 winner is " +
                    agentMinimal.getLocalName());
            System.out.println("_____________________________________________________");
            ACLMessage accept = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
            accept.setProtocol("stuffBuying");
            accept.setContent(foundMinimal + "");
            accept.addReceiver(agentMinimal);
            agent.send(accept);
            ACLMessage reject = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
            reject.setContent(foundMinimal + "");
            reject.setProtocol("stuffBuying");
            for (AID rec : confirmedTraders){
                if (!rec.equals(agentMinimal)){
                    reject.addReceiver(rec);
                }
            }
            agent.send(reject);
            getDataStore().put("confirmedTraders", confirmedTraders);
        }
        return super.onEnd();
    }
}
