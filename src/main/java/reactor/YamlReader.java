package reactor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class YamlReader {

    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    ReactorHolder holder;
    Map<String, Double> reactorBurnupMap = new HashMap<>();
    String path;

    public YamlReader(String path) {
        this.path = path;
        readYaml();
    }

    public void readYaml() {
        try {
            holder = mapper.readValue(new File(path), ReactorHolder.class);
            for (Map.Entry<String, ReactorType> entry : holder.getReactors().entrySet()) {
                String reactorClass = entry.getValue().getReactorClass();
                double burnup = entry.getValue().getBurnup();
                reactorBurnupMap.put(reactorClass, burnup);
            }
        } catch (IOException e) {
            // Обработка исключения IOException здесь, например:
            System.err.println("Ошибка чтения YAML-файла: " + e.getMessage());
            // Вы также можете перебросить исключение дальше, записать его в журнал или обработать его по-другому.
        }
    }

    public Map<String, Double> getReactorBurnupMap() {
        return reactorBurnupMap;
    }
}