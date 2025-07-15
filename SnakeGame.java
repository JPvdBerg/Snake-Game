import java.awt.*;
import java.awt.event.*;
//import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener
{
    private class Tile
    {
        int x;
        int y;

        Tile(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    //Snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //Food
    Tile food;
    Random random;

    //Game Logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    public SnakeGame()
    {
        this(600,600);
    }

    public SnakeGame(int boardWidth, int boardHeight)
    {
        setBoardWidth(boardWidth);
        setBoardHeight(boardHeight);
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10,10);
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 1;

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    public void setBoardWidth(int boardWidth)
    {
        this.boardWidth = boardWidth;
    }

    public void setBoardHeight(int boardHeight)
    {
        this.boardHeight = boardHeight;
    }

    public int getBoardWidth()
    {
        return boardWidth;
    }

    public int getBoardHeight()
    {
        return boardHeight;
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g)
    {
        //GridLines
        for (int k = 0; k < boardHeight/tileSize; k++)
        {
           // g.drawLine(k*tileSize, 0, k*tileSize, boardHeight);
           // g.drawLine(0, k*tileSize, boardWidth, k*tileSize);
        }

        //Food
        g.setColor(Color.RED);
        //g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize,true);
        
        //Snake head
        g.setColor(Color.GREEN);
        //g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize , tileSize, tileSize);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize , tileSize, tileSize, true);

        //Snake body
        for (int k = 0; k < snakeBody.size(); k++)
        {
            Tile snakePart = snakeBody.get(k);
            //g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }

        //Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver)
        {
            g.setColor(Color.RED);
            g.drawString("Game Over: Score = " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
        else
        {
            g.setColor(Color.GREEN);
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
    }

    public void placeFood()
    {
        food.x = random.nextInt(boardWidth/tileSize);
        food.y = random.nextInt(boardHeight/tileSize);
    }

    public boolean collision(Tile tile1, Tile tile2)
    {
        if (tile1.x == tile2.x && tile1.y == tile2.y)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void move()
    {
        //Eat food
        if (collision(snakeHead, food))
        {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }
        
        //Snake Body
        for (int k = snakeBody.size()-1; k >= 0; k--)
        {
            Tile snakePart = snakeBody.get(k);
            if (k == 0)
            {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else
            {
                Tile prevSnakePart = snakeBody.get(k-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        //Snake Head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //Game over conditions
        for (int k = 0; k < snakeBody.size(); k++)
        {
            Tile snakePart = snakeBody.get(k);
            if (collision(snakeHead, snakePart))
            {
                gameOver = true;
            }
        }

        if (snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > boardWidth || 
            snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardHeight)
        {
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver)
        {
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W && velocityY != 1) 
        {
            velocityX = 0;
            velocityY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_S && velocityY != -1)
        {
            velocityX = 0;
            velocityY = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_A && velocityX != 1)
        {
            velocityX = -1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_D && velocityX != -1)
        {
            velocityX = 1;
            velocityY = 0;
        }
    }

    //Do not need
    @Override
    public void keyTyped(KeyEvent e) 
    {

    }

    @Override
    public void keyReleased(KeyEvent e) 
    {

    }
}
