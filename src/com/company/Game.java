package com.company;

import com.entity.Player;
import com.gfx.Sprite;
import com.gfx.SpriteSheet;
import com.input.KeyInput;
import com.tile.Wall;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable{
    public static final int WIDTH=270;
    public static final int HEIGHT=WIDTH/14*10;
    public static final int SCALE=4;
    public static final String TITLE="Mario";

    private Thread thread;
    private boolean running=false;

    public static Handler handler;
    public static SpriteSheet sheet;

    public static Sprite grass;
    public static Sprite player;


    public Game(){
        Dimension size=new Dimension(WIDTH*SCALE, HEIGHT*SCALE);
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
    }
    private void init(){
        handler=new Handler();
        //sheet=new SpriteSheet("/grid.xcf");

        addKeyListener(new KeyInput());

        //grass=new Sprite(sheet, 2,1);
        //player=new Sprite(sheet, 1 ,1);

        handler.addEntity(new Player(300, 512, 64, 64, true, Id.player, handler));
        handler.addTile(new Wall(200,200,64,64, true, Id.wall, handler));
    }
    public static void main(String [] args) {
        Game game=new Game();
        JFrame frame=new JFrame(TITLE);
        frame.add(game);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        game.start();

    }
    private synchronized void start(){
        if(running) return;
        running=true;
        thread=new Thread(this, "Thread");
        thread.start();

    }
    private synchronized void stop(){
        if(!running)return;
        running=false;
        try{
            thread.join();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        init();
        requestFocus();
        long lastTime=System.nanoTime();
        long timer=System.currentTimeMillis();
        double delta=0;
        double ns=1000000000.0/60.0;
        int frames=0;
        int ticks=0;
        while(running){
            long now=System.nanoTime();
            delta+=(now-lastTime)/ns;
            lastTime=now;
            while(delta>=1){
                tick();
                ticks++;
                delta--;
            }
            render();
            frames++;
            if(System.currentTimeMillis()-timer>1000){
                timer+=1000;
                //frames per seconds
                frames=0;
                ticks=0;
            }
        }
        stop();
    }
    public void render(){
        //buffer strategy
        BufferStrategy bs=getBufferStrategy();
        if(bs==null){
            createBufferStrategy(3);
            return;
        }
        Graphics g= bs.getDrawGraphics();
        //colorare background
        //g.setColor(new Color(222, 9, 165));
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        //dreptunghi in mijloc
        /*g.setColor(Color.RED);
        g.fillRect(200, 200, getWidth()-400, getHeight()-400);
        */
        handler.render(g);
        g.dispose();
        bs.show();

    }
    public void tick(){
        handler.tick();
    }

}
