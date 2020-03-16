/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P4.twitter;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
    	Timespan ts = null;
    	int n = tweets.size();
    	if(n>1) {
    		Instant a=tweets.get(0).getTimestamp(),b=tweets.get(0).getTimestamp();
    		for(int i=1;i<n;i++) {
    			if(tweets.get(i).getTimestamp().isAfter(b))  b=tweets.get(i).getTimestamp();
    			else if(tweets.get(i).getTimestamp().isBefore(a)) a=tweets.get(i).getTimestamp();
    		}
    		ts = new Timespan(a,b);
    	}
    	else if(n==1) ts = new Timespan(tweets.get(0).getTimestamp(),tweets.get(0).getTimestamp());
		return ts;
        //throw new RuntimeException("not implemented");
    }

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT 
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
    	Set<String> mu = new HashSet<String>();
    	Pattern pattern = Pattern.compile("@([\\w,-]+)");
    	for(Tweet tweet:tweets) {
    		String textstring = tweet.getText();
    		Matcher matcher = pattern.matcher(textstring.toLowerCase());
    		while (matcher.find()) {
    			String userName = null;
    			int start = matcher.start();
    			if(start > 0) {
    				char tch = textstring.charAt(start-1);
    				if(tch!='-' && tch!='_' && !Character.isDigit(tch) && !Character.isLetter(tch)){
    					userName = matcher.group(1);
    				}
    			}
    			else userName = matcher.group(1);
    			if(userName != null) mu.add(userName);	
    		}
    	}
    	return mu;
        //throw new RuntimeException("not implemented");
    }
    
    public static Set<String> getMentionedTags(List<Tweet> tweets){
    	Set<String> tags = new HashSet<String>();
    	Pattern pattern = Pattern.compile("#([\\w,-]+)");
    	for(Tweet tweet:tweets) {
    		String textstring = tweet.getText();
    		Matcher matcher = pattern.matcher(textstring);
    		while(matcher.find()) {
    			String tag = matcher.group();
    			tags.add(tag);
    		}  			
    	}
    	return tags;
    }
}
