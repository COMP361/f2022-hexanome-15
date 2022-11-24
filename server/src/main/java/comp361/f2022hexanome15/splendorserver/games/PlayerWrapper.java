/**
 * Nov 23, 2022
 * TODO
 */
package comp361.f2022hexanome15.splendorserver.games;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author zacharyhayden
 *
 */
@Entity
public class PlayerWrapper {
	@Id
	private final String aUserName;

	private final static Map<String, PlayerWrapper> PLAYER_MAP = new HashMap<>();

	private PlayerWrapper(String pUserName) {
		aUserName = pUserName;
	}

	public static PlayerWrapper newPlayerWrapper(String pUserName) {
		if (PLAYER_MAP.containsKey(pUserName)) {
			return PLAYER_MAP.get(pUserName);
		} else {
			PlayerWrapper newplPlayerWrapper = new PlayerWrapper(pUserName);
			PLAYER_MAP.put(pUserName, newplPlayerWrapper);
			return newplPlayerWrapper;
		}
	}

	public String getaUserName() {
		return aUserName;
	}

}
