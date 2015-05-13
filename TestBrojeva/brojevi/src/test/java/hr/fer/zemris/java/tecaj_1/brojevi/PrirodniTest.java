package hr.fer.zemris.java.tecaj_1.brojevi;

import org.junit.Test;
import org.junit.Assert;

public class PrirodniTest {

    @Test
	public void negativanBrojNijePrirodan() {
		Assert.assertFalse(Prirodni.jePrirodni(-1));
	}

    @Test
	public void pozitivniBrojJePrirodan() {
		Assert.assertTrue(Prirodni.jePrirodni(4));
	}

    @Test
	public void nulaNijePrirodniBroj() {
		Assert.assertFalse(Prirodni.jePrirodni(0));
	}
}
