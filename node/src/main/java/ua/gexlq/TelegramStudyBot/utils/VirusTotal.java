package ua.gexlq.TelegramStudyBot.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.kanishka.virustotal.dto.FileScanReport;
import com.kanishka.virustotal.dto.ScanInfo;
import com.kanishka.virustotal.exception.APIKeyNotFoundException;
import com.kanishka.virustotal.exception.UnauthorizedAccessException;
import com.kanishka.virustotalv2.VirusTotalConfig;
import com.kanishka.virustotalv2.VirustotalPublicV2;
import com.kanishka.virustotalv2.VirustotalPublicV2Impl;

@Component
public class VirusTotal {

	@Value("${virus-total.apikey}")
	private String virusTotalApiKey;

	public boolean isFileContainsViruses(String filePath) throws Exception {

		try {
			VirusTotalConfig.getConfigInstance().setVirusTotalAPIKey(virusTotalApiKey);
			VirustotalPublicV2 virusTotalRef = new VirustotalPublicV2Impl();

			ScanInfo scanInformation = virusTotalRef.scanFile(new File(filePath));

			String resource = scanInformation.getResource();
			FileScanReport report = virusTotalRef.getScanReport(resource);

			Integer positives = report.getPositives();
			
			if (positives == null)
				return false;

			return positives > 5 ? true : false;

		} catch (APIKeyNotFoundException ex) {
			throw new APIKeyNotFoundException("API Key not found! " + ex.getMessage());
		} catch (UnsupportedEncodingException ex) {
			throw new UnsupportedEncodingException("Unsupported Encoding Format!" + ex.getMessage());
		} catch (UnauthorizedAccessException ex) {
			throw new UnauthorizedAccessException("Invalid API Key " + ex.getMessage());
		} catch (Exception ex) {
			throw new Exception("Something Bad Happened! " + ex.getMessage());
		}
	}
}
