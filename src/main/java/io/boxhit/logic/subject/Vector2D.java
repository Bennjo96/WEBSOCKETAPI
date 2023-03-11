package io.boxhit.logic.subject;

public class Vector2D {

    private double x;
    private double y;
    public boolean hard = false;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D vector2D) {
        this.x = vector2D.getX();
        this.y = vector2D.getY();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void addX(double x) {
        this.x += x;
    }

    public void addY(double y) {
        this.y += y;
    }

    public Vector2D add(Vector2D vector2D) {
        this.x += vector2D.getX();
        this.y += vector2D.getY();
        return this;
    }

    public Vector2D sub(Vector2D vector2D) {
        this.x -= vector2D.getX();
        this.y -= vector2D.getY();
        return this;
    }

    public Vector2D mul(double value) {
        this.x *= value;
        this.y *= value;
        return this;
    }

    public Vector2D div(double value) {
        this.x /= value;
        this.y /= value;
        return this;
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public double lengthSquared() {
        return x * x + y * y;
    }

    public double distance(Vector2D vector2D) {
        double dx = this.x - vector2D.getX();
        double dy = this.y - vector2D.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double distanceSquared(Vector2D vector2D) {
        double dx = this.x - vector2D.getX();
        double dy = this.y - vector2D.getY();
        return dx * dx + dy * dy;
    }

    public double dot(Vector2D vector2D) {
        return this.x * vector2D.getX() + this.y * vector2D.getY();
    }

    public double cross(Vector2D vector2D) {
        return this.x * vector2D.getY() - this.y * vector2D.getX();
    }

    public double angle(Vector2D vector2D) {
        double dot = this.dot(vector2D);
        double det = this.cross(vector2D);
        return Math.atan2(det, dot);
    }

    public Vector2D normalize() {
        double length = this.length();
        this.x /= length;
        this.y /= length;
        return this;
    }

    public Vector2D getNormalized() {
        double length = this.length();
        return new Vector2D(this.x / length, this.y / length);
    }

    public void rotate(double angle) {
        double rad = Math.toRadians(angle);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        double x = this.x * cos - this.y * sin;
        double y = this.x * sin + this.y * cos;
        this.x = x;
        this.y = y;
    }

    public Vector2D getRotated(double angle) {
        double rad = Math.toRadians(angle);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        double x = this.x * cos - this.y * sin;
        double y = this.x * sin + this.y * cos;
        return new Vector2D(x, y);
    }

    public void rotateAroundPoint(Vector2D point, double angle) {
        this.sub(point);
        this.rotate(angle);
        this.add(point);
    }

    public Vector2D getRotatedAroundPoint(Vector2D point, double angle) {
        Vector2D vector2D = new Vector2D(this);
        vector2D.sub(point);
        vector2D.rotate(angle);
        vector2D.add(point);
        return vector2D;
    }

    public void rotateAroundOrigin(double angle) {
        double rad = Math.toRadians(angle);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        double x = this.x * cos - this.y * sin;
        double y = this.x * sin + this.y * cos;
        this.x = x;
        this.y = y;
    }

    public Vector2D getRotatedAroundOrigin(double angle) {
        double rad = Math.toRadians(angle);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        double x = this.x * cos - this.y * sin;
        double y = this.x * sin + this.y * cos;
        return new Vector2D(x, y);
    }

    public void reflect(Vector2D normal) {
        double dot = this.dot(normal);
        this.x = 2 * dot * normal.getX() - this.x;
        this.y = 2 * dot * normal.getY() - this.y;
    }

    public Vector2D getReflected(Vector2D normal) {
        double dot = this.dot(normal);
        double x = 2 * dot * normal.getX() - this.x;
        double y = 2 * dot * normal.getY() - this.y;
        return new Vector2D(x, y);
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vector2D vector2D) {
        this.x = vector2D.getX();
        this.y = vector2D.getY();
    }

    public void zero() {
        this.x = 0;
        this.y = 0;
    }

    public boolean isZero() {
        return this.x == 0 && this.y == 0;
    }

    public boolean isUnit() {
        return this.lengthSquared() == 1;
    }

    public boolean isParallel(Vector2D vector2D) {
        return this.cross(vector2D) == 0;
    }

    public boolean isPerpendicular(Vector2D vector2D) {
        return this.dot(vector2D) == 0;
    }

    public boolean equals(Vector2D vector2D) {
        return this.x == vector2D.getX() && this.y == vector2D.getY();
    }

    public boolean equals(Vector2D vector2D, double epsilon) {
        return Math.abs(this.x - vector2D.getX()) < epsilon && Math.abs(this.y - vector2D.getY()) < epsilon;
    }

    public Vector2D clone() {
        return new Vector2D(this);
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    public Vector2D subtract(Vector2D vector2D) {
        return new Vector2D(this.x - vector2D.getX(), this.y - vector2D.getY());
    }

    public Vector2D setHard(boolean hard){
        this.hard = hard;
        return this;
    }



}
