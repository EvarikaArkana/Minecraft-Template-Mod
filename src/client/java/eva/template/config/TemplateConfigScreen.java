package eva.template.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.network.chat.Component;

import static eva.template.config.TemplateConfig.*;

public class TemplateConfigScreen implements ModMenuApi {
    public static ConfigBuilder builder() {
        ConfigBuilder builder = ConfigBuilder.create()
                .setTitle(Component.literal("Simply Dual Wielding Config"))
                .setSavingRunnable(JsonConfigHelper::writeToConfig)
                .setEditable(true);
        TemplateConfig INSTANCE = getInstance();
        return builder;
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            // Return the screen here with the one you created from Cloth Config Builder
            return builder().setParentScreen(parent).build();
        };
    }
}