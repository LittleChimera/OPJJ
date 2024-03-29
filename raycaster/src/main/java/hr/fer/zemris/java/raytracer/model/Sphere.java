package hr.fer.zemris.java.raytracer.model;

/**
 * Implementation of {@link GraphicalObject} for a sphere.
 * 
 * @author Luka Skugor
 *
 */
public class Sphere extends GraphicalObject {

	/**
	 * Center of the sphere.
	 */
	private Point3D center;
	/**
	 * Radius of the sphere.
	 */
	private double radius;
	/**
	 * Red color diffuse coefficient.
	 */
	private double kdr;
	/**
	 * Green color diffuse coefficient.
	 */
	private double kdg;
	/**
	 * Blue color diffuse coefficient.
	 */
	private double kdb;
	/**
	 * Red color reflect coefficient.
	 */
	private double krr;
	/**
	 * Green color reflect coefficient.
	 */
	private double krg;
	/**
	 * Blue color reflect coefficient.
	 */
	private double krb;
	/**
	 * Reflection coefficient n.
	 */
	private double krn;

	/**
	 * Creates a new sphere for given center point, radius and given material color components.
	 * @param center center of the sphere
	 * @param radius radius of the sphere
	 * @param kdr red color diffuse coefficient
	 * @param kdg green color diffuse coefficient
	 * @param kdb blue color diffuse coefficient
	 * @param krr red color reflective coefficient
	 * @param krg green color reflective coefficient
	 * @param krb blue color reflective coefficient
	 * @param krn reflection coefficient n
	 */
	public Sphere(Point3D center, double radius, double kdr, double kdg,
			double kdb, double krr, double krg, double krb, double krn) {
		super();
		this.center = center;
		if (radius < 0) {
			throw new IllegalArgumentException("Radius needs to be positive.");
		}
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hr.fer.zemris.java.raytracer.model.GraphicalObject#findClosestRayIntersection
	 * (hr.fer.zemris.java.raytracer.model.Ray)
	 */
	@Override
	public RayIntersection findClosestRayIntersection(Ray arg0) {
		/**
		 * Implementation of {@link RayIntersection} where intersected object is
		 * a Sphere.
		 * 
		 * @author Luka Skugor
		 *
		 */
		class SphereRayIntersection extends RayIntersection {

			/**
			 * Creates a new {@link RayIntersection} of this Sphere with a ray.
			 * 
			 * @param point
			 *            point of intersection between ray and the sphere
			 * @param distance
			 *            distance between start of ray and intersection
			 * @param outer
			 *            specifies if intersection is outer
			 */
			public SphereRayIntersection(Point3D point, double distance,
					boolean outer) {
				super(point, distance, outer);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see hr.fer.zemris.java.raytracer.model.RayIntersection#getKdr()
			 */
			@Override
			public double getKdr() {
				return kdr;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see hr.fer.zemris.java.raytracer.model.RayIntersection#getKdg()
			 */
			@Override
			public double getKdg() {
				return kdg;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see hr.fer.zemris.java.raytracer.model.RayIntersection#getKdb()
			 */
			@Override
			public double getKdb() {
				return kdb;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see hr.fer.zemris.java.raytracer.model.RayIntersection#getKrr()
			 */
			@Override
			public double getKrr() {
				return krr;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see hr.fer.zemris.java.raytracer.model.RayIntersection#getKrg()
			 */
			@Override
			public double getKrg() {
				return krg;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see hr.fer.zemris.java.raytracer.model.RayIntersection#getKrb()
			 */
			@Override
			public double getKrb() {
				return krb;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see hr.fer.zemris.java.raytracer.model.RayIntersection#getKrn()
			 */
			@Override
			public double getKrn() {
				return krn;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * hr.fer.zemris.java.raytracer.model.RayIntersection#getNormal()
			 */
			@Override
			public Point3D getNormal() {
				return getPoint().sub(Sphere.this.center).normalize();
			}

		}

		// using equation from
		// http://en.wikipedia.org/wiki/Line%E2%80%93sphere_intersection

		// o - c (origin of the line - center of the sphere)
		Point3D oMinC = arg0.start.sub(center);

		// first argument of the equation
		double eqArg1 = -arg0.direction.scalarProduct(oMinC);

		// root arguments
		double rootArg1sqrt = arg0.direction.scalarProduct(oMinC);
		double rootArg1 = rootArg1sqrt * rootArg1sqrt;
		double rootArg2sqrt = oMinC.norm();
		double rootArg2 = -rootArg2sqrt * rootArg2sqrt;
		double rootArg3 = radius * radius;

		double underRoot = rootArg1 + rootArg2 + rootArg3;
		if (underRoot < 0) {
			return null;
		}

		// second argument of the equation
		double eqArg2;

		// if under root is close to zero skip calculation of root and set
		// second equation argument to zero
		if (underRoot < 1e-9) {
			eqArg2 = 0;
		} else {
			eqArg2 = Math.sqrt(underRoot);
		}

		// solutions
		double sol1 = eqArg1 - eqArg2;
		double sol2 = eqArg1 + eqArg2;

		if (sol1 >= 0) {
			return new SphereRayIntersection(arg0.start.add(arg0.direction
					.scalarMultiply(sol1)), sol1, true);
		} else if (sol2 >= 0) {
			return new SphereRayIntersection(arg0.start.add(arg0.direction
					.scalarMultiply(sol2)), sol2, false);
		} else {
			return null;
		}
	}
}
