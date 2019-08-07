package bk.hungd.App;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class AppFunction {

	public static ArrayList<String> readFile(File file) {
		try {
			InputStream is = new FileInputStream(file);

			InputStreamReader ir = new InputStreamReader(is, Charset.forName("UTF-8"));
			BufferedReader br = new BufferedReader(ir);

			ArrayList<String> lines = new ArrayList<>();
			String line = "";
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
			br.close();
			return lines;
		} catch (FileNotFoundException e) {
			AppDebug.error(e, "");
			return null;
		} catch (IOException e) {
			AppDebug.error(e, "");
			return null;
		}
	}

	public static void writeFileFilter(File file, String value, String filter) {
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			ArrayList<String> lines = readFile(file);
			if (lines != null) {
				boolean exist = false;
				for (int i = 0; i < lines.size(); i++) {
					if (lines.get(i).startsWith(filter)) {
						exist = true;
						lines.set(i, filter + value);
					}
				}
				if (!exist) {
					lines.add(filter + value);
				}

				FileWriter fw = new FileWriter(file, false);
				String contents = "";
				for (String line : lines) {
					contents += line + "\n";
				}
				fw.write(contents);
				fw.close();
			}
		} catch (IOException e) {
			System.out.println(" > ERROR: Fail to write to log file! " + e.getMessage());
			AppDebug.error(e, "");
		}
	}

	public static int[] filteredRandom(int count, int max) {
		int[] rands = new int[count];
		Random rand = new Random();

		ArrayList<Integer> arr = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			boolean valid;
			do {
				int r = rand.nextInt(max);
				valid = true;
				for (int j = 0; j < arr.size(); j++) {
					if (r == arr.get(j)) {
						valid = false;
						break;
					}
				}
				if (valid) {
					arr.add(r);
					rands[i] = r;
				}
			} while (valid == false);
		}
		String s = "";
		for (int i = 0; i < count; i++)
			s += rands[i] + " ";
		// System.out.println(" > Random "+count +" from "+max+": "+s);
		return rands;
	}

	public static byte[] getBytesFromImage(Image image) {
		byte[] bytes = null;
		try {
			BufferedImage buffImage = SwingFXUtils.fromFXImage(image, null);
			ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
			ImageIO.write(buffImage, "png", byteOutput);
			bytes = byteOutput.toByteArray();
			byteOutput.close();
		} catch (IOException e) {
			AppDebug.error(e, "");
		} catch (Exception e) {
			AppDebug.error(e, "");
		}
		return bytes;
	}

	public static byte[] getBytesFromImageFile(File file) {
		byte[] bytes = null;
		try {
			Image image = new Image(file.toURI().toString());
			BufferedImage buffImage = SwingFXUtils.fromFXImage(image, null);
			ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
			ImageIO.write(buffImage, "png", byteOutput);
			bytes = byteOutput.toByteArray();
			byteOutput.close();
		} catch (IOException e) {
			AppDebug.error(e, "Can not get bytes of image file!");
		} catch (Exception e) {
			AppDebug.error(e, "Can not get bytes of image file!");
		}
		return bytes;
	}

	public static Image getImageFromFile(File file) {
		Image image = null;
		try {
			image = new Image(file.toURI().toString());
		} catch (Exception e) {
			AppDebug.error(e, "Can not get image from file!");
		}
		return image;
	}

	public static String getRelativePath(String fullPath, String workspaceDir) {
		try {
			return fullPath.substring(fullPath.indexOf(workspaceDir) + workspaceDir.length());
		} catch (Exception e) {
			return fullPath;
		}
	}

	public static FileChooser fileImageChooser(String title, String defaultDir){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(title);
		fileChooser.setInitialDirectory(new File(defaultDir));
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("All Images", "*.*"),
				new FileChooser.ExtensionFilter("JPG", "*.jpg"),
				new FileChooser.ExtensionFilter("PNG", "*.png"),
				new FileChooser.ExtensionFilter("GIF", "*.gif"),
				new FileChooser.ExtensionFilter("BMP", "*.bmp"));
		return fileChooser;
	}

	public static DirectoryChooser directoryChooser(String title, String defaultDir){
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle(title);
		File defaultDirectory = new File(defaultDir);
		chooser.setInitialDirectory(defaultDirectory);
		return chooser;
	}
	
	public static void createFolder(String sourcePath, int order, String name){
		String fullName = "";
		if (order < 10) {
			fullName = "00" + order + "-" + name;
		} else if (order < 100) {
			fullName = "0" + order + "-" + name;
		} else {
			fullName = "" + order + "-" + name;
		}
		try {
			File folder = new File(sourcePath + File.separator + fullName);
			folder.mkdirs();
		} catch (Exception e) {
			AppDebug.error(e, "");
		}
	}
}