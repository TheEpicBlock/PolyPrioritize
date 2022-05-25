package nl.theepicblock.polyprioritize;

import io.github.theepicblock.polymc.api.PolyMcEntrypoint;
import io.github.theepicblock.polymc.api.PolyRegistry;
import io.github.theepicblock.polymc.impl.generator.BlockPolyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PolyPrioritize implements PolyMcEntrypoint {
	public static final Logger LOGGER = LoggerFactory.getLogger("polyprioritize");

	@Override
	public void registerPolys(PolyRegistry polyRegistry) {
		var c = ConfigReader.readAndForEach((block) -> polyRegistry.registerBlockPoly(block, BlockPolyGenerator.generatePoly(block, polyRegistry)));
		PolyPrioritize.LOGGER.info("[PolyPrioritize] prioritized "+c+" blocks");
	}
}
