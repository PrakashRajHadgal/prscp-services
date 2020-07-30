package org.egov.prscp.util;

import java.util.List;

import org.egov.prscp.config.PrScpConfiguration;
import org.egov.prscp.web.models.FileStore;
import org.egov.prscp.web.models.Files;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FileStoreUtils {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private PrScpConfiguration config;

	/**
	 * Get file path of documents 
	 * @param filestoreIds 
	 * @return list of file paths
	 */
	public List<Files> getFiles(String tenantId, List<Files> fileStoreIds) {

		StringBuilder url = new StringBuilder(config.getFilStoreHost());
		url.append(config.getFilStoreUrlEndPoint());
		url.append("?tenantId=" + tenantId.split("\\.")[0]);
		url.append("&fileStoreIds=");

		for (Files ids : fileStoreIds) {
			url.append(ids.getFileStoreId());
			url.append(",");
		}
		FileStore response = restTemplate.getForObject(url.toString(), FileStore.class);
		return response.getFileStoreIds();
	}
}
