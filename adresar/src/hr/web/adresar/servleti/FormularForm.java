package hr.web.adresar.servleti;

import hr.web.adresar.Record;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class FormularForm {

	private String id;
	private String prezime;
	private String ime;
	private String email;

	Map<String, String> greske = new HashMap<>();
	
	public FormularForm() {
	}

	public String dohvatiPogresku(String ime) {
		return greske.get(ime);
	}
	
	public boolean imaPogresaka() {
		return !greske.isEmpty();
	}
	
	public boolean imaPogresku(String ime) {
		return greske.containsKey(ime);
	}
	
	public void popuniIzHttpRequesta(HttpServletRequest req) {
		this.id = pripremi(req.getParameter("id"));
		this.ime = pripremi(req.getParameter("ime"));
		this.prezime = pripremi(req.getParameter("prezime"));
		this.email = pripremi(req.getParameter("email"));
	}

	public void popuniIzRecorda(Record r) {
		if(r.getId()==null) {
			this.id = "";
		} else {
			this.id = r.getId().toString();
		}
		
		this.ime = r.getIme();
		this.prezime = r.getPrezime();
		this.email = r.getEmail();
	}

	public void popuniURecord(Record r) {
		if(this.id.isEmpty()) {
			r.setId(null);
		} else {
			r.setId(Long.valueOf(this.id));
		}
		
		r.setIme(this.ime);
		r.setPrezime(this.prezime);
		r.setEmail(this.email);
	}
	
	public void validiraj() {
		greske.clear();
		
		if(!this.id.isEmpty()) {
			try {
				Long.parseLong(this.id);
			} catch(NumberFormatException ex) {
				greske.put("id", "Vrijednost identifikatora nije valjana.");
			}
		}
		
		if(this.ime.isEmpty()) {
			greske.put("ime", "Ime je obavezno!");
		}
		
		if(this.prezime.isEmpty()) {
			greske.put("prezime", "Prezime je obavezno!");
		}

		if(this.email.isEmpty()) {
			greske.put("email", "EMail je obavezan!");
		} else {
			int l = email.length();
			int p = email.indexOf('@');
			if(l<3 || p==-1 || p==0 || p==l-1) {
				greske.put("email", "EMail nije ispravnog formata.");
			}
		}
	}
	
	private String pripremi(String s) {
		if(s==null) return "";
		return s.trim();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
