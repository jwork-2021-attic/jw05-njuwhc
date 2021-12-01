/*
 * Copyright (C) 2015 Aeranythe Echosong
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package screen;

import world.*;
import asciiPanel.AsciiPanel;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Aeranythe Echosong
 */
public class PlayScreen implements Screen {

    private World world;
    private Creature player;
    private int screenWidth;
    private int screenHeight;
    private List<String> messages;
    private List<String> oldMessages;

    public PlayScreen() {
        //this.screenWidth = 80;
        //this.screenHeight = 24;
        this.screenWidth = 50;
        this.screenHeight = 50;
        createWorld();
        this.messages = new ArrayList<String>();
        this.oldMessages = new ArrayList<String>();

        CreatureFactory creatureFactory = new CreatureFactory(this.world);
        createCreatures(creatureFactory);

    }

    private void createCreatures(CreatureFactory creatureFactory) {
        this.player = creatureFactory.newPlayer(this.messages);  //创造一个玩家

        //for (int i = 0; i < 5; i++) {
          //  creatureFactory.newFungus();
        //}
        int num=225;
        Color[] colors={AsciiPanel.brightRed,AsciiPanel.brightYellow,AsciiPanel.brightGreen,AsciiPanel.brightBlue,AsciiPanel.brightMagenta};
        ExecutorService exec=Executors.newCachedThreadPool();
        for(int i = 0; i < 5; i++) {exec.submit(creatureFactory.newMonster(this.messages,(char)num++,colors[i]));}//创造若干怪物，线程池管理线程
        exec.shutdown();
        //Thread monsterThread = new Thread(creatureFactory.newMonster());
        //monsterThread.start();
    }

    private void createWorld() {
        //world = new WorldBuilder(90, 31).makeCaves().build();
        world = new WorldBuilder(50, 50).makeCaves().build();
    }

    private void displayTiles(AsciiPanel terminal, int left, int top) {
        // Show terrain
        for (int x = 0; x < screenWidth; x++) {
            for (int y = 0; y < screenHeight; y++) {
                int wx = x + left;
                int wy = y + top;

                if (player.canSee(wx, wy)) {
                    terminal.write(world.glyph(wx, wy), x, y, world.color(wx, wy));
                } else {
                    terminal.write(world.glyph(wx, wy), x, y, Color.DARK_GRAY);
                }
            }
        }
        // Show creatures
        for (Creature creature : world.getCreatures()) {
            if (creature.x() >= left && creature.x() < left + screenWidth && creature.y() >= top
                    && creature.y() < top + screenHeight) {
                if (player.canSee(creature.x(), creature.y())) {
                    terminal.write(creature.glyph(), creature.x() - left, creature.y() - top, creature.color());
                }
            }
        }

        // Show bullets
        for (Bullet bullet : world.getBullets()) {
            if (bullet.x() >= left && bullet.x() < left + screenWidth && bullet.y() >= top
                    && bullet.y() < top + screenHeight) {
                if (player.canSee(bullet.x(), bullet.y())) {
                    terminal.write(bullet.glyph(), bullet.x() - left, bullet.y() - top,bullet.color());
                }
            }
        }

        // Creatures can choose their next action now
        world.update();
    }

    private void displayMessages(AsciiPanel terminal, List<String> messages) {
        int top = this.screenHeight - messages.size();
        for (int i = 0; i < messages.size(); i++) {
            terminal.write(messages.get(i), 51, top + i + 1);
        }
        this.oldMessages.addAll(messages);
        //messages.clear();
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        // Terrain and creatures
        displayTiles(terminal, getScrollX(), getScrollY());
        // Player
        terminal.write(player.glyph(), player.x() - getScrollX(), player.y() - getScrollY(), player.color());
        // Stats
        String stats = String.format("%3d/%3d hp", player.hp(), player.maxHP());
        terminal.write(stats, 1, 50);
        // Messages
        displayMessages(terminal, this.messages);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if(this.player.hp()<1)
            return new LoseScreen();
        switch (key.getKeyCode()) {
            case KeyEvent.VK_LEFT:
            if(player.x()>0)
            {
                player.setDirection(1);
                new Thread(player.bulletFactory.newBullet((char) 27,player.color())).start();
                player.moveBy(-1, 0);
            }
                break;
            case KeyEvent.VK_RIGHT:
            if(player.x()<this.world.width()-1)
            {
                player.setDirection(2);
                new Thread(player.bulletFactory.newBullet((char) 26,player.color())).start();
                player.moveBy(1, 0);
            }
                break;
            case KeyEvent.VK_UP:
            if(player.y()>0)
            {
                player.setDirection(3);
                new Thread(player.bulletFactory.newBullet((char) 24,player.color())).start();
                player.moveBy(0, -1);
            }
                break;
            case KeyEvent.VK_DOWN:
            if(player.y()<this.world.height()-1)
            {
                player.setDirection(4);
                new Thread(player.bulletFactory.newBullet((char) 25,player.color())).start();
                player.moveBy(0, 1);
            }
                break;
            case KeyEvent.VK_R:
                return new PlayScreen();
            case KeyEvent.VK_W:
                return new WinScreen();
                
        }
        return this;
    }

    public int getScrollX() {
        return Math.max(0, Math.min(player.x() - screenWidth / 2, world.width() - screenWidth));
    }

    public int getScrollY() {
        return Math.max(0, Math.min(player.y() - screenHeight / 2, world.height() - screenHeight));
    }

}
