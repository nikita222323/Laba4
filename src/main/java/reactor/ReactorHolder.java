package reactor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class ReactorHolder {

    Map<String, ReactorType> reactors = new HashMap<>();

    @JsonAnySetter
    public void setReactor(String name, Map<String, Object> properties) {
        ReactorType reactor = new ReactorType();
        reactor.setReactorClass((String) properties.get("class"));

        Object burnupValue = properties.get("burnup");
        if (burnupValue != null) {
            reactor.setBurnup(new BigDecimal(burnupValue.toString()).doubleValue());
        }

        reactors.put(name, reactor);
    }

    public Map<String, ReactorType> getReactors() {
        return reactors;
    }
}