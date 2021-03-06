package com.raymondlxtech.raiixdmserver.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.raymondlxtech.raiixdmserver.RaiixDMServer;
import com.raymondlxtech.raiixdmserver.RaiixDMServerRoom;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.Map;
import java.util.Set;

public class DMSReconnectCommand {
    private static final String name = "dmsreconnect";

    private RaiixDMServer theMod;
    public DMSReconnectCommand(RaiixDMServer m)
    {
        theMod = m;
    }

    public String getName(){return name;}

    public DMSReconnectCommand registry(CommandDispatcher theDispatcher)
    {
        theDispatcher.register(
                CommandManager.literal(getName()).then(CommandManager.argument("roomID", StringArgumentType.greedyString()).executes((commandContext) -> {
                    String[] args = new String[1];
                    args[0] = StringArgumentType.getString(commandContext, "roomID");
                    execute(commandContext, commandContext.getSource().getMinecraftServer(), commandContext.getSource().getEntity(), args);
                    return Command.SINGLE_SUCCESS;
                }))
        );
        return this;
    }

    public void execute(CommandContext<ServerCommandSource> cc, MinecraftServer server, Entity sender, String[] args)
    {
        if(args.length < 1) return;

        if(args[0].equals("all"))
        {
            Set<Map.Entry<String, RaiixDMServerRoom>> rooms = theMod.theRooms.entrySet();
            for(Map.Entry<String, RaiixDMServerRoom> kr : rooms)
            {
                if(!kr.getKey().equals("all"))
                {
                    args[0] = kr.getKey();
                    execute(cc, server, sender, args);
                }
            }
        }else
        {
            theMod.dmsDisconnectCommand.execute(server, sender, args);
            theMod.dmsConnectCommand.execute(cc, server, sender, args);
        }
    }
}
