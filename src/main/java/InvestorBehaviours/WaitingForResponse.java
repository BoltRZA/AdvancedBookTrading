package InvestorBehaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class WaitingForResponse extends Behaviour{
    private Agent agent;
    private double foundMinimal = Waiting4Response.foundMinimal;
    private AID agentMinimal;
    private boolean bhvDone = false;
    private double rcvCNT;
    private int roundCounter = 1;

    public WaitingForResponse(Agent agent, DataStore dataStore) {
        setDataStore(dataStore);
        this.agent = agent;
    }

    private List<AID> nowTraders;


    @Override
    public void action() {
        rcvCNT = Double.parseDouble(getDataStore().get("tradersCounter").toString());
        nowTraders = (List<AID>) getDataStore().get("confirmedTraders");
        DecimalFormat format = new DecimalFormat("###.00");
        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchProtocol("stuffBuying"), MessageTemplate.or(
                        MessageTemplate.MatchPerformative(ACLMessage.CONFIRM),
                        MessageTemplate.MatchPerformative(ACLMessage.DISCONFIRM)));
        ACLMessage response = agent.receive(mt);
        while (response != null) {
            if (response.getPerformative() == ACLMessage.CONFIRM) {
                    rcvCNT--;
                double price = Double.parseDouble(response.getContent());
                System.out.println("Agent " + agent.getLocalName() +" said: Agent's "
                            + response.getSender().getLocalName() + " price is - " + format.format(price));
                    if (price < foundMinimal) {
                    foundMinimal = price;
                    agentMinimal = response.getSender();
                    }
                        if (rcvCNT == 0) {
                            roundCounter++;
                            rcvCNT = nowTraders.size();
                            ACLMessage replyReject = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
                            replyReject.setProtocol("stuffBuying");
                            ACLMessage replyAccept = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                            replyAccept.setProtocol("stuffBuying");
                                for (AID trd : nowTraders) {
                                    if (!trd.equals(agentMinimal)) {
                                        replyReject.setContent(foundMinimal + "");
                                        replyReject.addReceiver(trd);
                                    }
                                }
                            replyAccept.addReceiver(agentMinimal);
                            replyAccept.setContent(foundMinimal + "");
                            System.out.println("Agent " + agent.getLocalName() + " said: The round " + roundCounter +
                            " winner is " + agentMinimal.getLocalName());
                            System.out.println("Sending prices back for the next round..");
                            System.out.println("______________________________________________________");
                            agent.send(replyAccept);
                            agent.send(replyReject);
                            block();
                        }
            } else if (response.getPerformative() == ACLMessage.DISCONFIRM ) {
                System.out.println("Agent " + response.getSender().getLocalName() + " is out of Trading!");
                nowTraders.remove(response.getSender());
                rcvCNT = nowTraders.size();
                block();
                }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            response = myAgent.receive(mt);
        }
            block();

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
