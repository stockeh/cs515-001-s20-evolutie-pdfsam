/* 
 * This file is part of the PDF Split And Merge source code
 * Created on 09 ago 2016
 * Copyright 2017 by Sober Lemur S.a.s. di Vacondio Andrea (info@pdfsam.org).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as 
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.pdfsam;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.pdfsam.context.BooleanUserPreference;
import org.pdfsam.context.StringUserPreference;

/**
 * @author Jason Stock
 *
 */
public class PdfsamAppContextTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private PdfsamAppContext victim;

	@Before
	public void setUp() {
		victim = new PdfsamAppContext();
	}

	@After
	public void tearDown() {
		victim.getUserContext().clear();
	}

	@Test
	public void userContextNonNull() {
		assertNotNull(victim.getUserContext());
	}

	@Test
	public void initActiveModuleBranch() {
		victim.getUserContext().setStringPreference(StringUserPreference.STARTUP_MODULE, "");
		victim.initActiveModule();
		victim.getUserContext().setStringPreference(StringUserPreference.STARTUP_MODULE, "ChuckNorris");
		victim.initActiveModule();
	}

	@Test
	public void saveWorkspaceIfRequiredBranch() throws IOException {
		victim.getUserContext().setBooleanPreference(BooleanUserPreference.SAVE_WORKSPACE_ON_EXIT, false);
		victim.saveWorkspaceIfRequired();
		victim.getUserContext().setBooleanPreference(BooleanUserPreference.SAVE_WORKSPACE_ON_EXIT, true);
		victim.saveWorkspaceIfRequired();
		victim.getUserContext().setStringPreference(StringUserPreference.WORKSPACE_PATH,
				folder.getRoot().getAbsolutePath());
		victim.saveWorkspaceIfRequired();
		folder.newFile("tempFile.txt");
		victim.getUserContext().setStringPreference(StringUserPreference.WORKSPACE_PATH,
				folder.getRoot().getAbsolutePath() + "/tempFile.txt");
		victim.saveWorkspaceIfRequired();
		assertTrue(new File(folder.getRoot().getAbsolutePath() + "/tempFile.txt").exists());
	}

}
