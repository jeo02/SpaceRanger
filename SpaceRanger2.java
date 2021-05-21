import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.Font;
import java.util.ArrayList;
import java.applet.Applet;
import java.applet.AudioClip;

public class SpaceRanger2 extends JComponent implements KeyListener, MouseListener{
    //instance variables
    private final int WIDTH, HEIGHT;
    private Rectangle player, whiteBar, healthBar, boss;
    private ArrayList<Circle> bullets, asteroids, enemyBullets;
    
    //all time related
    private long msElapsed, preMsElapsed, preMsElapsed2, temp, temp2, temp3;
    
    private boolean left, right, space, blink, end, won, bossTime;
    private int lives, level, aTime, bTime, bossbSpeed;
    private double bSpeed;
    
    //sound
    private final AudioClip BACK_MUSIC;
    private final AudioClip SHOT;
    private final AudioClip GAME_OVER;
    private final AudioClip VICTORY;
    
    //Default Constructor
    public SpaceRanger2()
    {
        //initializing instance variables
        WIDTH = 525;
        HEIGHT = 1000;
        player = new Rectangle(WIDTH/2 - 30, HEIGHT - 125, 70, 70, 25, 0, 1000);
        boss = new Rectangle(WIDTH/2-60, -100, 100, 100, 0, 5, 5000);
        whiteBar = new Rectangle(160, 960, 200,25,0,0,100);
        healthBar = new Rectangle(160, 960, 200, 25, 0, 0, 100);
        bullets = new ArrayList<Circle>();
        asteroids = new ArrayList<Circle>();
        enemyBullets = new ArrayList<Circle>();
        msElapsed = 0;
        preMsElapsed = 0;
        preMsElapsed2 = 0;
        level = 0;
        aTime = 1500;
        bTime = 800;
        bSpeed = 8;
        bossbSpeed = 8;
        temp = 0;
        temp2 = 0;
        temp3 = 0;
        lives = 5;
        won = false;
        left = false; 
        right = false; 
        space = false;
        blink = false;
        end = false;
        bossTime = false;
        BACK_MUSIC = Applet.newAudioClip(getClass().getResource("Light-Years_v001.wav"));
        SHOT = Applet.newAudioClip(getClass().getResource("lazer7.wav"));
        GAME_OVER = Applet.newAudioClip(getClass().getResource("game_over.wav"));
        VICTORY = Applet.newAudioClip(getClass().getResource("victoryff.wav"));
        BACK_MUSIC.loop();
        
        
        //Setting up the GUI
        JFrame gui = new JFrame(); 
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setTitle("Space Shooter");
        gui.setPreferredSize(new Dimension(WIDTH + 5, HEIGHT + 30));
        gui.setResizable(false);
        gui.getContentPane().add(this);
        //Place for buttons
        gui.pack();
        gui.setLocationRelativeTo(null);
        gui.setVisible(true);
        gui.addKeyListener(this);
        gui.addMouseListener(this);
    }
    
    //This method determines what keys are being pressed
    public void keyPressed(KeyEvent e) 
    {
        //getting the key pressed
        int key = e.getKeyCode();
        
        //checking what key was pressed
        if(key == 37)
            left = true;
        else if(key == 39)
            right = true;
        if(key == 32){
            space = true;  
            SHOT.play();
        }
    } 
    
    // depending on what key/s are being pressed it does a action
    public void keyAction()
    {
        if(left == true && msElapsed > preMsElapsed2 + 25)
        {
            player.moveLeft();
            preMsElapsed2 = msElapsed;
        }
        if(right == true && msElapsed > preMsElapsed2 + 25)
        {
            player.moveRight();
            preMsElapsed2 = msElapsed;
        }
        //Formula to the right makes sure that the distance will remain 150 even if the speed changes
        if(space == true && msElapsed > preMsElapsed + (200/(int)bSpeed)*10)
        {
            //Adding a new bullet to the ArrayList
            bullets.add(new Circle(0,0,8,0,(int) bSpeed,10,1));
            
            //putting the newly added bullet at the default location
            bullets.get(bullets.size()-1).setX(20 + player.getX() - (bullets.get(bullets.size()-1).getDiam()/2));
            bullets.get(bullets.size()-1).setY(player.getY() - (bullets.get(bullets.size()-1).getDiam()/2));
            
            //Doing the same as stated above but for 2nd bullet, adding .getW sets it to the other corner of the player
            bullets.add(new Circle(0,0,8,0,(int) bSpeed,10,1));
            bullets.get(bullets.size()-1).setX((player.getX() + player.getW()) - (bullets.get(bullets.size()-1).getDiam()/2) - 20);
            bullets.get(bullets.size()-1).setY(player.getY() - (bullets.get(bullets.size()-1).getDiam()/2));

            //recording the time to make sure theres no spam of space button
            preMsElapsed = msElapsed;
        }
        
    } 
    
    //Once a button is released it sets that boolean to false
    public void keyReleased(KeyEvent e)
    {
        //getting the key pressed
        int key = e.getKeyCode();
        
        //checking what key was released
        if(key == 37)
            left = false;
        if(key == 39)
            right = false;
        if(key == 32)
            space = false; 
    }
    
    //randomly spawning asteroids
    public void asteroidSpawn()
    {
        int x = (int) (Math.random() * 5);
        if(temp + aTime < msElapsed)
        {
            asteroids.add(new Circle(0,10,90,0,2,10,50));
            if(x == 0)
               asteroids.get(asteroids.size()-1).setX(7);
            if(x == 1)
               asteroids.get(asteroids.size()-1).setX(112);    
            if(x == 2)
               asteroids.get(asteroids.size()-1).setX(217);
            if(x == 3)
               asteroids.get(asteroids.size()-1).setX(322);
            if(x == 4)
               asteroids.get(asteroids.size()-1).setX(427);
            temp = msElapsed;
        }
        
    }
    
    //spawns the boss bullets 
    public void bossBullets()
    {
        int x = (int) (Math.random() * 10);
        if(temp3 + bTime < msElapsed)
        {
            enemyBullets.add(new Circle(0,250,20,0,bossbSpeed,5,1));
            if(x == 0)
                enemyBullets.get(enemyBullets.size()-1).setX(50);
            if(x == 1)
                enemyBullets.get(enemyBullets.size()-1).setX(100);
            if(x == 2)  
                enemyBullets.get(enemyBullets.size()-1).setX(150);
            if(x == 3)
                enemyBullets.get(enemyBullets.size()-1).setX(200);
            if(x == 4)
                enemyBullets.get(enemyBullets.size()-1).setX(250);
            if(x == 5)
                enemyBullets.get(enemyBullets.size()-1).setX(300);
            if(x == 6)
                enemyBullets.get(enemyBullets.size()-1).setX(350);
            if(x == 7)
                enemyBullets.get(enemyBullets.size()-1).setX(400);
            if(x == 8)
                enemyBullets.get(enemyBullets.size()-1).setX(450);
            if(x == 9)
                enemyBullets.get(enemyBullets.size()-1).setX(510);
            
            temp3 = msElapsed;
        }
    }
    
    //Move boss
    public void bossMove()
    {
        if(boss.getY() < 100 && bossTime)
            boss.setY(boss.getY() + boss.getVY());
        if(boss.getY() > -100 && !bossTime)
            boss.setY(boss.getY() - boss.getVY());
    }
    //Boss method
    public void level(){
        
        //level 1 
        if(msElapsed >= 30000 && msElapsed <= 50000){
            bossTime = true;
            level = 1;
            bossbSpeed = 8;
            aTime = 1500;
            bTime = 700;
        }
        //level 2
        else if(msElapsed >= 80000 && msElapsed <= 100000){
            bossTime = true;
            level = 2;
            bossbSpeed = 10;
            aTime = 1400;
            bTime = 600;
        }
        //level 3
        else if(msElapsed >= 130000 && msElapsed <= 150000){
            bossTime = true;
            level = 3;
            bossbSpeed = 12;
            aTime = 1300;
            bTime = 550;
        }
        //level 4
        else if(msElapsed >= 180000 && msElapsed <= 200000){
            bossTime = true;
            level = 4;
            bossbSpeed = 14;
            aTime = 1150;
            bTime = 400;
            asteroidSpawn();
        }
        //level 5
        else if(msElapsed >= 230000 && msElapsed <= 250000){
            bossTime = true;
            level = 5;
            bossbSpeed = 16;
            aTime = 900;
            bTime = 300;
            asteroidSpawn();
        }
        else if(msElapsed > 250000){
            level = 6;
        }
        else{
            bossTime = false;
        }    
    }
    //Distance Formula
    public static int distance(int x1, int y1, int x2, int y2){
        int xDiff = x2 - x1;
        double yDiff = y2 - y1;
        double distance = Math.sqrt((xDiff * xDiff) + (yDiff * yDiff));
        return (int) distance;
    }
    
    //Handles a collision between objects
    public void handleCollision()
    {   
        //Runs through every astroid and bullet to see if there are any 2 that are at the same location
        //Creates new Circle ArrayList with the same length as asteriods in order to not change the number of iterations when removing
        for(Circle a : new ArrayList <Circle> (asteroids)){
            for(Circle b : new ArrayList <Circle> (bullets)){
                //gets the future center position of the circle and plugging it into distance formula
                if(distance((2*(a.getX()+a.getVX())+ a.getDiam())/2, (2*(a.getY()+a.getVY())+ a.getDiam())/2, (2*(b.getX()+b.getVX())+ b.getDiam())/2, (2*(b.getY()+b.getVY())+ b.getDiam())/2) < a.getDiam()/2){
                    a.setHealth(a.getHealth() - b.getDamage());
                    
                    //remove asteriods and bullets accordingly
                    if(a.getHealth() <= 0){
                        asteroids.remove(a);
                        
                        //increasing the speed steadily everytime a asteriod is removed
                        bSpeed += .015;
                    }
                    bullets.remove(b);
                }
                
                //remove out of bound bullets
                if(b.getY() < 0)
                    bullets.remove(b);
            }
            //remove out of bound asteroids
            if(a.getY() > 1000){
                    
                    asteroids.remove(a);
                    healthBar.setHealth(healthBar.getHealth()- a.getDamage());
                    healthBar.setW(healthBar.getHealth()*2);
            }
            
            //collision between astroid and player
            if(!blink && distance((2*(a.getX()+a.getVX())+ a.getDiam())/2, (2*(a.getY()+a.getVY())+ a.getDiam())/2, (2*(player.getX()+player.getVX())+ player.getW())/2, (2*(player.getY()+player.getVY())+ player.getW())/2) < player.getW()){
                //if lives is at 0 subtract from health bar
                if(lives > 0){
                    lives -=1;
                    blink = true;
                }
                else{
                    healthBar.setHealth(healthBar.getHealth()- a.getDamage());
                    healthBar.setW(healthBar.getHealth()*2);
                }
                
                temp2 = msElapsed;
                asteroids.remove(a);
            }
            
        }
        
        //Deals with collisions that only happen during boss time
        if(bossTime){
            //Collsion boss-player bullet
            for(Circle b : new ArrayList <Circle> (bullets)){
                int cX, cY, bossX, bossY;
                //center of bullets
                cX = (2*((b.getX() + b.getVX()) + b.getDiam()))/2;
                cY = (2*((b.getY() + b.getVY()) + b.getDiam()))/2;
                bossX = boss.getX() + boss.getW();
                bossY = boss.getY() + boss.getH();
                
                //collsion
                if(cX >= bossX-100 && cX <= bossX && cY - b.getDiam()/2 <= bossY){
                    boss.setHealth(boss.getHealth() - b.getDamage());
                    bullets.remove(b);
                }
                
                //remove out of bound bullets
                if(b.getY() < 0)
                    bullets.remove(b);
            }
            
            //Collision player-boss bullet
            for(Circle b : new ArrayList <Circle> (enemyBullets)){
                int cX, cY, playerX, playerY;
                //center of bullets
                cX = (2*((b.getX() + b.getVX()) + b.getDiam()))/2;
                cY = (2*((b.getY() + b.getVY()) + b.getDiam()))/2;
                playerX = player.getX();
                playerY = player.getY();
                
                //collsion
                if(!blink && cX >= playerX && cX <= playerX+100 && cY - b.getDiam()/2 >= playerY){
                    if(lives > 0){
                        lives--;
                        blink = true;
                    }
                    else{
                        healthBar.setHealth(healthBar.getHealth()- b.getDamage());
                        healthBar.setW(healthBar.getHealth()*2);
                    }
                    enemyBullets.remove(b);
                    temp2 = msElapsed;
                }
                
                //remove out of bound bullets
                if(b.getY() > 1000)
                    enemyBullets.remove(b);
            }
        }
    }
    
    //Determines if the game is over
    public void isGameOver(){
        if(lives <= 0 && healthBar.getHealth() <= 0)
            end = true;
        else if(level == 6 || boss.getHealth() <= 0)
            won = true;
    }
    
    //sets the screen to game over
    public void gameOver(Graphics g){
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        g.setColor(Color.DARK_GRAY);
        g.setFont(small);
        g.drawString(msg, WIDTH/2-50, HEIGHT/2);
        
        //music
        BACK_MUSIC.stop();
        GAME_OVER.play();
    }
    
    //sets the screen to you won!
    public void gameWon(Graphics g){
        String msg = "You Won!";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        g.setColor(Color.DARK_GRAY);
        g.setFont(small);
        g.drawString(msg, WIDTH/2-50, HEIGHT/2);
        BACK_MUSIC.stop();
        VICTORY.play();
    }
    //All your UI drawing goes in here
    public void paintComponent(Graphics g)
    {
        if(!end && !won){
            //Drawing the wallpaper to be the background
            Image imgBackground = Toolkit.getDefaultToolkit().getImage(getClass().getResource("SpaceWallpaper.jpg"));
            g.drawImage(imgBackground, 0, 0, WIDTH, HEIGHT, this);
            
            Font small = new Font("Helvetica", Font.BOLD, 14);
            g.setColor(Color.WHITE);
            g.setFont(small);
            g.drawString("Lives: " + lives + "  Level: " + level + "                                                                     Boss Health: " + boss.getHealth(), 2, 14);
            
            //Drawing the user-controlled rectangle, blink indicates that it was hit and gives the user a couple seconds without getting hit
            if(!blink){
                Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("SpaceShip.png"));
                g.drawImage(img ,player.getX(), player.getY(), player.getW(), player.getH(), this);
            }
            if(blink){
                g.setColor(new Color(0,1,1,.1f));
                g.fillRect(player.getX(), player.getY(), player.getW(), player.getH());
            }
        
            //Draws Boss
            Image imgBoss = Toolkit.getDefaultToolkit().getImage(getClass().getResource("boss.gif"));
            g.drawImage(imgBoss ,boss.getX(), boss.getY(), boss.getW(), boss.getH(), this);    
        
            //Drawing the health bar 
            g.setColor(Color.WHITE);
            g.fillRect(whiteBar.getX(), whiteBar.getY(), whiteBar.getW(), whiteBar.getH());
            g.setColor(Color.GREEN);
            g.fillRect(healthBar.getX(), healthBar.getY(), healthBar.getW(), whiteBar.getH());
        
            //Drawing the bullets
            g.setColor(Color.ORANGE);
            for (Circle bullet : bullets) {
                g.fillOval(bullet.getX(), bullet.getY(), bullet.getDiam(), bullet.getDiam());
            }
        
            //drawing the enemy bullets
            g.setColor(Color.RED);
            for(Circle bullet : enemyBullets){
                g.fillOval(bullet.getX(), bullet.getY(), bullet.getDiam(), bullet.getDiam());
            }   
        
            //Drawing the Asteroids
            g.setColor(Color.WHITE);
            for (Circle asteroid : asteroids) {
                Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("Asteroid.png"));
                g.drawImage(img ,asteroid.getX(), asteroid.getY(), asteroid.getDiam(), asteroid.getDiam(), this);
            }
        }
        else if(end)
            gameOver(g);
        else if(won)
            gameWon(g);

    }
    public void loop()
    {
        //making the bullets move
        for(Circle bullet : bullets)
            bullet.setY(bullet.getY() - bullet.getVY());
        
        //making the asteroids move downwards
        for(Circle asteroid : asteroids)
            asteroid.setY(asteroid.getY() + asteroid.getVY());
        
        //making the enemy bullets move
        for(Circle bullet : enemyBullets)
            bullet.setY(bullet.getY() + bullet.getVY());
        
        //handling when the player collides with the edges
        if(player.getX() <= 0){
            player.setX(0);
        }
        if(player.getX() + player.getW() >= WIDTH){
            player.setX(WIDTH - player.getW());
        }
        
        //Checking to see if blink is true so that it changes color for 3000ms
        if(blink && msElapsed > temp2+2000){
            blink = false;
        }
        
            
        //time
        msElapsed+=10;
        
        //handles the actions when a key is pressed
        keyAction();
        
        if(!bossTime)
            asteroidSpawn();
        else
            bossBullets();
        handleCollision();
        
        //Boss
        bossMove();
        level();
        
        if(!end || !won)
            isGameOver();
        //Do not write below this
        repaint();
        
        
    }
    //These methods are required by the compiler.  
    //You might write code in these methods depending on your goal.
    public void keyTyped(KeyEvent e) 
    {
    }
    public void mousePressed(MouseEvent e)
    {
    }
    public void mouseReleased(MouseEvent e)
    {
    }
    public void mouseClicked(MouseEvent e)
    {
    }
    public void mouseEntered(MouseEvent e)
    {
    }
    public void mouseExited(MouseEvent e)
    {
    }
    public void start(final int ticks){
        Thread gameThread = new Thread(){
            public void run(){
                while(true){
                    loop();
                    try{
                        Thread.sleep(1000 / ticks);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };	
        gameThread.start();
    }

    public static void main(String[] args)
    {
        SpaceRanger2 g = new SpaceRanger2();
        g.start(60);
    }
}