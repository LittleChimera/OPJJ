package hr.web.adresar;

import java.io.IOException;

public class Demo {

	public static void main(String[] args) throws IOException {

		
		String staza = "C:/podatci.txt";
		Adresar adr = Adresar.procitajIzDatoteke(staza);
		Record novi = new Record(null, "Ivić", "Ivo", "Ivo.Ivic@acme.org");
		adr.zapamti(novi);

		Long dobiveniId = novi.getId();
		System.out.println("Dobiven je identifikator: " + dobiveniId);

		Record r = adr.dohvati(dobiveniId);
		System.out.println("e-mail: " + r.getEmail());

		adr.zapamti(new Record(null, "Anić", "Ante", "Ante.Anic@acme.org"));
		adr.zapamti(new Record(null, "Jasić", "Jasmina", "Jasmina.Jasic@acme.org"));
		adr.zapamti(new Record(null, "Krivić", "Karla", "Karla.Krivic@acme.org"));
		
		adr.spremiUDatoteku(staza);

	}
}
