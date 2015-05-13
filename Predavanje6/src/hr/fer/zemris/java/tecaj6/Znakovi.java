package hr.fer.zemris.java.tecaj6;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Znakovi {
	
	public static void main(String[] args) throws IOException {
		try(OutputStream oStream = new BufferedOutputStream(new FileOutputStream("unicodeExacmple1.txt"))) {
			String poruka = "Šeće čevapčić u čevapđnicu";
			byte okteti[] = poruka.getBytes(StandardCharsets.UTF_8);
			oStream.write(okteti);
		} catch (Exception e) {
		}
	}

}
