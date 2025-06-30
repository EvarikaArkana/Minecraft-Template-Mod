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
    private static File templateConfig;
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
            templateConfig = new File(folder, "template.json");
            boolean seemsValid;
            if (templateConfig.exists()) {
                try {
                    String templateConfigJson = Files.readString(Path.of(templateConfig.getPath()));
                    seemsValid = templateConfigJson.trim().startsWith("{");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                seemsValid = true;
            }
            if (!templateConfig.exists() || !seemsValid) {
                if (!seemsValid) {
                    TemplateClient.LOGGER.info("Found invalid config file, creating new config file at './config/moreshieldvariants.json'.");
                }
                try {
                    templateConfig.createNewFile();
                    String json = configGson.toJson(TemplateConfig.getInstance());
                    FileWriter writer = new FileWriter(templateConfig);
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
            TemplateConfig config = configGson.fromJson(new FileReader(templateConfig), TemplateConfig.class);
            TemplateConfig.getInstance().updateConfigs(config);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static void writeToConfig() {
        try {
            String json = configGson.toJson(TemplateConfig.getInstance());
            FileWriter writer = new FileWriter(templateConfig, false);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}