package TraderBehavoiors;

import etc.Valuta;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.List;

public class Trading extends Behaviour {
    private Agent agent;
    private double priceGot;
    private List<Valuta> bitCoin;
    private boolean outOfTrading;
    private double minimum;
    private double currentPrice;

    public Trading(Agent agent, DataStore dataStore) {
        setDataStore(dataStore);
        bitCoin = (List<Valuta>)dataStore.get("bitCoin");
        this.agent = agent;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchProtocol("stuffBuing"),
                MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL),
                        MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL)));

        ACLMessage message = agent.receive(mt);
        minimum = bitCoin.get(0).getMinimalPrice();

        if (message != null){
            if (message.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){
                ACLMessage noBidding = message.createReply();
                noBidding.setContent(message.getContent());
                agent.send(noBidding);
                block();
            }else {
                priceGot = Double.parseDouble(message.getContent());
                if (priceGot > minimum){
                    ACLMessage replyConfirm = message.createReply();
                    replyConfirm.setPerformative(ACLMessage.CONFIRM);
                    int discount = 1 + (int) (Math.random() * 10);
                    System.out.println("Agent " + agent.getLocalName() + " said: The discount is " + discount + "%");
                    replyConfirm.setContent(priceGot * discount / 100 + "");
                    agent.send(replyConfirm);
                    block();
                }else{
                    ACLMessage replyDisconfirm = message.createReply();
                    replyDisconfirm.setPerformative(ACLMessage.DISCONFIRM);
                    agent.send(replyDisconfirm);
                    outOfTrading = true;
                }
            }
        }else {
            block();
        }
    }

    @Override
    public boolean done() {
        return outOfTrading;
    }
}
