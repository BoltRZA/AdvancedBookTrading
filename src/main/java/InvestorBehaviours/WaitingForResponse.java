package InvestorBehaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.List;

public class WaitingForResponse extends CyclicBehaviour{
    private Agent agent;
    private double foundMinimal = 1000000;
    private List<AID> confirmedTraders;

    public WaitingForResponse(Agent agent) {
        this.agent = agent;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchProtocol("stuffBuying"), MessageTemplate.or(
                        MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                        MessageTemplate.MatchPerformative(ACLMessage.CANCEL)
                ));
        ACLMessage response = agent.receive(mt);

        if(response != null){
            if(response.getPerformative() == ACLMessage.INFORM){
                confirmedTraders.add(response.getSender());
                System.out.println("Agent's " + response.getSender().getLocalName() + " price is:"
                        + response.getContent());
                double price = Double.parseDouble(response.getContent());
                if(price < foundMinimal){
                    foundMinimal = price;
                }
            }else if(response.getPerformative() == ACLMessage.CANCEL){
                System.out.println("Agent " + response.getSender().getLocalName() + " has no Bitcoins!");
            }
        }else{
            block();
        }
        for (AID trd:confirmedTraders){
            ACLMessage reply = new ACLMessage(ACLMessage.CONFIRM);
            reply.setContent(foundMinimal+"");
        }
    }
}
