package reactor;

public class ReactorType {

    private String reactorClass;
    private double burnup;

    public String getReactorClass() {
        return reactorClass;
    }

    public void setReactorClass(String reactorClass) {
        this.reactorClass = reactorClass;
    }

    public double getBurnup() {
        return burnup;
    }

    public void setBurnup(double burnup) {
        this.burnup = burnup;
    }
}