package com.revogear.utils.io;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.EnumSet;
import java.util.Set;

import static java.nio.file.StandardOpenOption.*;

public class FileSystemUtility {

//	/**
//	 * Logger for this class
//	 */
//	private static final Logger logger = LoggerFactory.getLogger(FileSystemUtility.class);

	private static int IO_BUFFER_SIZE = 4096;

	public FileSystemUtility() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static String getFileExtension(String fileName) {
		int idx = fileName.lastIndexOf(".");
		if (idx != -1) {
			return fileName.substring(idx + 1);
		}
		return null;
	}

	public static String getFileExtension(File file) {
		return getFileExtension(file.getName());
	}

	public static byte[] fileToByte(File file) throws FileNotFoundException, IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		copyInputStream(new FileInputStream(file), bos);

		return bos.toByteArray();
	}

	public static void copyInputStream(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[IO_BUFFER_SIZE];
		int len;
		int tot = 0;

		while ((len = in.read(buffer)) >= 0) {
			out.write(buffer, 0, len);
			tot += len;
		}

//		System.out.println("SCRITTI "+tot);
		in.close();
		out.close();
	}

	public static void checkForDirectory(File directory, boolean createIfNonExists) throws FileNotFoundException {
		boolean r = true;

		try {
			if (directory == null || !directory.exists() || !directory.isDirectory())
				throw new FileNotFoundException(
						"Il file: " + directory.getPath() + " non esiste, oppure non � una cartella.");
		} catch (FileNotFoundException e) {
			if (createIfNonExists)
				directory.mkdirs();
			else
				throw e;
		}

	}

	public static void checkForFile(File file) throws FileNotFoundException {
		if (file == null || !file.exists() || !file.isFile())
			throw new FileNotFoundException("Il file: " + file.getPath() + " non esiste, oppure non  un file.");
	}

	public static File writeTempFile(String fileName, InputStream is, String tmpDir) throws IOException {
		File f = null;
		try {
			File ff = new File(tmpDir);
			if (ff.exists()) {
				ff.mkdir();
			}
			f = new File(ff, fileName);
			if (f.exists()) {
				f.delete();
			}

			FileOutputStream fw = new FileOutputStream(f);

			long readByte = 0;
			byte[] buff = new byte[512];
			int len = 0;

			len = is.read(buff);
			while (len != -1) {
				fw.write(buff, 0, len);
				readByte += len;
				len = is.read(buff);

			}
			fw.flush();
			fw.close();

		} catch (Exception e) {

			throw e;
		}

		return f;
	}

	public static void eliminaCartella(File pathCartella, boolean eliminaSoloContenuto) throws IOException {

		// Make sure the file or directory exists and isn't write protected
		if (!pathCartella.exists())
			throw new IllegalArgumentException("Delete: File o Directory non trovata: " + pathCartella.getPath());

		if (!pathCartella.canWrite())
			throw new IllegalArgumentException("Delete: write protected: " + pathCartella.getPath());

		if (pathCartella.isDirectory()) {
			for (File child : pathCartella.listFiles()) {
				eliminaCartella(child.getAbsoluteFile(), false);
			}

		}

		String nomeFile = pathCartella.getAbsolutePath();
		if (!(eliminaSoloContenuto && pathCartella.getAbsolutePath().equals(pathCartella))) {
			boolean success = pathCartella.delete();
			if (!success)
				throw new IllegalArgumentException("Delete file: deletion failed " + nomeFile);
		}
	}

	public static void writeAsBuffer(InputStream is, Path dest, Set<PosixFilePermission> perms) throws IOException {
		int len;
		byte[] buff = new byte[1024];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		while ((len = is.read(buff)) != -1) {
			out.write(buff, 0, len);
		}
		ByteBuffer bb = ByteBuffer.wrap(out.toByteArray());

		FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);

		try (SeekableByteChannel sbc = Files.newByteChannel(dest, EnumSet.of(CREATE, TRUNCATE_EXISTING, WRITE), attr)) {
			sbc.write(bb);
		} catch (IOException e) {
			throw e;
		}

	}

	public static void purgeFolder(String folderName) {
		purgeFolder(new File(folderName), true);
	}

	private static void purgeFolder(File file, boolean delete) {
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();

				for (File f : files) {
					purgeFolder(f, true);
				}
			}
			if (delete) {
				file.delete();
			}
		}
	}

	/**
	 *
	 * @param file
	 * @return
	 */
	public static byte[] toByteArray(File file) {

		if (!file.exists()) {
//			logger.error("File "+ file.getName() + " is not exist");
			return null;
		}

		byte[] bFile = new byte[(int) file.length()];

		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			fileInputStream.read(bFile);
			fileInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
//			logger.error("Failed to reading file "+ e.getMessage());
			return null;
		}

		return bFile;
	}

	/**
	 *
	 * @param bytes
	 * @param dirPath
	 * @param filename
	 * @return
	 */
	public static File toFileObject(byte[] bytes, String dirPath, String filename) {

		Path path = Paths.get(dirPath + File.separator + filename);
		try {
			Files.write(Paths.get(dirPath + File.separator + filename), bytes);
		} catch (IOException e) {
			e.printStackTrace();
//			logger.error("Failed to write file "+ e.getMessage());
			return null;
		}

		File file = path.toFile();

		if (file.exists()) {
//			logger.info("File "+ file.getName() +" is created successfully");
		} else {
//			logger.error("File "+ file.getName() +" is not created");
			return null;
		}

		return file;
	}

	public static byte[] inputStreamToByteArray(InputStream inputStream) throws IOException {

		return IOUtils.toByteArray(inputStream);
	}

}
