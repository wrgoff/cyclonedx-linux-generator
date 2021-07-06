/*
 * Copyright (c) 2018,2019 Lockheed Martin Corporation.
 *
 * This work is owned by Lockheed Martin Corporation. Lockheed Martin personnel are permitted to use and
 * modify this software.  Lockheed Martin personnel may also deliver this source code to any US Government
 * customer Agency under a "US Government Purpose Rights" license.
 *
 * See the LICENSE file distributed with this work for licensing and distribution terms
 */
package com.lmco.efoss.unix.sbom;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.lmco.efoss.unix.sbom.exceptions.SBomException;
import com.lmco.efoss.unix.sbom.generator.SBomGenerator;
import com.lmco.efoss.unix.sbom.utils.DateUtils;

/**
 * (U) This Spring Boot application is used to build a Software Build Of Materials (SBOM) for a Unix
 * Virtual Machine (VM), regardless if it is Centos, Redhat, or Ubunto.
 *
 * @author wrgoff
 * @since 22 April 2020
 */
@SpringBootApplication
public class UnixSbomGeneratorApplication
{
	private static final Logger logger = Logger
			.getLogger(UnixSbomGeneratorApplication.class.getName());
	
	/**
	 * (U) Main method used to start the building of the linux operating system building of the
	 * Software Bill Of Materials (SBOM).
	 * 
	 * @param args String array of arguments.
	 */
	public static void main(String[] args)
	{
		Date startDate = DateUtils.rightNowDate();
		int softwareProcessed = 0;
		boolean failed = false;
		try
		{
			logger.debug("Starting Unix SBOM Generator.");
			
			softwareProcessed = SBomGenerator.generateSBom();
			SpringApplication.run(UnixSbomGeneratorApplication.class, args);
		}
		catch (SBomException sbe)
		{
			logger.error(sbe.getMessage(), sbe);
			failed = true;
		}
		catch (Exception e)
		{
			failed = true;
			logger.error("Encountered an unexpected error while attempting to build the Software " +
					"Bill Of Materials for your operating system!", e);
			
		}
		finally
		{
			if (logger.isInfoEnabled())
			{
				StringBuilder msg = new StringBuilder("It took " + DateUtils.computeDiff(startDate,
						DateUtils.rightNowDate()));
				if (failed)
					msg.append(" to fail to ");
				else
					msg.append(" to successfully ");
					
				msg.append("build the Software Bill Of Materials (SBOM)!");
					
				if (!failed)
					msg.append("  With " + softwareProcessed + " components!");
				
				logger.info(msg.toString());
			}
		}
	}
}