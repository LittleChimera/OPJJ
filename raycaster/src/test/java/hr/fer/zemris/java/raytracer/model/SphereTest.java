package hr.fer.zemris.java.raytracer.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class SphereTest {

	private static final double THETA = 1e-9;

	@Test
	public void interesectionTest() {
		Ray ray = new Ray(new Point3D(10, 0, 0), new Point3D(-1,0,0));
		Sphere sphere = new Sphere(new Point3D(), 1.4, 0, 0, 0, 0, 0, 0, 0);
		RayIntersection intersection = sphere.findClosestRayIntersection(ray);
		assertEquals(1.4, intersection.getPoint().x, THETA);
		assertEquals(0, intersection.getPoint().y, THETA);
		assertEquals(0, intersection.getPoint().z, THETA);
		assertTrue(intersection.isOuter());
	}
	
	@Test
	public void interesectionTestInner() {
		Ray ray = new Ray(new Point3D(0, 0, 0), new Point3D(1,0,0));
		Sphere sphere = new Sphere(new Point3D(), 1.4, 0, 0, 0, 0, 0, 0, 0);
		RayIntersection intersection = sphere.findClosestRayIntersection(ray);
		assertEquals(1.4, intersection.getPoint().x, THETA);
		assertEquals(0, intersection.getPoint().y, THETA);
		assertEquals(0, intersection.getPoint().z, THETA);
		assertFalse(intersection.isOuter());
	}
	
	@Test
	public void noIntersectionTest() {
		Ray ray = new Ray(new Point3D(10, 0, 0), new Point3D(0,1,0));
		Sphere sphere = new Sphere(new Point3D(), 1.4, 0, 0, 0, 0, 0, 0, 0);
		RayIntersection intersection = sphere.findClosestRayIntersection(ray);
		assertEquals(null, intersection);
	}
	
	@Test
	public void intersectionTestunderRootNegative() {
		Ray ray = new Ray(new Point3D(1000, 0, 0), new Point3D(1,0,0));
		Sphere sphere = new Sphere(new Point3D(), 1.4, 0, 0, 0, 0, 0, 0, 0);
		RayIntersection intersection = sphere.findClosestRayIntersection(ray);
		assertEquals(null, intersection);
	}
	
	@Test
	public void rayTouchingSphereIntersectionTest() {
		Ray ray = new Ray(new Point3D(10, 1.4, 0), new Point3D(-1,0,0));
		Sphere sphere = new Sphere(new Point3D(), 1.4, 0, 0, 0, 0, 0, 0, 0);
		RayIntersection intersection = sphere.findClosestRayIntersection(ray);
		assertEquals(0, intersection.getPoint().x, THETA);
		assertEquals(1.4, intersection.getPoint().y, THETA);
		assertEquals(0, intersection.getPoint().z, THETA);
		assertTrue(intersection.isOuter());
	}

}
