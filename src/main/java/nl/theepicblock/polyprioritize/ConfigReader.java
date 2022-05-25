package nl.theepicblock.polyprioritize;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.function.Consumer;

public class ConfigReader {
	public static int readAndForEach(Consumer<Block> consumer) {
		Path configDirPath = FabricLoader.getInstance().getConfigDir();
		Path configPath = configDirPath.resolve("PolyPriorityList.txt");

		if (!Files.exists(configPath)) {
			try {
				Files.createDirectories(configDirPath);
				fillFileWithDefault(configPath);
			} catch (IOException e) {
				PolyPrioritize.LOGGER.error("[PolyPrioritize] failed to create config directory");
				e.printStackTrace();
			}
		}

		int c = 0;
		try (var bufReader = new BufferedReader(new FileReader(configPath.toFile()))) {
			while (true) {
				var line = bufReader.readLine();
				if (line == null) return c;
				line = line.trim();

				if (line.startsWith("#")) {
					continue;
				}

				Identifier id = Identifier.tryParse(line);
				if (id == null) {
					PolyPrioritize.LOGGER.warn("[PolyPrioritize] invalid identifier: "+line);
					continue;
				}

				if (!Registry.BLOCK.containsId(id)) {
					PolyPrioritize.LOGGER.warn("[PolyPrioritize] no such block: "+line);
					continue;
				}
				Block block = Registry.BLOCK.get(id);
				consumer.accept(block);
				c++;
			}
		} catch (IOException e) {
			PolyPrioritize.LOGGER.error("[PolyPrioritize] error reading config file");
			e.printStackTrace();
		}
		return c;
	}

	private static void fillFileWithDefault(Path file) throws IOException {
		Files.createFile(file);
		FileWriter writer = new FileWriter(file.toFile());
		writer.write("# This is a comment\n");
		writer.write("# Each line should contain the identifier of a single block\n");
		writer.write("# Example:\n");
		writer.write("# terrestria:cypress_sapling\n");
		writer.close();
	}
}
