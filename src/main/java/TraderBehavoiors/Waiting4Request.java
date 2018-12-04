+package TraderBehavoiors;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Waiting4Request extends CyclicBehaviour {
    private Agent agent;
    private double startPrice;
    private int step = 0;

    public Waiting4Request(Agent agent, double currentPrice) {
        this.agent = agent;
        this.startPrice = currentPrice;
    }

    @Override
    public void action() {
        switch (step){
            case 0:
                MessageTemplate startingMT = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                        MessageTemplate.MatchProtocol("stuffBuying"));
                ACLMessage request = agent.receive(startingMT);
                if (request != null){
                    ACLMessage reply = request.createReply();
                    reply.setContent(startPrice + "");
                    reply.setPerformative(ACLMessage.INFORM);
                    System.out.println("Agent " + agent.getLocalName() + " said: I've offered my Price!" );
                    step++;
                    block();
                }else
                    block();
            case 1:
                MessageTemplate cyclicMT = MessageTemplate.and(MessageTemplate.MatchProtocol("stuffBuying"),
                        MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));
                ACLMessage propose = agent.receive(cyclicMT);
                if(propose != null){


        }


            }
    }
}
