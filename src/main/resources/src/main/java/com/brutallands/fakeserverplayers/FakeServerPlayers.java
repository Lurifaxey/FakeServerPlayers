   ```java
   package com.brutallands.fakeserverplayers;

   import org.bukkit.Bukkit;
   import org.bukkit.event.EventHandler;
   import org.bukkit.event.Listener;
   import org.bukkit.event.server.ServerListPingEvent;
   import org.bukkit.plugin.java.JavaPlugin;
   import com.github.retrooper.packetevents.PacketEvents;
   import com.github.retrooper.packetevents.event.PacketSendEvent;
   import com.github.retrooper.packetevents.protocol.packet.PacketType;
   import com.github.retrooper.packetevents.packetwrappers.server.play.WrappedPacketOutPlayerInfo;
   import com.github.retrooper.packetevents.packetwrappers.server.play.WrappedPacketOutPlayerInfo.PlayerInfoData;
   import com.github.retrooper.packetevents.event.PacketPriority;
   import java.util.ArrayList;
   import java.util.List;
   import java.util.UUID;

   public class FakeServerPlayers extends JavaPlugin implements Listener {
       int fakePlayers = 70, maxPlayers = 100;
       @Override public void onEnable() {
           Bukkit.getPluginManager().registerEvents(this,this);
           PacketEvents.create(this);
           PacketEvents.get().getEventManager().registerListener(this, PacketSendEvent.class, e->{
               if(e.getPacketType()==PacketType.Play.Server.PLAYER_INFO) {
                   WrappedPacketOutPlayerInfo p = new WrappedPacketOutPlayerInfo(e.getPacket());
                   List<PlayerInfoData> list=new ArrayList<>(p.getEntries());
                   for(int i=0;i<fakePlayers;i++){
                       UUID id=UUID.nameUUIDFromBytes(("FakePlayer"+i).getBytes());
                       list.add(new PlayerInfoData(id,"FakePlayer"+i,0,WrappedPacketOutPlayerInfo.Action.ADD_PLAYER));
                   }
                   p.setEntries(list); e.setPacket(p.getRawPacket());
               }
           }, PacketPriority.HIGH);
           getLogger().info("FakeServerPlayers v1.0 enabled");
       }
       @Override public void onDisable(){PacketEvents.get().terminate();}
       @EventHandler public void onPing(ServerListPingEvent e){
           int real=Bukkit.getOnlinePlayers().size();
           e.setMaxPlayers(maxPlayers);
           e.setNumPlayers(real+fakePlayers);
       }
   }
   ```
