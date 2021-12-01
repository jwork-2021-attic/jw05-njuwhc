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
package world;

import java.util.List;

import asciiPanel.AsciiPanel;

import java.awt.Color;

/**
 *
 * @author Aeranythe Echosong
 */
public class CreatureFactory {

    private World world;

    public CreatureFactory(World world) {
        this.world = world;
    }

    public synchronized Creature newPlayer(List<String> messages) {
        Creature player = new Creature(this.world, (char)2, AsciiPanel.brightWhite, 100, 20, 5, 9);
        BulletFactory bulletFactory = new BulletFactory(this.world,player);
        player.bulletFactory=bulletFactory;
        world.addAtEmptyLocation(player);
        new PlayerAI(player, messages);
        return player;
    }

    public synchronized Creature newFungus() {
        Creature fungus = new Creature(this.world, (char)3, AsciiPanel.green, 10, 4, 0, 0);
        world.addAtEmptyLocation(fungus);
        new FungusAI(fungus, this);
        return fungus;
    }

    public synchronized Monster newMonster(List<String> messages,char style,Color color){
        Monster monster = new Monster(this.world,style,color, 20, 20, 1, 0);
        world.addAtEmptyLocation(monster);
        BulletFactory bulletFactory = new BulletFactory(this.world,monster);
        monster.bulletFactory=bulletFactory;
        new MonsterAI(monster,messages);
        return monster;
    }

}
