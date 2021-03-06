package org.magic.api.pictures.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.magic.api.beans.MagicCard;
import org.magic.api.beans.MagicEdition;
import org.magic.api.interfaces.abstracts.AbstractPicturesProvider;
import org.magic.services.MTGConstants;
import org.magic.tools.FileTools;
import org.magic.tools.ImageTools;

public class PersonalSetPicturesProvider extends AbstractPicturesProvider {

	private static final String FORMAT = "FORMAT";
	private static final String PICS_DIR = "PICS_DIR";

	@Override
	public STATUT getStatut() {
		return STATUT.DEV;
	}

	BufferedImage back;

	public void savePicture(BufferedImage bi, MagicCard mc, MagicEdition ed) throws IOException {
		File mainDir = getFile(PICS_DIR);
		File edDir = new File(mainDir, ed.getId());

		if (!edDir.exists())
			edDir.mkdir();

		ImageTools.write(bi, getString(FORMAT),
				Paths.get(edDir.getAbsolutePath(), mc.getId() + "." + getString(FORMAT).toLowerCase()).toFile());
	}

	public void removePicture(MagicEdition ed, MagicCard mc) {
		File mainDir = getFile(PICS_DIR);
		File edDir = new File(mainDir, ed.getId());
		
		try {
			FileTools.deleteFile(new File(edDir, mc.getId() + "." + getString(FORMAT)));
		} catch (IOException e) {
			logger.error("error removing " + new File(edDir, mc.getId() + "." + getString(FORMAT)),e);
		}
	}

	public PersonalSetPicturesProvider() {
		super();

		if (!getFile(PICS_DIR).exists())
			getFile(PICS_DIR).mkdir();

	}

	@Override
	public BufferedImage extractPicture(MagicCard mc) throws IOException {
		return null;
	}

	
	@Override
	public BufferedImage getPicture(MagicCard mc, MagicEdition ed) throws IOException {
		return getOnlinePicture(mc, ed);
	}
	
	
	@Override
	public String generateUrl(MagicCard mc, MagicEdition ed) {
		File mainDir = getFile(PICS_DIR);
		File edDir = new File(mainDir,mc.getCurrentSet().getId());
		if(ed!=null)
			edDir = new File(mainDir, ed.getId());
		
		return new File(edDir, mc.getId() + "." + getString(FORMAT)).getAbsolutePath();
	}
	
	@Override
	public BufferedImage getOnlinePicture(MagicCard mc, MagicEdition ed) throws IOException {
	
		try {
			return ImageTools.read(new File(generateUrl(mc, ed)));
		}
		catch(Exception e)
		{
			logger.debug(generateUrl(mc, ed) + " is not found");
			return null;
		}
	}

	@Override
	public String getName() {
		return "Personal Set Pictures";
	}

	
	@Override
	public void initDefault() {
		super.initDefault();
		
		setProperty(PICS_DIR,Paths.get(MTGConstants.DATA_DIR.getAbsolutePath(),"privateSets","pics").toFile().getAbsolutePath());
		setProperty(FORMAT,"PNG");
	}
	

	@Override
	public boolean equals(Object obj) {
		
		if(obj ==null)
			return false;
		
		return hashCode()==obj.hashCode();
	}
	
	@Override
	public int hashCode() {
		return getName().hashCode();
	}
	
	@Override
	public Icon getIcon() {
		return new ImageIcon(MTGConstants.IMAGE_LOGO);
	}

}
