package noobanidus.mods.lootr_ftb.common.impl.teams;

import com.google.auto.service.AutoService;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.event.TeamEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import noobanidus.mods.lootr.common.api.PlatformAPI;
import noobanidus.mods.lootr.common.api.team.ITeamResolver;

import java.util.UUID;

@AutoService(ITeamResolver.class)
public class FTBTeamsResolver implements ITeamResolver {
  private final ResourceLocation IDENTIFIER = ResourceLocation.fromNamespaceAndPath("lootr", "ftb");

  @Override
  public UUID resolveServerPlayer(Player player) {
    var team = FTBTeamsAPI.api().getManager().getTeamForPlayer((ServerPlayer) player).orElseThrow();
    if (team.getMembers().size() == 1) {
      return player.getUUID();
    }

    return team.getTeamId();
  }

  @Override
  public UUID resolveClientPlayer(Player player) {
    var team = FTBTeamsAPI.api().getClientManager().getTeamForPlayer(player).orElseThrow();
    if (team.getMembers().size() == 1) {
      return player.getUUID();
    }

    return team.getTeamId();
  }

  @Override
  public void init() {
    TeamEvent.PLAYER_CHANGED.register((event) ->
        PlatformAPI.syncAfterTeamChange(event.getPlayer()));
  }

  @Override
  public ResourceLocation resolverId() {
    return IDENTIFIER;
  }
}
