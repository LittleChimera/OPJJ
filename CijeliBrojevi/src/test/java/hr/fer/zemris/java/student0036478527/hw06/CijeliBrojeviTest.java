package hr.fer.zemris.java.student0036478527.hw06;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit test for simple CijeliBrojevi.
 */
public class CijeliBrojeviTest {

    @Test
    public void testirajNaNeparnostNeparanBroj() {
        assertTrue((new CijeliBrojevi()).jeNeparan(27));
    }
    @Test
    public void testirajNaNeparnostParanBroj() {
        assertFalse((new CijeliBrojevi()).jeNeparan(28));
    }
    @Test
    public void testirajNaParnostParanBroj() {
        assertTrue((new CijeliBrojevi()).jeParan(28));
    }
    @Test
    public void testirajNaParnostNeparanBroj() {
        assertFalse((new CijeliBrojevi()).jeParan(27));
    }
    @Test
    public void testirajJeliProstProstBroj() {
        assertTrue((new CijeliBrojevi()).jeProst(83));
    }
    @Test
    public void testirajJeliProstNeprostBroj() {
        assertFalse((new CijeliBrojevi()).jeProst(111));
    }
    @Test
    public void testirajJeliProstParanBroj() {
        assertFalse((new CijeliBrojevi()).jeProst(112));
    }
}
