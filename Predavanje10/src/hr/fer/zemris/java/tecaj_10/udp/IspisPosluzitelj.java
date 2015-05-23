package hr.fer.zemris.java.tecaj_10.udp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class IspisPosluzitelj {

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Ocekivao sam port");
			return;
		}

		DatagramSocket socket = new DatagramSocket(Integer.parseInt(args[0]));
		while (true) {
			byte[] spremnik = new byte[1024];

			DatagramPacket packet = new DatagramPacket(spremnik,
					spremnik.length);
			socket.receive(packet);

			// simulacija gubljenja paketa
			if (Math.random() < 0.25) {
				continue;
			}
			
			if (packet.getLength() < 2) {
				continue;
			}

			byte[] duljina = Arrays.copyOfRange(packet.getData(),
					packet.getOffset(), packet.getOffset() + 2);
			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
					duljina));
			short duljinaImena = dis.readShort();
			System.out.println(duljinaImena);

			if (packet.getLength() < 2 + duljinaImena + 2 || duljinaImena < 1) {
				continue;
			}

			String ime = new String(packet.getData(), packet.getOffset() + 2,
					duljinaImena, StandardCharsets.UTF_8);

			duljina = Arrays.copyOfRange(packet.getData(), packet.getOffset()
					+ 2 + duljinaImena, packet.getOffset() + 2 + duljinaImena
					+ 2);
			dis = new DataInputStream(new ByteArrayInputStream(duljina));
			short duljinaTeksta = dis.readShort();

			if (packet.getLength() != 2 + duljinaImena + 2 + duljinaTeksta
					|| duljinaImena < 1) {
				continue;
			}

			String poruka = new String(packet.getData(), packet.getOffset() + 2
					+ duljinaImena + 2, duljinaTeksta, StandardCharsets.UTF_8);

			System.out.println("Dobio sam poruku s adrese "
					+ packet.getAddress() + " s porta " + packet.getPort()
					+ " od korisnika " + ime);
			
			System.out.println("Poruka je: " + poruka);
			System.out.println("========================================================");
			
			byte[] odgovor = "OK".getBytes(StandardCharsets.UTF_8);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			
			DataOutputStream dos = new DataOutputStream(bos);
			
			dos.writeShort(odgovor.length);
			dos.write(odgovor);
			dos.close();
			
			byte[] oktetiOdgovora = bos.toByteArray();
			
			DatagramPacket paketOdgovora = new DatagramPacket(oktetiOdgovora, oktetiOdgovora.length);
			paketOdgovora.setSocketAddress(packet.getSocketAddress());
			socket.send(paketOdgovora);
			
		}
	}

}
