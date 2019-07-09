package io.github.pepsidog.miniadditions.utils;

import io.github.pepsidog.miniadditions.MiniAdditions;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;

public class Projectile extends BukkitRunnable {
    private ItemStack item;
    private Location position;
    private Location oldPosition;
    private Vector velocity;
    private Vector acceleration;
    private double speedLimit;
    private ArmorStand itemHolder;
    private double collisionResolution;
    private double dropDelta;
    private double dragDelta;
    private int lifespan;
    private int age;

    public Projectile(ItemStack item, Location position) {
        this.item = item;
        this.position = position;
        this.oldPosition = this.position.clone();
        this.velocity = new Vector(0, 0, 0);
        this.acceleration = new Vector(0, 0, 0);
        this.speedLimit = 3;
        this.collisionResolution = 0.1;
        this.dragDelta = 0.05;
        this.dropDelta = 0.05;
        this.lifespan = -1;
        this.age = 0;
    }

    public void applyForce(Vector force) {
        this.acceleration.add(force);
    }

    public void update() {
        this.velocity.add(this.acceleration);
        limit(this.velocity, this.speedLimit);
        this.velocity.multiply(1 - this.dragDelta);
        this.velocity.add(new Vector(0, -this.dropDelta, 0));

        this.oldPosition = this.position.clone();
        this.position.add(this.velocity);
        this.acceleration.multiply(0);

        draw();
        checkCollision();
    }

    public void draw() {
        this.itemHolder.teleport(this.position.clone().subtract(0, 1.7, 0));
    }

    public void checkCollision() {
        Vector step = this.position.clone().subtract(this.oldPosition).toVector();
        step = step.normalize().multiply(this.collisionResolution);

        Location currentCheck = this.oldPosition.clone();

        while(currentCheck.distance(this.position) >= 0) {
            try {
                Collection<Entity> nearby = currentCheck.getWorld().getNearbyEntities(currentCheck, 0.25, 0.25, 0.25);
                for (Entity entity : nearby) {
                    intersectedEntity(entity);
                }
            } catch (Exception e) {}

            Block block = currentCheck.getWorld().getBlockAt(currentCheck);
            if(!block.isPassable()) {
                intersectedBlock(block);
            }

            currentCheck.add(step);
        }
    }

    public void setCollisionResolution(double resolution) {
        this.collisionResolution = resolution;
    }

    public void setDropDelta(double dropDelta) {
        this.dropDelta = dropDelta;
    }

    public void setDragDelta(double dragDelta) {
        this.dragDelta = dragDelta;
    }

    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
    }

    public void intersectedEntity(Entity entity) {

    }

    public void intersectedBlock(Block block) {

    }

    public void diedOfOldAge() {

    }

    private void limit(Vector vector, double limit) {
        double length = vector.length();

        if(length > limit) {
            double scalar = limit / length;
            vector.multiply(scalar);
        }
    }

    public void launch() {
        this.itemHolder = (ArmorStand) position.getWorld().spawnEntity(this.position, EntityType.ARMOR_STAND);

        this.itemHolder.setHelmet(this.item);
        this.itemHolder.setVisible(false);
        this.itemHolder.setInvulnerable(true);
        this.runTaskTimerAsynchronously(MiniAdditions.getInstance(), 0, 5);
    }

    public void destroy() {
        this.cancel();
        this.itemHolder.remove();
    }

    @Override
    public void run() {
        if(this.lifespan > 0 && this.age > this.lifespan) {
            this.destroy();
            this.diedOfOldAge();
        }

        update();
        this.age += 5;
    }
}
