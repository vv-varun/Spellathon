package com.ayansh.spellathon.android;

import com.ayansh.spellathon.android.Application.Constants;

public class SpellathonConstants implements Constants {

	private static final byte[] SALT = new byte[] { -29, 82, 30, -57, -63, -11, 93, -98, 75, 64, -17, -35, 48, -56, -62, -79, -81, 99, -22, 49 };
	private boolean premiumVersion;
	
	public byte[] getSalt1() {
		return SALT;
	}

	@Override
	public String getPublicKey() {
		return "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqB0qPe/wOrr" +
				"WHwHdk7vDjO0w1btTyaLJXwbQBqFTSTEd/nxsmjF7If7TQFh2/Mux+L" +
				"F1C2Jc9KNcP/bBrka1RPkRpdbK/8om3NlY4kkmv2i9tUgLOyF1/egRv" +
				"3w26t/gf5nNCcCtYsPUdw+A6gZOFdN/7BR4v9VUTAbKRy6ZrtDsK4Ol" +
				"bpwNkgB38yw9HIfkPVBoWxpuKjDl+25xiC8zn+4eGT/zSnw8MiO02FP" +
				"cz0UtmQzjhx8dw4eQltwHjMUYy1GrW/3iw/9NElSdsOc5akEkfcY8hch" +
				"ayXeCONqF2FiCqUvpO5jHOXRbiosNU961sxvoV8QwVEbriXzobyPtzQIDAQAB";
	}

	@Override
	public String getPreferencesFile() {
		return "Spellathon";
	}

	@Override
	public String getLoggerTag() {
		return "Spellathon";
	}

	@Override
	public String getDBName() {
		return "Spellathon_DB.db";
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
		return "ID";
	}

	@Override
	public int getCompletedDrawable() {
		return R.mipmap.completed;
	}

	@Override
	public int getPauseDrawable() {
		return R.mipmap.pause;
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
		
		if(prodName.contentEquals("ShowMeanings")){
			return "show_meanings";
		}
		return null;
		
	}

	@Override
	public void setPremiumVersion(boolean premium){
		premiumVersion = premium;
	}

	@Override
	public boolean isPremiumVersion() {
		return premiumVersion;
	}

}