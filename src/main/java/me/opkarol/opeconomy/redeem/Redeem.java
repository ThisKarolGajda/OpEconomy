package me.opkarol.opeconomy.redeem;

import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Redeem implements Serializable {
    private final int maxUses;
    private int uses;
    private final int reward;
    private List<Player> used;

    public Redeem(int maxUses2, int uses2, int reward2){
        maxUses = maxUses2;
        uses = uses2;
        reward = reward2;
        used = new ArrayList<>();
    }

    public void setUsed(List<Player> used) {
        this.used = used;
    }

    public void setUses(int uses) {
        this.uses = uses;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public int getReward() {
        return reward;
    }

    public int getUses() {
        return uses;
    }

    public List<Player> getUsed() {
        return used;
    }
}
