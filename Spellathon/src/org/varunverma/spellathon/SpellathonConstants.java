package org.varunverma.spellathon;

import org.varunverma.spellathon.Application.Constants;

public class SpellathonConstants implements Constants {

	private static final byte[] SALT = new byte[] { -29, 82, 30, -57, -63, -11, 93, -98, 75, 64, -17, -35, 48, -56, -62, -79, -81, 99, -22, 49 };
	
	@Override
	public byte[] getSalt() {
		return SALT;
	}

	@Override
	public String getPublicKey() {
		return "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArelQc"
				+ "1O1dRTgbd4N451Di09jXN0qGjg337tAW5wShXdVsC0bVRT7so"
				+ "SnVOeTUsJihJtOof6MOXM2tso+XVFp/9y/SBZeZzC0HZSefq+"
				+ "pHJ3IhhRB+6aytULAYR9gc2KoZPAl8zlUrZCjSSaa9fpTVQrr"
				+ "BX55EN2ZmsmHmfOYqoDpApFODs5oHU7JRUtufPKhGTQSYSDl/"
				+ "Z4eMjBgod06rJwIoi9B++a0R7oVKrmiGuSAHF/rdnOK9n6V2v"
				+ "fKwfwPST09AlQh+EvxMPORsyhViDIfnpUJCKxgmvzCZXewMYK"
				+ "YXsNjM8NreZFXFtkeXkPee4w+CTJfbinQTRsyKH4xfwIDAQAB";
	}

	@Override
	public String getImageDir() {
		return "/Android/data/.org.varunverma.Spellathon";
	}

	@Override
	public String getPreferencesFile() {
		return "SpellathonPremium";
	}

	@Override
	public int getActivityCodes(String activity) {
		
		if(activity.contentEquals("EULA")){
			return 1;
		}
		
		if(activity.contentEquals("GAME_UI")){
			return 2;
		}
		
		return 0;
	}

	@Override
	public String getLoggerTag() {
		return "Spellathon";
	}

	@Override
	public String getDBName() {
		return "Spellathon_Premium_DB.db";
	}

	@Override
	public String getGameInfoTableName() {
		return "GameInfo";
	}

	@Override
	public String getGameSolutionTableName() {
		return "GameSolution";
	}

	@Override
	public int getDBVersion() {
		return 1;
	}

	@Override
	public String getGameIdFieldName() {
		return "GameId";
	}

	@Override
	public int getCompletedDrawable() {
		return R.drawable.completed;
	}

	@Override
	public int getPauseDrawable() {
		return R.drawable.pause;
	}

	@Override
	public int getZeroStarDrawable() {
		return R.drawable.zero_star;
	}

	@Override
	public int getOneStarDrawable() {
		return R.drawable.one_star;
	}

	@Override
	public int getTwoStarDrawable() {
		return R.drawable.two_star;
	}

	@Override
	public int getThreeStarDrawable() {
		return R.drawable.three_star;
	}

	@Override
	public int getIdForGameId() {
		return R.id.game_id;
	}

	@Override
	public int getIdForRating() {
		return R.id.rating;
	}

	@Override
	public int getIdForStatus() {
		return R.id.game_status;
	}

	@Override
	public int getIdForWord() {
		return R.id.word;
	}

	@Override
	public int getIdForMeaning() {
		return R.id.meaning;
	}

	@Override
	public String getProductKey(String prodName) {
		return null;
	}

	public boolean isPremiumVersion() {
		return true;
	}

	public void setPremiumVersion(boolean premium) {
	
	}

}