package eva.template.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eva.template.TemplateClient;
import eva.template.TemplateMain;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonConfigHelper {
    private static final File folder = new File("config");
    private static File simplydualwieldingConfig;
    public static Gson configGson = new GsonBuilder().setPrettyPrinting().create();

    public static void init() {
        createConfig();
        readFromConfig();
        writeToConfig();
    }

    public static void createConfig() {
        if (!folder.exists()) {
            folder.mkdir();
        }
        if (folder.isDirectory()) {
            simplydualwieldingConfig = new File(folder, "simplydualwielding.json");
            boolean seemsValid;
            if (simplydualwieldingConfig.exists()) {
                try {
                    String simplydualwieldingConfigJson = Files.readString(Path.of(simplydualwieldingConfig.getPath()));
                    seemsValid = simplydualwieldingConfigJson.trim().startsWith("{");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                seemsValid = true;
            }
            if (!simplydualwieldingConfig.exists() || !seemsValid) {
                if (!seemsValid) {
                    TemplateClient.LOGGER.info("Found invalid config file, creating new config file at './config/moreshieldvariants.json'.");
                }
                try {
                    simplydualwieldingConfig.createNewFile();
                    String json = configGson.toJson(TemplateConfig.getInstance());
                    FileWriter writer = new FileWriter(simplydualwieldingConfig);
                    writer.write(json);
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    public static void readFromConfig() {
        try {
            TemplateConfig config = configGson.fromJson(new FileReader(simplydualwieldingConfig), TemplateConfig.class);
            TemplateConfig.getInstance().updateConfigs(config);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static void writeToConfig() {
        try {
            String json = configGson.toJson(TemplateConfig.getInstance());
            FileWriter writer = new FileWriter(simplydualwieldingConfig, false);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}