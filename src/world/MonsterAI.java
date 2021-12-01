package world;

import java.util.List;

public class MonsterAI extends CreatureAI {

    private List<String> messages;

    public MonsterAI(Creature creature, List<String> messages) {
        super(creature);
        this.messages = messages;
        //TODO Auto-generated constructor stub
    }
    
    public synchronized void onEnter(int x, int y, Tile tile) {
        if (tile.isGround()) {
            creature.setX(x);
            creature.setY(y);
        } else if (tile.isDiggable()) {
            creature.dig(x, y);
        }
    }

    public synchronized void onNotify(String message) {
        this.messages.add(message);
    }
    
}


