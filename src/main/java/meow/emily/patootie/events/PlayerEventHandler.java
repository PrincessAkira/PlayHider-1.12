package meow.emily.patootie.events;

import com.mojang.realmsclient.gui.ChatFormatting;
import meow.emily.patootie.Emily;
import meow.emily.patootie.util.Utils;
import net.labymod.addon.AddonLoader;
import net.labymod.addons.voicechat.VoiceChat;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerEventHandler {

    // UUID VoiceCHat 1.12
    private final UUID vcUuid12 = UUID.fromString("24c0644d-ad56-4609-876d-6e9da3cc9794");
    // UUID VoiceChat 1.8
    private final UUID VcUuid8 = UUID.fromString("43152d5b-ca80-4b29-8f48-39fd63e48dee");

    @SubscribeEvent
    public void onPrePlayerRender(RenderPlayerEvent.Pre e) {
        Emily instance = Emily.getInstance();
        if (instance.isRenderPlayers()) {
            EntityPlayer enPlayer = e.getEntityPlayer();
            if (instance.isRenderPlayers() && !enPlayer.equals(Minecraft.getMinecraft().player)) {
                List<String> localPlayersToRender = instance.getPlayersToRenderString();
                if (!Utils.isNPC(enPlayer)) {
                    e.setCanceled(false);
                    for (String s : localPlayersToRender) {
                        if (s.equals(enPlayer.getGameProfile().getName())) {
                            e.setCanceled(true);
                            // LabyMod.getInstance().displayMessageInChat("§a" + instance.isVoiceexist());
                            if (instance.isVoiceexist()) {
                                if (instance.isMuted()) {
                                    //    LabyMod.getInstance().displayMessageInChat("§a" + "here");
                                    mute(enPlayer);
                                } else {
                                    unmute(enPlayer);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

// Needs fixing

    public void mute(EntityPlayer player) {
        Emily instance = Emily.getInstance();
        VoiceChat voiceChat = (VoiceChat) AddonLoader.getAddonByUUID(this.vcUuid12);
        voiceChat.getPlayerVolumes().put(player.getUniqueID(), 0);
        voiceChat.savePlayersVolumes();
    }

    public void unmute(EntityPlayer player) {
        Emily instance = Emily.getInstance();
        VoiceChat voiceChat = (VoiceChat) AddonLoader.getAddonByUUID(this.vcUuid12);
        UUID uuid = player.getGameProfile().getId();
        Map<UUID, Integer> volume = voiceChat.getPlayerVolumes();
        if (volume.containsKey(uuid)) {
            volume.put(uuid, voiceChat.getVolume(uuid));
        } else {
            volume.put(uuid, 100);
        }
        voiceChat.savePlayersVolumes();
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent e) {
        Emily instance = Emily.getInstance();
        if (instance.getKey() > -1) {
            if (Keyboard.isKeyDown(instance.getKey())) {
                LabyMod labymod = LabyMod.getInstance();
                if (instance.isRenderPlayers()) {
                    instance.setRenderPlayers(false);
                    instance.setMuted(false);
                    if (instance.isConfigMessage()) {
                        labymod.displayMessageInChat(ChatFormatting.GRAY + ">>" + "[" + ChatFormatting.AQUA + "PH" + ChatFormatting.WHITE + "]" + ChatFormatting.BOLD + ChatFormatting.GREEN + " on");
                    }
                } else {
                    instance.setRenderPlayers(true);
                    instance.setMuted(true);
                    if (instance.isConfigMessage()) {
                        labymod.displayMessageInChat(ChatFormatting.GRAY + ">>" + "[" + ChatFormatting.AQUA + "PH" + ChatFormatting.WHITE + "]" + ChatFormatting.BOLD + ChatFormatting.DARK_RED + " off");
                    }
                }
            }
        }
    }
}
