package uk.ac.ncl.cs.csc8101.weblogcoursework;


import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DataControl {
	private static HashMap<String, SiteSession> sessions = new LinkedHashMap<String, SiteSession>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		protected boolean removeEldestEntry(Map.Entry eldest) {
			SiteSession siteSession = (SiteSession) eldest.getValue();
			boolean shouldExpire = siteSession.isExpired();
			if (shouldExpire) {
				WriterSessionData.inside(siteSession);
			}
			return siteSession.isExpired();
		}
	};

	public static void setup() {
		SiteSession.resetGlobalMax();
	}

	public static void addsession(String id, long firstHitMillis, String url) {

		SiteSession session = sessions.get(id);
		

		if (session != null) {
			if (session.getLastHitMillis()+session.MAX_IDLE_MS < firstHitMillis) {
				WriterSessionData.inside(session);
				sessions.remove(id);
				session = new SiteSession(id, firstHitMillis, url);
				sessions.put(id, session);
				return;
			}
			session.update(firstHitMillis, url);
		} else {
			session = new SiteSession(id, firstHitMillis, url);
			sessions.put(id, session);
		}
	}

	public static void endsession() {
		for(Iterator it=sessions.entrySet().iterator();it.hasNext();){
			Entry<String, SiteSession> entry = (Entry<String, SiteSession>)it.next();
			WriterSessionData.inside(entry.getValue());
			
		}
		WriterSessionData.endconnection();
	}
}
