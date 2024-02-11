package ua.gexlq.TelegramStudyBot.utils;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.hibernate.boot.archive.spi.ArchiveException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

@Component
public class ArchiveChecker {
	public boolean isSafeArchive(String filePatString) throws Exception {
		try {
			File file = new File(filePatString);

			ZipFile zipFile = new ZipFile(file);

			if (isEncrypted(zipFile)) {
				return false;
			}

			Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();

			while (entries.hasMoreElements()) {
				ZipArchiveEntry entry = entries.nextElement();

				if (isArchive(entry.getName().toLowerCase())) {
					return false;
				}

			}
			return true;

		} catch (IOException e) {
			throw new IOException("File not found");
		} catch (ArchiveException e) {
			throw new ArchiveException("Something went wrong while processing archive");
		} catch (Exception e) {
			throw new Exception("Something went wrong");
		}
	}

	private boolean isEncrypted(ZipFile zipFile) {
		Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
		while (entries.hasMoreElements()) {
			ZipArchiveEntry entry = entries.nextElement();
			if (entry.getGeneralPurposeBit().usesEncryption()) {
				return true;
			}
		}
		return false;
	}

	// TODO FIND NEW WAY
	public boolean isArchive(String fileName) {
		String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

		List<String> archiveExtensions = Arrays.asList("zip", "tar", "tar.gz", "tar.bz2", "tar.xz", "7z", "rar", "gz",
				"bz2", "xz", "jar", "war", "ear", "cpio", "ar", "iso", "cab", "lzma", "Z", "deb", "rpm", "tar.Z",
				"tar.lz", "tgz", "tbz", "taz", "tb2", "tbz2", "tb2", "taz", "tgz", "txz", "tlz", "iso", "ustar", "xpi",
				"wim", "aar", "cbr", "cbz", "cfs", "apk", "cab", "001");

		if (archiveExtensions.contains(fileExtension.toLowerCase())) {
			return true;
		}

		return false;
	}
}
