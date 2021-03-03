package com.tile;

import com.company.Game;
import com.company.Handler;
import com.company.Id;

import java.awt.*;

public class Wall extends Tile{
    public Wall(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
        super(x, y, width, height, solid, id, handler);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x,y,width,height);
       // g.drawImage(Game.grass.getBufferedImage(), x,y, width, height, null);
    }

    @Override
    public void tick() {

    }

}
