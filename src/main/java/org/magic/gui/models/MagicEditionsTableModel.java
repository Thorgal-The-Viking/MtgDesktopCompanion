package org.magic.gui.models;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ImageIcon;

import org.magic.api.beans.MagicCollection;
import org.magic.api.beans.MagicEdition;
import org.magic.api.interfaces.MTGDao;
import org.magic.gui.abstracts.GenericTableModel;
import org.magic.services.MTGControler;
import org.magic.services.extra.IconSetProvider;

public class MagicEditionsTableModel extends GenericTableModel<MagicEdition> {
	
	private Map<MagicEdition, Integer> mapCount;

	int countTotal = 0;
	int countDefaultLibrary = 0;
	
	@Override
	public void init(List<MagicEdition> editions) {
		this.items = editions;
		mapCount = new TreeMap<>();

		try {
			calculate();
		} catch (Exception e) {
			logger.error("error calculate", e);
		}

	}

	public void calculate() {

		MagicCollection mc = new MagicCollection(MTGControler.getInstance().get("default-library"));
		Map<String, Integer> temp;
		try {
			temp = MTGControler.getInstance().getEnabled(MTGDao.class).getCardsCountGlobal(mc);
		
		countDefaultLibrary = 0;
		countTotal = 0;
		for (MagicEdition me : items) {
			mapCount.put(me, (temp.get(me.getId()) == null) ? 0 : temp.get(me.getId()));
			countDefaultLibrary += mapCount.get(me);
		}

		for (MagicEdition me : items)
			countTotal += me.getCardCount();
		
		} catch (SQLException e) {
			logger.error("error in calculation",e);
		}
	}

	public Map<MagicEdition, Integer> getMapCount() {
		return mapCount;
	}

	public int getCountTotal() {
		return countTotal;
	}

	public void setCountTotal(int countTotal) {
		this.countTotal = countTotal;
	}

	public int getCountDefaultLibrary() {
		return countDefaultLibrary;
	}

	public void setCountDefaultLibrary(int countDefaultLibrary) {
		this.countDefaultLibrary = countDefaultLibrary;
	}

	public MagicEditionsTableModel() {
		columns=new String[] { "EDITION_CODE",
				"EDITION",
				"EDITION_SIZE",
				"DATE_RELEASE",
				"PC_COMPLETE",
				"QTY",
				"EDITION_TYPE",
				"EDITION_BLOCK",
				"EDITION_ONLINE" };

	}

	@Override
	public Object getValueAt(int row, int column) {
		MagicEdition e = items.get(row);
		if (column == 0)
			return IconSetProvider.getInstance().get24(e.getId());

		if (column == 1)
			return e;

		if (column == 2)
			return e.getCardCount();

		if (column == 3)
			return e.getReleaseDate();

		if (column == 4) {
			if (e.getCardCount() > 0)
				return (double) mapCount.get(e) / e.getCardCount();
			else
				return (double) mapCount.get(e) / 1;
		}

		if (column == 5)
			return mapCount.get(e);

		if (column == 6)
			return e.getType();

		if (column == 7)
			return e.getBlock();

		if (column == 8)
			return e.isOnlineOnly();

		return "";

	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {

		switch (columnIndex) {
		case 0:
			return ImageIcon.class;
		case 1:
			return MagicEdition.class;
		case 2:
			return Integer.class;
		case 3:
			return String.class;
		case 4:
			return double.class;
		case 5:
			return Integer.class;
		case 8:
			return Boolean.class;
		default:
			return Object.class;
		}
	}


}
