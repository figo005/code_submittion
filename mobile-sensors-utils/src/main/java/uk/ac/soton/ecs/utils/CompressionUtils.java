package uk.ac.soton.ecs.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.Validate;

public class CompressionUtils {

	private static final int BUFFER = 2048;

	public static void gzip(File infile, boolean remove) {

		File outfile = new File(infile.getAbsoluteFile() + ".gz");
		System.out.println("compressing " + infile.getAbsolutePath() + " to "
				+ outfile.getAbsolutePath());

		try {
			GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(
					outfile));

			// Open the input file
			FileInputStream in = new FileInputStream(infile);

			// Transfer bytes from the input file to the GZIP output stream
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();

			// Complete the GZIP file
			out.finish();
			out.close();

			if (remove)
				infile.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void gunzip(File inFile, File outFile) {
		System.out.println("Expanding " + inFile.getAbsolutePath() + " to "
				+ outFile.getAbsolutePath());

		FileOutputStream out = null;
		GZIPInputStream zIn = null;
		FileInputStream fis = null;
		try {
			out = new FileOutputStream(outFile);
			fis = new FileInputStream(inFile);
			zIn = new GZIPInputStream(fis);
			byte[] buffer = new byte[8 * 1024];
			int count = 0;
			do {
				out.write(buffer, 0, count);
				count = zIn.read(buffer, 0, buffer.length);
			} while (count != -1);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static final void zipDirectory(File directory, File zip)
			throws IOException {
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zip));
		zip(directory, directory.getParentFile(), zos);
		zos.close();
	}

	private static final void zip(File directory, File base, ZipOutputStream zos)
			throws IOException {
		File[] files = directory.listFiles();
		byte[] buffer = new byte[8192];
		int read = 0;
		for (int i = 0, n = files.length; i < n; i++) {
			if (files[i].isDirectory()) {
				zip(files[i], base, zos);
			} else {
				FileInputStream in = new FileInputStream(files[i]);
				ZipEntry entry = new ZipEntry(files[i].getPath().substring(
						base.getPath().length() + 1));
				zos.putNextEntry(entry);
				while (-1 != (read = in.read(buffer))) {
					zos.write(buffer, 0, read);
				}
				in.close();
			}
		}
	}

	public static void unzip(File file, File destination) {
		try {
			BufferedOutputStream dest = null;
			BufferedInputStream is = null;
			ZipEntry entry;
			Validate.isTrue(file.exists());
			ZipFile zipfile = new ZipFile(file);
			Enumeration<? extends ZipEntry> e = zipfile.entries();
			while (e.hasMoreElements()) {
				entry = (ZipEntry) e.nextElement();
				// System.out.println("Extracting: " + entry);
				is = new BufferedInputStream(zipfile.getInputStream(entry));
				int count;
				byte data[] = new byte[BUFFER];
				File zipEntryFile = new File(destination, entry.getName());

				org.apache.commons.io.FileUtils.forceMkdir(zipEntryFile
						.getParentFile());

				FileOutputStream fos = new FileOutputStream(zipEntryFile);
				// System.out.println(zipEntryFile);

				dest = new BufferedOutputStream(fos, BUFFER);
				while ((count = is.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, count);
				}
				dest.flush();
				dest.close();
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IllegalArgumentException,
			IOException {
		unzip(
				new File(
						"/home/rs06r//workspace/experiments/setup/sensor_mdp-0.9experiment1/runs/runs_640_1276789868445.zip"),
				new File(
						"/home/rs06r//workspace/experiments/setup/sensor_mdp-0.9experiment1/runs/"));
	}
	/*
	 * public static void main(String[] args) throws IllegalArgumentException,
	 * IOException { zipDirectory(new File("/home/rs06r/workspace/utils/src"),
	 * new File( "test.zip")); }
	 */

}
