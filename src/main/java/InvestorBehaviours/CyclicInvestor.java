package InvestorBehaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class CyclicInvestor extends CyclicBehaviour {
    private boolean end;
    private AID bestSeller;
    private double bestPrice;
    private Agent agent;
    private int tradersCounter;

    public CyclicInvestor(Agent agent) {
        this.agent = agent;
    }

    @Override
    public void action() {
        tradersCounter = 0;
        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchProtocol("stuffBuying"),
                MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));
        ACLMessage propose = agent.receive(mt);
        if (propose != null){
            tradersCounter++;
           double currentPrice = Double.parseDouble(propose.getContent());
           if()

        }else{
            System.out.println("Error!!!");
        }
    }
}
