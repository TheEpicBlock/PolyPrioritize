package nl.theepicblock.polyprioritize;

import io.github.theepicblock.polymc.api.PolyMcEntrypoint;
import io.github.theepicblock.polymc.api.PolyRegistry;
import io.github.theepicblock.polymc.api.resource.ResourcePackMaker;
import io.github.theepicblock.polymc.impl.generator.BlockPolyGenerator;
import net.fabricmc.api.ModInitializer;

public class PolyPrioritize implements PolyMcEntrypoint {

	@Override
	public void registerPolys(PolyRegistry polyRegistry) {
		ConfigReader.readAndForEach((block) -> {
			polyRegistry.registerBlockPoly(block, BlockPolyGenerator.generatePoly(block, polyRegistry));
		});
	}
}
