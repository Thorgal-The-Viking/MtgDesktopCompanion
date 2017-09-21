package org.magic.api.tokens.impl;

import java.awt.image.BufferedImage;

import org.magic.api.beans.MagicCard;
import org.magic.api.interfaces.MagicTokensProvider;
import org.magic.api.interfaces.abstracts.AbstractTokensProvider;

public class TokenMTGOnlTokenProvider extends AbstractTokensProvider {

	
	public TokenMTGOnlTokenProvider() {
		super();
		//http://alternative.mtg.onl/#/
		
	}
	
	
	@Override
	public boolean isTokenizer(MagicCard mc) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmblemizer(MagicCard mc) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public MagicCard generateTokenFor(MagicCard mc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MagicCard generateEmblemFor(MagicCard mc) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BufferedImage getPictures(MagicCard tok) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getName() {
		return "Tokens.MTG.onl";
	}

	@Override
	public String toString() {
		return getName();
	}

}