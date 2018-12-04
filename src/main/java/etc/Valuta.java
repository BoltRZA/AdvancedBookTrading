package etc;

public class Valuta {
    private double startPrice;
    private double minimalPrice;

    public Valuta(double startPrice, double minimalPrice) {
        this.startPrice = startPrice;
        this.minimalPrice = minimalPrice;
    }

    public double getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(double startPrice) {
        this.startPrice = startPrice;
    }

    public double getMinimalPrice() {
        return minimalPrice;
    }

    public void setMinimalPrice(double minimalPrice) {
        this.minimalPrice = minimalPrice;
    }
}
