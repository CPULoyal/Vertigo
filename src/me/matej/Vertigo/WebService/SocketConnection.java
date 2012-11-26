package me.matej.Vertigo.WebService;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * @author matejkramny
 */
public class SocketConnection {

	public static final String newline = "\r\n";
	public static final String REQUEST_GET = "GET";
	public static final String REQUEST_POST = "POST";
	public static final String REQUEST_PUT = "PUT";
	public static final String REQUEST_DELETE = "DELETE";
	public static final String REQUEST_ = "";

	private String host;
	private int port;
	private boolean verbose = false;

	private Socket socket;
	private BufferedWriter writer;
	private BufferedReader reader;

	private String status;
	private HashMap<String, String> headers;
	private StringBuilder data;

	public SocketConnection (String host, int port) {
		super();

		this.host = host;
		this.port = port;
	}
	public SocketConnection (String host, int port, boolean verbose) {
		this(host, port);

		this.verbose = verbose;
	}

	public void writeHeader (String name, String value) {
		write(name + ": " + value);
	}
	public void write () {
		this.write("");
	}
	public void write (String data) {
		this.write(data, true);
	}
	public void write (String data, boolean newline) {
		if (newline)
			data = data + this.newline;

		try {
			if (verbose)
				System.out.print("-+ Writing "+data);

			writer.write(data);
			writer.flush();
		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}
	}

	public void read () {
		try {
			if (verbose)
				System.out.println("-- Reading "+host+":"+port);

			String line;
			boolean sendingHeaders = true;

			status = null;
			headers = new HashMap<String, String>();
			data = new StringBuilder();

			while((line = reader.readLine()) != null) {
				if (verbose)
					System.out.println("+- Reading "+line);

				if (sendingHeaders) {
					if ("".equals(line)) {
						sendingHeaders = false;
					} else {
						if (status == null) {
							status = line;
							continue;
						}

						String[] kvpair = line.split(":", 2);

						headers.put(kvpair[0], kvpair[1]);
					}
				} else {
					data.append(line+"\n");
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}
	}

	public String getStatus () {
		return status;
	}
	public HashMap<String, String> getHeaders () {
		return headers;
	}
	public StringBuilder getData () {
		return data;
	}

	public void close () {
		if (verbose)
			System.out.println("-- Closing "+host+":"+port);
		try {
			socket.close();
			writer.close();
			reader.close();
		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}
	}

	public boolean connect () {
		if (verbose)
			System.out.println("-- Connecting "+host+":"+port);
		try {
			socket = new Socket(host, port);

			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF8"));

			return true;
		} catch (UnknownHostException ex) {
			ex.printStackTrace(System.err);
		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}

		return false;
	}

	public static String getHTTPHead (String url) {
		return getHTTPHead(SocketConnection.REQUEST_GET, url);
	}
	public static String getHTTPHead (String method, String url) {
		return method.toUpperCase() + " " + url.replaceAll(" ", "%20") + " HTTP/1.0";
	}
	public static void writeHTTPHead (SocketConnection socket) {
		writeHTTPHead("/", socket);
	}
	public static void writeHTTPHead (String url, SocketConnection socket) {
		writeHTTPHead(SocketConnection.REQUEST_GET, url, socket);
	}
	public static void writeHTTPHead (String method, String url, SocketConnection socket) {
		socket.write(getHTTPHead(method, url));
	}
}
