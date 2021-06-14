package fr.sgo.entity;

import fr.sgo.service.IDGenerator;
import fr.sgo.service.ProfileInfo;

public class HostedGroupChat extends GroupChat {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6948740240113777937L;

	public HostedGroupChat(String name) {
		super(name);
		this.id = IDGenerator.newId();
		correspondents.add(new Correspondent(ProfileInfo.getInstance().getUserId(),
				ProfileInfo.getInstance().getUserName(), true));
	}

}
