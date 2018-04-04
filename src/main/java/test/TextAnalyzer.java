package test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.spi.cache.CacheProvider;
import com.jayway.jsonpath.spi.cache.LRUCache;

public class TextAnalyzer {

	public static void main(String[] args) throws IOException {
	/*	CacheProvider.setCache(new LRUCache(200));
		ReadContext ctx = JsonPath.parse(new File("C:\\Users\\Nicolas\\.magicDeskCompanion\\cardsProviders\\AllSets-x.json"));
		ctx.withListeners(fr->{
			if(fr.path().startsWith("$"))
			{
				analyse(fr.result().toString());
			}
			return null;
		}).read("$..cards[*].text",List.class);
	*/
		analyse("{T}: Add {C} to your mana pool.\\n{T}, Sacrifice Quicksand: Target attacking creature without flying gets -1/-2 until end of turn.");
		
	}
		
		
		
		

	private static void analyse(String text) {
		Scanner scanner = new Scanner(text);
		System.out.println(scanner.next());
	}

}
