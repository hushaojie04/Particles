package sj.android.particles;

import android.util.FloatMath;

/**
 * Created by Administrator on 2015/8/11.
 */
public class Geometry {
    public static class Point {
        public final float x, y, z;

        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Point translateY(float distance) {
            return new Point(x, y + distance, z);
        }

        public Point translate(Vector vector) {
            return new Point(x + vector.x, y + vector.y, z + vector.z);
        }

    }

    public static class Circle {
        public final Point center;
        public final float radius;

        public Circle(Point center, float radius) {
            this.center = center;
            this.radius = radius;
        }

        public Circle scale(float scale) {
            return new Circle(center, radius * scale);
        }
    }

    public static class Cylinder {
        public final Point center;
        public final float radius;
        public final float height;

        public Cylinder(Point center, float radius, float height) {
            this.center = center;
            this.radius = radius;
            this.height = height;
        }
    }

    public static class Vector {
        public final float x, y, z;

        public Vector(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public float length() {
            return FloatMath.sqrt(x * x + y * y + z * z);
        }

        public Vector crossProduct(Vector other) {
            return new Vector(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x);
        }

        public float dotProduct(Vector other) {
            return x * other.x + y * other.y + z * other.z;
        }

        public Vector scale(float f) {
            return new Vector(x * f, y * f, z * f);
        }

        public Vector normalize() {
            return scale(1f / length());
        }
    }

    public static class Ray {
        public final Point point;
        public final Vector vector;

        public Ray(Point point, Vector vector) {
            this.point = point;
            this.vector = vector;
        }
    }

    public static class Sphere {
        public final Point cener;
        public final float radius;

        public Sphere(Point center, float radius) {
            this.cener = center;
            this.radius = radius;
        }
    }

    public static class Plane {
        public final Point point;
        public final Vector normal;

        public Plane(Point point, Vector normal) {
            this.point = point;
            this.normal = normal;
        }
    }

    public static Point intersectionPoint(Ray ray, Plane plane) {
        Vector rayToPlaneVector = vectorBetWeen(ray.point, plane.point);
        float scaleFactor = rayToPlaneVector.dotProduct(plane.normal) / ray.vector.dotProduct(plane.normal);
        Point intersectionPoint = ray.point.translate(ray.vector.scale(scaleFactor));
        return intersectionPoint;
    }

    public static Vector vectorBetWeen(Point from, Point to) {
        return new Vector(to.x - from.x, to.y - from.y, to.z - from.z);
    }

    public static boolean intersects(Sphere sphere, Ray ray) {
        return distanceBetWeen(sphere.cener, ray) < sphere.radius;
    }

    public static float distanceBetWeen(Point point, Ray ray) {
        Vector p1ToPoint = vectorBetWeen(ray.point, point);
        Vector p2ToPoint = vectorBetWeen(ray.point.translate(ray.vector), point);
        float areaOfTriangleTimesTow = p1ToPoint.crossProduct(p2ToPoint).length();
        float lengthOfBase = ray.vector.length();
        float distanceFromPointToRay = areaOfTriangleTimesTow / lengthOfBase;
        return distanceFromPointToRay;
    }


}
