package hr.fer.zemris.java.tecaj6;

public interface IObrada<T> {
	
	int brojStupaca();
	void obradiRedak(String[] elementi);
	T dohvatiRezultat();
	
}
