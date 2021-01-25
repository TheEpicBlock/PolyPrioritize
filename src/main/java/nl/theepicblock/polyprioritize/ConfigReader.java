package nl.theepicblock.polyprioritize;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.function.Consumer;

public class ConfigReader {
	public static void readAndForEach(Consumer<Block> consumer) {
		Path configDirPath = FabricLoader.getInstance().getConfigDir();
		Path configPath = configDirPath.resolve("PolyPriorityList.txt");

		if (!Files.exists(configPath)) {
			try {
				Files.createDirectories(configDirPath);
				fillFileWithDefault(configPath);
			} catch (IOException e) {
				System.out.println("[PolyPrioritize] failed to create config directory");
				e.printStackTrace();
			}
		}

		try {
			Scanner scanner = new Scanner(configPath.toFile());

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();

				if (line.startsWith("#")) {
					continue;
				}

				Identifier id = Identifier.tryParse(line);
				if (id == null) {
					System.out.println("[PolyPrioritize] invalid identifier: "+line);
					continue;
				}

				Block block = Registry.BLOCK.get(id);
				if (block == Blocks.AIR) {
					//Air is the default block. This means that either:
					//The user entered an invalid id or the user entered an actual air block.
					//There should be no reason to enter an actual air block so we'll assume the first
					System.out.println("[PolyPrioritize] no such block: "+line);
					continue;
				}

				consumer.accept(block);
			}
		} catch (FileNotFoundException e) {
			System.out.println("[PolyPrioritize] error reading config file");
			e.printStackTrace();
		}
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
