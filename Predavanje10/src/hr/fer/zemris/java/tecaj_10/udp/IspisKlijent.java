package hr.fer.zemris.java.tecaj_10.udp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class IspisKlijent {

	public static void main(String[] args) throws IOException{
		if (args.length != 4) {
			System.out.println("Expected: ip/host port username message");
			return;
		}
		
		SocketAddress odrediste = new InetSocketAddress(
				InetAddress.getByName(args[0]), 
				Integer.parseInt(args[1])
				);
		
		byte[] ime = args[2].getBytes(StandardCharsets.UTF_8);
		byte[] poruka = args[3].getBytes(StandardCharsets.UTF_8);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
		dos.writeShort(ime.length);
		dos.write(ime);
		dos.writeShort(poruka.length);
		dos.write(poruka);
		dos.close();
		
		byte[] spremnik = bos.toByteArray();
		
		final int TIMEOUT = 4000;
		
		DatagramSocket socket = new DatagramSocket();
		socket.setSoTimeout(TIMEOUT);
		
		while (true) {
			DatagramPacket packet = new DatagramPacket(spremnik, spremnik.length);
			packet.setSocketAddress(odrediste);
			
			socket.send(packet);
			
			byte[] spremnikOdgovora = new byte[100];
			DatagramPacket paketOdgovora = new DatagramPacket(spremnikOdgovora, spremnikOdgovora.length);
			
			try {
				socket.receive(paketOdgovora);				
			} catch (SocketTimeoutException ste) {
				System.out.println("Radim retransmisiju");
				continue;
			}
			
			byte[] duljina = Arrays.copyOfRange(paketOdgovora.getData(),
					paketOdgovora.getOffset(), paketOdgovora.getOffset() + 2);
			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
					duljina));
			short duljinaStatusa = dis.readShort();

			if (paketOdgovora.getLength() != 2 + duljinaStatusa || duljinaStatusa < 1) {
				continue;
			}

			String status = new String(paketOdgovora.getData(), paketOdgovora.getOffset() + 2,
					duljinaStatusa, StandardCharsets.UTF_8);
			
			System.out.println("Poluszitelj je javio: " + status);
			break;
		}
		
		socket.close();
	}
}
