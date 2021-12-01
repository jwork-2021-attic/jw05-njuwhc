package world;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

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
/**
 *
 * @author Aeranythe Echosong
 */
public class World {

    private Tile[][] tiles;
    private int width;
    private int height;
    private List<Creature> creatures;
    private List<Bullet> bullets;

    public static final int TILE_TYPES = 2;

    public World(Tile[][] tiles) {
        this.tiles = tiles;
        this.width = tiles.length;
        this.height = tiles[0].length;
        this.creatures = new ArrayList<>();
        this.bullets=new ArrayList<>();
    }

    public synchronized Tile tile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return Tile.BOUNDS;
        } else {
            return tiles[x][y];
        }
    }

    public synchronized char glyph(int x, int y) {
        return tiles[x][y].glyph();
    }

    public synchronized Color color(int x, int y) {
        return tiles[x][y].color();
    }

    public synchronized int width() {
        return width;
    }

    public synchronized int height() {
        return height;
    }

    public synchronized void dig(int x, int y) {
        if (tile(x, y).isDiggable()) {
            tiles[x][y] = Tile.FLOOR;
        }
    }

    public synchronized void addAtEmptyLocation(Creature creature) {
        int x;
        int y;

        do {
            x = (int) (Math.random() * this.width);
            y = (int) (Math.random() * this.height);
        } while (!tile(x, y).isGround() || this.creature(x, y) != null);

        creature.setX(x);
        creature.setY(y);
        creature.setDirection(-1);

        this.creatures.add(creature);
    }
    public synchronized void addBulletAt(Bullet bullet){
        int mx=0,my=0;
        int x=bullet.ownerX();
        int y=bullet.ownerY();
        switch(bullet.flyDirection())
        {
            case(1):
                mx=-1;
                my=0;
                break;
            case(2):
                mx=1;
                my=0;
                break;
            case(3):
                mx=0;
                my=-1;
                break;
            case(4):
                mx=0;
                my=1;
                break;

        }

        while (!tile(x+mx, y+my).isGround() || this.creature(x+mx, y+my) != null)
        {
            if(!tile(x+mx, y+my).isGround()){this.dig(x+mx,y+my);}
            if(this.creature(x+mx, y+my) != null){
                while(this.creature(x+mx, y+my) != null)
                {bullet.attack(this.creature(x+mx,y+my));}
                //mx=mx+mx;my=my+my;
            }
        }

        bullet.setX(x+mx);
        bullet.setY(y+my);

        this.bullets.add(bullet);
    }


    public synchronized Creature creature(int x, int y) {
        for (Creature c : this.creatures) {
            if (c.x() == x && c.y() == y) {
                return c;
            }
        }
        return null;
    }

    public synchronized List<Creature> getCreatures() {
        return this.creatures;
    }

    public synchronized List<Bullet> getBullets() {
        return this.bullets;
    }

    public synchronized void remove(Creature target) {
        this.creatures.remove(target);
        return;
    }

    public synchronized void remove(Bullet target) {
        this.bullets.remove(target);
        return;
    }

    public void update() {
        ArrayList<Creature> toUpdate = new ArrayList<>(this.creatures);

        for (Creature creature : toUpdate) {
            creature.update();
        }
    }
}
