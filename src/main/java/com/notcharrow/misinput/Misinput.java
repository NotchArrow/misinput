package com.notcharrow.misinput;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.notcharrow.misinput.config.ConfigManager;
import com.notcharrow.misinput.helper.SuggestionBuilder;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.Collections;
import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class Misinput implements ModInitializer {
	private static final MinecraftClient client = MinecraftClient.getInstance();

	@Override
	public void onInitialize() {
		ConfigManager.loadConfig();

		ClientCommandRegistrationCallback.EVENT.register((dispatcher,
														  registryAccess) -> {
			dispatcher.register(
				literal("misinput")
					.then(literal("add")
						.then(argument("username", StringArgumentType.string())
							.executes(Misinput::addUsername)))
					.then(literal("remove")
						.then(argument("username", StringArgumentType.string())
								.suggests(SuggestionBuilder.createSuggestionProvider(ConfigManager.config.usernames))
								.executes(Misinput::removeUsername)))
					.then(literal("list")
						.executes(Misinput::listUsernames))
			);
		});
	}

	private static int addUsername(CommandContext<FabricClientCommandSource> context) {
		String username = StringArgumentType.getString(context, "username");
		ConfigManager.config.usernames.add(username.toLowerCase());
		ConfigManager.saveConfig();

		if (client.player != null) {
			client.player.sendMessage(Text.of("Username: " + username + " added."), false);
		}

		return 1;
	}

	private static int removeUsername(CommandContext<FabricClientCommandSource> context) {
		String username = StringArgumentType.getString(context, "username");
		ConfigManager.config.usernames.remove(username.toLowerCase());
		ConfigManager.saveConfig();

		if (client.player != null) {
			client.player.sendMessage(Text.of("Username: " + username + " removed."), false);
		}

		return 1;
	}

	private static int listUsernames(CommandContext<FabricClientCommandSource> context) {
		List<String> usernames = ConfigManager.config.usernames;
		Collections.sort(usernames);

		StringBuilder output = new StringBuilder("Usernames: ");
		for (String username: usernames) {
			if (usernames.indexOf(username) == usernames.size() - 1) {
				output.append(username);
			} else {
				output.append(username).append(", ");
			}
		}
		if (client.player != null) {
			client.player.sendMessage(Text.of(String.valueOf(output)), false);
		}

		return 1;
	}
}