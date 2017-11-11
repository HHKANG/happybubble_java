import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;

public class SocketTest {
	public static final int port = 8765;
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("수신 대기 중");
			socket = serverSocket.accept();       

			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			byte[] sizeArr = new byte[4];
			in.read(sizeArr);
			int imgSize = getInt(sizeArr);
			System.out.println("이미지 사이즈 : " + imgSize);
			byte[] arrImg = new byte[imgSize];
			in.read(arrImg);
			InputStream convertImgStream = new ByteArrayInputStream(arrImg);
			BufferedImage bImg = ImageIO.read(convertImgStream);
			File outputfile = new File("C:\\Users\\Administrator\\Desktop\\test.png");
			ImageIO.write(bImg, "png", outputfile);
			System.out.println("이미지 수신 완료");
			byte[] arrPaperWidth = new byte[4];
			byte[] arrPaperHeight = new byte[4];
			in.read(arrPaperWidth);
			in.read(arrPaperHeight);
			int width = getInt(arrPaperWidth);
			int height = getInt(arrPaperHeight);
			System.out.println("Image Info");
			System.out.println("Width : " + width);
			System.out.println("Height : " + height);
		} catch(Exception e) {
			e.printStackTrace();
		}
	    finally {
	    	try {
	    		socket.close();
    		} catch(Exception ignored) { }
    		try {
    			serverSocket.close();
			} catch(Exception ignored) { }
	    }
	}
	public static int getInt(byte[] data) {
	    int s1 = data[0] & 0xFF;
	    int s2 = data[1] & 0xFF;
	    int s3 = data[2] & 0xFF;
	    int s4 = data[3] & 0xFF;

	    return ((s1 << 24) + (s2 << 16) + (s3 << 8) + (s4 << 0));
	}
}
