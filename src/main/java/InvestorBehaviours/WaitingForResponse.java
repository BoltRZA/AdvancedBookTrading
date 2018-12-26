package InvestorBehaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.List;

public class WaitingForResponse extends Behaviour{
    private Agent agent;
    private double foundMinimal = Waiting4Response.foundMinimal;
    private AID agentMinimal;
    private boolean bhvDone = false;
    private double rcvCNT;
    public WaitingForResponse(Agent agent, DataStore dataStore) {
        setDataStore(dataStore);
        this.agent = agent;
        nowTraders = (List<AID>) dataStore.get("confirmedTraders");
        rcvCNT = Double.parseDouble(getDataStore().get("tradersCounter").toString());
    }

    private List<AID> nowTraders;


    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchProtocol("stuffBuying"), MessageTemplate.or(
                        MessageTemplate.MatchPerformative(ACLMessage.CONFIRM),
                        MessageTemplate.MatchPerformative(ACLMessage.DISCONFIRM)));
        ACLMessage response = agent.receive(mt);

        if (response != null) {
                if (response.getPerformative() == ACLMessage.CONFIRM) {
                    rcvCNT--;
                    System.out.println("Agent " + agent.getLocalName() +" said: Agent's "
                            + response.getSender().getLocalName() + " price is:" + response.getContent());
                double price = Double.parseDouble(response.getContent());
                    if (price < foundMinimal) {
                    foundMinimal = price;
                    agentMinimal = response.getSender();
                    }
                        if (rcvCNT == 0) {
                            rcvCNT = nowTraders.size();
                            ACLMessage replyReject = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
                            ACLMessage replyAccept = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                                for (AID trd : nowTraders) {
                                    if (trd != agentMinimal) {
                                        replyReject.setContent(foundMinimal + "");
                                        replyReject.addReceiver(trd);
                                    } else {
                                        replyAccept.setContent(foundMinimal + " ");
                                        replyAccept.addReceiver(trd);
                                    }
                                }
                            agent.send(replyAccept);
                            agent.send(replyReject);
                        }
                block();
                } else if (response.getPerformative() == ACLMessage.DISCONFIRM ) {
                System.out.println("Agent " + response.getSender().getLocalName() + " is out of Trading!");
                nowTraders.remove(response.getSender());
                rcvCNT = nowTraders.size();
                block();
                }
        } else {
            block();
        }
        if (nowTraders.size() == 1){
            System.out.println("Auction has been finished! The price is " + foundMinimal + " and the winner" +
                    " is "  + agentMinimal.getLocalName() );
            bhvDone = true;
        }
    }

    @Override
    public boolean done() {
        return bhvDone;
    }
}
