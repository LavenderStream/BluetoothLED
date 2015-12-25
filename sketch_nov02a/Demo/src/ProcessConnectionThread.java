
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.NetworkInterface;

import javax.microedition.io.StreamConnection;

public class ProcessConnectionThread implements Runnable
{

	private StreamConnection mConnection;

	// Constant that indicate command from devices
	private static final int EXIT_CMD = -1;
	private static final int KEY_VOLUE_PLUS = 2;
	private static final int KEY_VOLUE_CUT = 3;
	private static final int KEY_SEEK_PLUS = 4;
	private static final int KEY_SEEK_CUT = 5;
	private static final int KEY_PLAY = 1;
	private static final int KEY_STOP = 6;
	int readBufferPosition = 0;
	byte[] readBuffer = new byte[1024];

	public ProcessConnectionThread(StreamConnection connection)
	{
		mConnection = connection;
	}

	@Override
	public void run()
	{
		// 1KB的缓存
		byte[] buffer = new byte[512];
		int bytes;
		File file = new File("sk.txt");
		
		try
		{
			// 得到输入流
			InputStream inputStream = mConnection.openInputStream();
		
		
			while (true)
			{
				FileWriter fileWritter = new FileWriter(file.getName(),true);
	            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				// 循环读取数据
				if ((bytes = inputStream.read(buffer)) > 0)
				{
					byte[] buf_data = new byte[bytes];
					for (int i = 0; i < bytes; i++)
					{
						buf_data[i] = buffer[i];
					}
					
					String s = new String(buf_data);
		
					//System.out.print(s);
					// 按\n截取
					String[] str = s.split("\n");
					// 丢掉不成组或者只有一组的数据
					if (str.length > 1)
					{
						// 去掉收尾两组数据
						for (int i = 1; i < str.length - 1; i++)
						{
							// str[i] 为一整组数据，注意每组数没有空格隔开，就是完成的一组数
							System.out.println(str[i] + " # ");
							bufferWritter.write(str[i] + "#\n");
						}
					}

				}
				bufferWritter.close();
			}
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		

	}

	/**
	 * Process the command from client
	 * 
	 * @param command
	 *            the command code
	 */
	private void processCommand(int command)
	{
		try
		{
			Robot robot = new Robot();
			switch (command)
			{
			// QQ 音乐的暂停动作
			case KEY_PLAY:
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_F12);
				robot.keyRelease(KeyEvent.VK_CONTROL);
				robot.keyRelease(KeyEvent.VK_F12);
				break;
			case KEY_SEEK_PLUS:
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_NUMPAD8);
				robot.keyRelease(KeyEvent.VK_CONTROL);
				robot.keyRelease(KeyEvent.VK_NUMPAD8);
				break;
			case KEY_SEEK_CUT:
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_F11);
				robot.keyRelease(KeyEvent.VK_CONTROL);
				robot.keyRelease(KeyEvent.VK_F11);
				break;
			case KEY_VOLUE_PLUS:
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_NUMPAD2);
				robot.keyRelease(KeyEvent.VK_CONTROL);
				robot.keyRelease(KeyEvent.VK_NUMPAD2);
				break;
			case KEY_VOLUE_CUT:
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_NUMPAD3);
				robot.keyRelease(KeyEvent.VK_CONTROL);
				robot.keyRelease(KeyEvent.VK_NUMPAD3);
				break;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}