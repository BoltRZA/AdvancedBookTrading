package Agents;

import TraderBehavoiors.FSM4Trader;
import etc.Valuta;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.FSMBehaviour;

import java.util.ArrayList;
import java.util.List;

public class AgentTrader extends Agent {
    private List<Valuta> bitCoin;

    @Override
    protected void setup() {
        super.setup();
        bitCoin = new ArrayList<Valuta>();
        createSettings4Traders(bitCoin);
        DataStore ds = new DataStore();
        ds.put("bitCoin", bitCoin);
        addBehaviour(new FSM4Trader(this, ds));
    }

    private void createSettings4Traders(List<Valuta> bitCoin){
        if(this.getLocalName().equals("Trader1")){
            bitCoin.add(new Valuta(300, 190));
        }
        if(this.getLocalName().equals("Trader2")){
            bitCoin.add(new Valuta(310, 200));
        }
        if(this.getLocalName().equals("Trader3")){
            bitCoin.add(new Valuta(340, 210));
        }
        if(this.getLocalName().equals("Trader4")){
            bitCoin.add(new Valuta(310, 180));
        }
    }
}
