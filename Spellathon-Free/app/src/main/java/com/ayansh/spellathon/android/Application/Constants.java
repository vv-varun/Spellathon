/**
 * 
 */
package com.ayansh.spellathon.android.Application;

/**
 * @author Varun Verma
 *
 */
public interface Constants {

	public String getPublicKey();

	public String getPreferencesFile();

	public String getLoggerTag();
	
	public String getDBName();
	
	public String getGameInfoTableName();
	
	public String getGameSolutionTableName();
	
	public int getDBVersion();
	
	public String getGameIdFieldName();

	public int getCompletedDrawable();
	
	public int getPauseDrawable();

	public int getIdForWord();

	public int getIdForMeaning();
	
	public String getProductKey(String prodName);
	
	public boolean isPremiumVersion();

	public void setPremiumVersion(boolean premium);
	
}