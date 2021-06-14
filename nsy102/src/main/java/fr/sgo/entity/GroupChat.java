package fr.sgo.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public abstract class GroupChat extends Chat {

	/**
	 * 
	 */
	private static final long serialVersionUID = 279792385811091040L;
	protected String name;
	protected String id;
	protected Set<Correspondent> correspondents;

	public GroupChat(String name) {
		super();
		this.name = name;
		this.correspondents = Collections.synchronizedSet(new TreeSet<Correspondent>());
	}

	public String getName() {
		return name;
	}

	@Override
	public String getId() {
		return id;
	}

	public Collection<Correspondent> getCorrespondents() {
		return correspondents;
	}

	public void addCorrespondent(Correspondent correspondent) {
		if (correspondents.add(correspondent))
			reportChange();
	}

	public void addCorrespondents(Collection<Correspondent> correspondents) {
		if (this.correspondents.addAll(correspondents))
			reportChange();
	}

	public void removeCorrespondent(Correspondent correspondent) {
		if (correspondents.remove(correspondent))
			reportChange();
	}

	public void replaceCorrespondent(Correspondent correspondentToRemove, Correspondent correspondentToAdd) {
		correspondents.remove(correspondentToRemove);
		correspondents.add(correspondentToAdd);
	}

}
