package net.a11v1r15.creakingsap;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreakingSap implements ModInitializer {
	public static final String MOD_ID = "creaking-sap";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		CreakingSapItems.initialize();
		CreakingSapBlocks.initialize();
		LOGGER.info("Please, grease your doors");
	}

	public static Identifier id(String id){
		return Identifier.of(MOD_ID, id);
	}
}