/**
 * 
 */
package org.varunverma.spellathon.Application;

/**
 * @author Varun Verma
 *
 */
public interface Constants {

	public byte[] getSalt();
	
	public String getPublicKey();
	
	public String getImageDir();
	
	public String getPreferencesFile();
	
	public int getActivityCodes(String activity);
	
	public String getLoggerTag();
	
	public String getDBName();
	
	public String getGameInfoTableName();
	
	public String getGameSolutionTableName();
	
	public int getDBVersion();
	
	public String getGameIdFieldName();

	public int getCompletedDrawable();
	
	public int getPauseDrawable();
	
	public int getZeroStarDrawable();
	
	public int getOneStarDrawable();
	
	public int getTwoStarDrawable();
	
	public int getThreeStarDrawable();

	public int getIdForGameId();

	public int getIdForRating();

	public int getIdForStatus();

	public int getIdForWord();

	public int getIdForMeaning();
	
	public String getProductKey(String prodName);
	
	public boolean isPremiumVersion();

	public void setPremiumVersion(boolean premium);
	
}