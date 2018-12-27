package TraderBehavoiors;

import etc.Valuta;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.text.DecimalFormat;
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
        this.agent = agent;
    }

    @Override
    public void action() {
        DecimalFormat format = new DecimalFormat("###.00");
        bitCoin = (List<Valuta>)getDataStore().get("bitCoin");
        minimum = bitCoin.get(0).getMinimalPrice();
        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchProtocol("stuffBuying"),
                MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL),
                        MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL)));

        ACLMessage message = agent.receive(mt);
        while (message != null){
            if (message.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){
                ACLMessage noBidding = message.createReply();
                noBidding.setContent(message.getContent());
                noBidding.setPerformative(ACLMessage.CONFIRM);
                agent.send(noBidding);
                double price = Double.parseDouble(message.getContent());
                System.out.println("Agent " + agent.getLocalName() + " said: Resending price got from the previous" +
                        " round - " + format.format(price));
                block();
            }else if (message.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
                priceGot = Double.parseDouble(message.getContent());
                if (priceGot > minimum){
                    ACLMessage replyConfirm = message.createReply();
                    replyConfirm.setPerformative(ACLMessage.CONFIRM);
                    double discount = 1 + (int) (Math.random() * 10);
                    double priceSaled = priceGot * (1 - discount/100);
                    if (priceSaled <= minimum){
                        priceSaled = minimum;
                        System.out.println("Agent " + agent.getLocalName() + " said: I'm offering my minimum now - " +
                                format.format(priceSaled));
                    }else {
                        System.out.println("Agent " + agent.getLocalName() + " said: The discount is " + discount +
                                "%, so the price is - " + format.format(priceSaled));
                    }
                    replyConfirm.setContent(priceSaled + "");
                    agent.send(replyConfirm);
                    block();
                }else{
                    ACLMessage replyDisconfirm = message.createReply();
                    replyDisconfirm.setPerformative(ACLMessage.DISCONFIRM);
                    agent.send(replyDisconfirm);
                    outOfTrading = true;
                }
            }else {
                System.out.println("Agent " + agent.getLocalName() + " said: WTF!");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
             message = agent.receive(mt);
        }
        block();
    }

    @Override
    public boolean done() {
        return outOfTrading;
    }
}
