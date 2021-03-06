package org.magic.api.decksniffer.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.magic.api.beans.MagicCard;
import org.magic.api.beans.MagicDeck;
import org.magic.api.beans.RetrievableDeck;
import org.magic.api.interfaces.MTGCardsProvider;
import org.magic.api.interfaces.abstracts.AbstractDeckSniffer;
import org.magic.services.MTGControler;
import org.magic.tools.URLTools;

public class DeckstatsDeckSniffer extends AbstractDeckSniffer {

	private static final String MAX_PAGE = "MAX_PAGE";
	private static final String FORMAT = "FORMAT";
	private static final String TIMEOUT = "TIMEOUT";
	private static final String URL = "URL";
	private Map<Integer, String> cacheColor;

	
	public DeckstatsDeckSniffer() {
		super();
		cacheColor = new HashMap<>();
		initcache();
	}

	private void initcache() {
		cacheColor.put(1, "{W}");
		cacheColor.put(2, "{U}");
		cacheColor.put(3, "{W}{U}");
		cacheColor.put(4, "{B}");
		cacheColor.put(5, "{W}{B}");
		cacheColor.put(6, "{U}{B}");
		cacheColor.put(7, "{W}{U}{B}");
		cacheColor.put(8, "{R}");
		cacheColor.put(9, "{W}{R}");
		cacheColor.put(10, "{U}{R}");
		cacheColor.put(11, "{W}{U}{R}");
		cacheColor.put(12, "{B}{R}");
		cacheColor.put(13, "{W}{B}{R}");
		cacheColor.put(14, "{U}{B}{R}");
		cacheColor.put(15, "{W}{U}{B}{R}");
		cacheColor.put(16, "{G}");
		cacheColor.put(17, "{W}{G}");
		cacheColor.put(18, "{U}{G}");
		cacheColor.put(19, "{W}{U}{G}");
		cacheColor.put(20, "{B}{R}");
		cacheColor.put(21, "{W}{B}{G}");
		cacheColor.put(22, "{U}{B}{G}");
		cacheColor.put(23, "{W}{U}{B}{G}");
		cacheColor.put(24, "{R}{G}");
		cacheColor.put(25, "{W}{R}{G}");
		cacheColor.put(26, "{U}{R}{G}");
		cacheColor.put(27, "{W}{U}{R}{G}");
		cacheColor.put(28, "{B}{R}{G}");
		cacheColor.put(29, "{W}{B}{R}{G}");
		cacheColor.put(30, "{U}{B}{R}{G}");
		cacheColor.put(31, "{W}{U}{B}{R}{G}");
	}

	@Override
	public String[] listFilter() {
		return new String[] { "casual", "standard", "modern", "legacy", "edh-commander", "highlander", "frontier",
				"pauper", "vintage", "extended", "cube", "tiny-leaders", "peasant", "other" };
	}

	@Override
	public MagicDeck getDeck(RetrievableDeck info) throws IOException {
		//

		MagicDeck deck = new MagicDeck();

		logger.debug("get deck " + info.getUrl());
		Document d = URLTools.extractHtml(info.getUrl().toString());

		deck.setDescription(info.getUrl().toString());
		deck.setName(info.getName());
		
		if (d.select("div#deck_overview_info") != null)
			deck.setDescription(d.select("div#deck_overview_info").select("div.deck_text_editable_container").text());

		
		for (Element a : d.select("a.deck_tags_list_tag"))
			deck.getTags().add(a.text());

		Elements e = d.select("textarea#deck_code");
		String content= e.html();
		
		String[] arr  = content.split("\n");
		
		arr = ArrayUtils.remove(arr, 0); //remove deck name
		arr = ArrayUtils.remove(arr, 0); //remove //main
		
		for(String s : arr)
		{
			try {
					if(s.startsWith("SB: "))
					{
						s=s.replaceFirst("SB: ", "").trim();
						MagicCard mc = MTGControler.getInstance().getEnabled(MTGCardsProvider.class).searchCardByName(parseString(s).getKey(), null, true).get(0);
						deck.getSideBoard().put(mc,parseString(s).getValue());
						notify(mc);
					}
					else
					{
						MagicCard mc = MTGControler.getInstance().getEnabled(MTGCardsProvider.class).searchCardByName(parseString(s).getKey(), null, true).get(0);
						deck.getMain().put(mc,parseString(s).getValue());
						notify(mc);
					}
			}
			catch(Exception ex)
			{
				logger.error("error parsing -> "+s);
			}
		}
		return deck;
	}


	@Override
	public List<RetrievableDeck> getDeckList() throws IOException {

		int nbPage = getInt(MAX_PAGE);
		List<RetrievableDeck> list = new ArrayList<>();

		for (int i = 1; i <= nbPage; i++) {
			Document d = URLTools.extractHtml(getString(URL) + "/" + getString(FORMAT) + "/?lng=fr&page=" + i);

			Elements e = d.select("tr.touch_row");

			for (Element cont : e) {
				RetrievableDeck deck = new RetrievableDeck();
				Element info = cont.select("a").get(0);
				String idColor = cont.select("img").get(0).attr("src");
				idColor = idColor.substring(idColor.lastIndexOf('/') + 1, idColor.lastIndexOf('.'));
				String name = info.text();
				String url = info.attr("href") + "/fr?get_code=1&code_type=bb_deck&code_extended=0&code_html_nl=off";
				String auteur = cont.select("a").get(1).text();

				deck.setName(name);
				try {
					deck.setUrl(new URI(url));
				} catch (URISyntaxException e1) {
					deck.setUrl(null);
				}
				deck.setAuthor(auteur);
				deck.setColor(cacheColor.get(Integer.parseInt(idColor)));

				list.add(deck);
			}
		}
		return list;
	}


	@Override
	public String getName() {
		return "DeckStats";
	}

	@Override
	public void initDefault() {
		setProperty(URL, "https://deckstats.net/decks/f/");
		setProperty(TIMEOUT, "0");
		setProperty(FORMAT, "standard");
		setProperty(MAX_PAGE, "2");

	}

	@Override
	public String getVersion() {
		return "3.0";
	}
	

	

}
