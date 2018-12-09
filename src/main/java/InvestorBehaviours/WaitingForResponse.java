package InvestorBehaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.List;

public class WaitingForResponse extends Behaviour{
    private Agent agent;
    private double foundMinimal = 1000000000;
    private AID agentMinimal;
    private List<AID> confirmedTraders;
    private boolean bhvDone = false;

    public WaitingForResponse(Agent agent) {
        this.agent = agent;
    }


    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchProtocol("stuffBuying"),
                MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE), MessageTemplate.or(
                        MessageTemplate.MatchPerformative(ACLMessage.CONFIRM),
                        MessageTemplate.MatchPerformative(ACLMessage.DISCONFIRM))
                ));
        ACLMessage response = agent.receive(mt);

        if (response != null) {
            if (response.getPerformative() == ACLMessage.PROPOSE || response.getPerformative() == ACLMessage.CONFIRM) {
                if (response.getPerformative() == ACLMessage.PROPOSE) {
                    confirmedTraders.add(response.getSender());
                }
                System.out.println("Agent's " + response.getSender().getLocalName() + " price is:"
                        + response.getContent());
                double price = Double.parseDouble(response.getContent());
                if (price < foundMinimal) {
                    foundMinimal = price;
                    agentMinimal = response.getSender();
                }
                ACLMessage replyReject = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
                ACLMessage replyAccept = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                for (AID trd : confirmedTraders) {
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
                block();
            } else if (response.getPerformative() == ACLMessage.DISCONFIRM) {
                System.out.println("Agent " + response.getSender().getLocalName() + " is out of Trading!");
                confirmedTraders.remove(response.getSender());
                block();
            }
        } else {
            block();
        }
        if (confirmedTraders.size() == 1){
            System.out.println("Auction has been finished! The price is " + foundMinimal + " and the winner" +
                    " is"  + agentMinimal.getLocalName() );
            bhvDone = true;
        }
    }

    @Override
    public boolean done() {
        return bhvDone;
    }
}
