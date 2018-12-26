package TraderBehavoiors;

import etc.Valuta;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.List;

public class Waiting4Request extends Behaviour {
    private Agent agent;
    private List<Valuta> bitCoin;
    private boolean msgArrived = false;

    public Waiting4Request(Agent agent, DataStore dataStore) {
        setDataStore(dataStore);
        bitCoin = (List<Valuta>) dataStore.get("bitCoin");
        this.agent = agent;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchProtocol("stuffBuying"),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
        ACLMessage message = agent.receive(mt);

        if (message != null){
            ACLMessage answer = message.createReply();
            System.out.println("Agent " + agent.getLocalName() + " said: Hi, agent " + message.getSender().getLocalName() +
                    "! I have some stuff, start price is " + bitCoin.get(0).getStartPrice() + "! Let's haggle?!");
            msgArrived = true;
            answer.setPerformative(ACLMessage.PROPOSE);
            answer.setProtocol("stuffBuying");
            answer.setContent(bitCoin.get(0).getStartPrice() + "");
            agent.send(answer);
        }
    }

    @Override
    public boolean done() {
        return msgArrived;
    }
}
