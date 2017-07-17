package org.magic.api.interfaces;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.swing.Icon;

import org.magic.api.beans.MagicCard;
import org.magic.api.beans.MagicCardStock;
import org.magic.api.beans.MagicDeck;

public interface CardExporter {

	public String getFileExtension();

	public void export(MagicDeck deck, File dest) throws Exception;
	public MagicDeck importDeck(File f) throws Exception;
	
	public void export(List<MagicCard> cards, File f) throws Exception;
	public void exportStock(List<MagicCardStock> stock,File f)throws Exception;
	public List<MagicCardStock> importStock(File f)throws Exception;
	
	
	public Icon getIcon();
	public String getName();
	public Properties getProperties();
	public void save();
	public void load();
	public void setProperties(String k, Object value);
	public Object getProperty(String k);	
	public boolean isEnable();
	public void enable(boolean t);

}