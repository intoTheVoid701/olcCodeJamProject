package dev.intoTheVoid.game.entities.projectiles;

import dev.intoTheVoid.game.Game;
import dev.intoTheVoid.game.entities.Entity;
import dev.intoTheVoid.game.sfx.SoundPlayer;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Projectile extends Entity
{
    private int direction; // is it moving up or down?
    private int speed; // speed
    private BufferedImage sprite;
    protected float yMove;

    public Projectile(BufferedImage sprite, float x, float y, float width, float height, Game game, int speed, int direction)
    {
        super(x, y, width, height, game);
        this.speed = speed;
        this.direction = direction;
        this.sprite = sprite;
        yMove = 0;
        // make sure that direction is not:
        // - greater than 1
        // - equal to 0
        // - less than -1
        // any one of these will result in headaches!
        if (direction == 0)
            direction = 1;
        if (direction > 1)
            direction = 1;
        if (direction < -1)
            direction = -1;

        bounds = new Rectangle(0, 4, 11, 33);
    }

    @Override
    public void update()
    {
        if (!active)
            return;
        yMove = speed * direction;

        if (!checkEntityCollisions(0, yMove))
        {
            y += yMove;
        }
        else
        {
            if ((checkEntityTitle(0, yMove).equalsIgnoreCase("player") && (this.title.equals("eProj")))) // did you just unironically touch an enemy laser?
            {
                game.getPlayer().die(); // kinda cringe bro
                die();
            }
            else if (checkEntityTitle(0, yMove).equalsIgnoreCase("enemy") && this.title.equals("fProj"))
            {
                getEntityAt(0, yMove).die();
                die();
            }
            else if (checkEntityTitle(0, yMove).equalsIgnoreCase("fProj") && this.title.equals("mProj"))
            {
                getEntityAt(0, yMove).die();
                SoundPlayer.playSound("res/sounds/die.wav");
            }
            else if (checkEntityTitle(0, yMove).equalsIgnoreCase("player") && this.title.equals("mProj"))
            {
                game.getPlayer().die();
            }
            else
                y += yMove;
        }
    }

    @Override
    public void die()
    {
        active = false;
        x = 3879;
        y = 9232;
    }

    @Override
    public void render(Graphics g)
    {
        if (!active)
            return;
        g.drawImage(sprite, (int)x, (int)y, (int)width, (int)height, null);
    }
}
