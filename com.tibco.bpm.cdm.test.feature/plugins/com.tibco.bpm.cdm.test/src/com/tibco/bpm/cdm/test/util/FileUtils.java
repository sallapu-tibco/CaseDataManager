package com.tibco.bpm.cdm.test.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.tibco.bpm.da.dm.api.DataModel;
import com.tibco.bpm.da.dm.api.DataModelSerializationException;
import com.tibco.bpm.dt.rasc.RuntimeApplication;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;
import com.tibco.bpm.dt.rasc.impl.RuntimeApplicationImpl;

/**
 * Utility methods for manipulating files and similar resources
 * @author smorgan
 */
public class FileUtils
{
	@FunctionalInterface
	public interface DataModelModifier
	{
		void modify(DataModel dm);
	}

	// Default max file count allowed when producing a ZIP
	private static int MAX_FILES = 30;

	private static int addFilesToZip(ZipOutputStream zos, File[] files, File baseFile, int filesLimit)
			throws IOException
	{
		int filesAdded = 0;
		byte[] buf = new byte[1024];
		for (File file : files)
		{
			System.out.println("Adding " + file);
			if (file.isDirectory())
			{
				File[] listFiles = file.listFiles();
				filesAdded += addFilesToZip(zos, listFiles, baseFile, filesLimit);
			}
			else
			{
				filesAdded++;
				if (filesAdded > filesLimit)
				{
					// This is a pragmatic sanity check to depend against accidentally choosing the wrong folder.
					// i.e. Selecting the root path would result in an attempt to zip up the entire drive, which will
					// then run out of disk space and cause all sorts of problems.  
					throw new IOException("Folder contains more than the permitted number of files. Maximum allowed is "
							+ filesLimit);
				}
				FileInputStream fis = new FileInputStream(file.getAbsoluteFile());
				URI relative = baseFile.toURI().relativize(file.getAbsoluteFile().toURI());
				zos.putNextEntry(new ZipEntry(relative.toString()));
				int len;
				while ((len = fis.read(buf)) > 0)
				{
					zos.write(buf, 0, len);
				}
				fis.close();
			}
		}
		return filesAdded;
	}

	public static File buildZipFromFolderURL(URL url, boolean deleteOnExit) throws IOException, URISyntaxException
	{
		File tempFile = File.createTempFile("temp", ".zip");
		if (deleteOnExit)
		{
			tempFile.deleteOnExit();
		}
		FileOutputStream fos = new FileOutputStream(tempFile);
		ZipOutputStream zos = new ZipOutputStream(fos);

		File f = new File(url.toURI());
		File[] listFiles = f.listFiles();
		int count = addFilesToZip(zos, listFiles, f, MAX_FILES);
		System.out.println("Added " + count + " files to " + tempFile + " from " + f);
		zos.close();
		return tempFile;
	}

	public static File buildZipFromFolderResource(String resourcePath, boolean deleteOnExit)
			throws IOException, URISyntaxException
	{
		return buildZipFromFolderURL(FileUtils.class.getResource(resourcePath), deleteOnExit);
	}

	public static FileInputStream buildInputStreamForZipFromFolderURL(URL url) throws IOException, URISyntaxException
	{
		return buildInputStreamForZipFromFolderURL(url, true);
	}

	public static FileInputStream buildInputStreamForZipFromFolderURL(URL url, boolean deleteOnExit)
			throws IOException, URISyntaxException
	{
		File file = buildZipFromFolderURL(url, deleteOnExit);
		return new FileInputStream(file);
	}

	public static void patchManifestInZip(Path zipPath, Map<String, String> properties) throws IOException
	{
		FileSystem zfs = FileSystems.newFileSystem(zipPath, null);
		Path zPath = zfs.getPath("/MANIFEST.MF");
		Manifest mf = new Manifest(Files.newInputStream(zPath));
		Attributes mainAttributes = mf.getMainAttributes();
		for (Entry<String, String> entry : properties.entrySet())
		{
			mainAttributes.putValue(entry.getKey(), entry.getValue());
		}
		OutputStream os = Files.newOutputStream(zPath, StandardOpenOption.TRUNCATE_EXISTING);
		mf.write(os);
		os.close();
		zfs.close();
	}

	public static void setVersionInRASC(Path zipPath, String version) throws IOException
	{
		FileUtils.patchManifestInZip(zipPath, Collections.singletonMap("Application-Version", version));
	}

	public static void setVersionInRASC(File zipFile, String version) throws IOException
	{
		setVersionInRASC(zipFile.toPath(), version);
	}

	public static void modifyDataModelInZip(Path zipPath, String fileName, DataModelModifier modifier)
			throws IOException, DataModelSerializationException
	{
		FileSystem zfs = FileSystems.newFileSystem(zipPath, null);
		Path zPath = zfs.getPath("/" + fileName);
		DataModel dm = DataModel.deserialize(Files.newInputStream(zPath));
		modifier.modify(dm);
		String dataModelJson = dm.serialize();
		OutputStream os = Files.newOutputStream(zPath, StandardOpenOption.TRUNCATE_EXISTING);
		os.write(dataModelJson.getBytes(StandardCharsets.UTF_8));
		os.close();
		zfs.close();
	}

	public static RuntimeApplication loadRuntimeApplicationFromZip(File file)
			throws FileNotFoundException, RuntimeApplicationException, IOException
	{
		return new RuntimeApplicationImpl(new FileInputStream(file));
	}

	public static RuntimeApplication loadRuntimeApplicationFromZipResource(String resourcePath)
			throws FileNotFoundException, RuntimeApplicationException, IOException
	{
		return new RuntimeApplicationImpl(FileUtils.class.getResourceAsStream(resourcePath));
	}

	public static RuntimeApplication loadRuntimeApplicationFromRASCResourceFolder(String resourceFolderPath)
			throws IOException, URISyntaxException, RuntimeApplicationException
	{
		File file = buildZipFromFolderURL(FileUtils.class.getResource(resourceFolderPath), true);
		RuntimeApplication runtimeApplication = new RuntimeApplicationImpl(new FileInputStream(file));
		return runtimeApplication;

	}

	public static String readFileContents(String fileLocation)
	{
		InputStream inputStream = FileUtils.class.getClassLoader().getResourceAsStream(fileLocation);
		Scanner scanner = new Scanner(inputStream, "UTF-8");
		String lineSeparator = System.getProperty("line.separator");

		StringBuilder s = new StringBuilder();

		while (scanner.hasNextLine())
		{

			s.append(scanner.nextLine());
			s.append(lineSeparator);

		}

		scanner.close();

		return s.toString();
	}

	public static String readFileFromRASC(String zipResourcePath, String filePath)
			throws IOException, URISyntaxException
	{
		URL url = ClassLoader.getSystemResource(zipResourcePath);
		Path path = Paths.get(url.toURI());
		FileSystem zfs = FileSystems.newFileSystem(path, null);
		Path p = zfs.getPath(filePath);
		String s = new String(Files.readAllBytes(p), "UTF-8");
		return s;
	}

}
