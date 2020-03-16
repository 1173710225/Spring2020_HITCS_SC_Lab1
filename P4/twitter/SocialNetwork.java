/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P4.twitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * SocialNetwork provides methods that operate on a social network.
 * 
 * A social network is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames. Users can't follow themselves. If A
 * doesn't follow anybody, then map[A] may be the empty set, or A may not even exist
 * as a key in the map; this is true even if A is followed by other people in the network.
 * Twitter usernames are not case sensitive, so "ernie" is the same as "ERNie".
 * A username should appear at most once as a key in the map or in any given
 * map[A] set.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     * @param tweets
     *            a list of tweets providing the evidence, not modified by this
     *            method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of
     *         tweets.
     *         One kind of evidence that Ernie follows Bert is if Ernie
     *         @-mentions Bert in a tweet. This must be implemented. Other kinds
     *         of evidence may be used at the implementor's discretion.
     *         All the Twitter usernames in the returned social network must be
     *         either authors or @-mentions in the list of tweets.
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
    	Map<String, Set<String>> gfg = new HashMap<String, Set<String>>();
    	for(Tweet tweet:tweets) {								//遍历每条推特
    		if(!gfg.containsKey(tweet.getAuthor())) {			//对于新author，添加map的key
    			Set<String> authorfollow = new HashSet<String>();
    			gfg.put(tweet.getAuthor(),authorfollow);
    		}
    		Set<String> follow = gfg.get(tweet.getAuthor());
    		follow.addAll(Extract.getMentionedUsers(Arrays.asList(tweet)));		//取并集
    		//gfg.put(tweet.getAuthor(), follow);
    	}
    	return gfg;
        //throw new RuntimeException("not implemented");
    }
    
    
    /**
     * Guess who might follow whom, from evidence found in tweets.
     * get smarter 
     * @param tweets
     *            a list of tweets providing the evidence, not modified by this
     *            method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of
     *         tweets.
     *         People who use the same hashtags in their tweets (e.g. #mit) may
     *         mutually influence each other. People who share a hashtag that 
     *         isn’t otherwise popular in the dataset, or people who share multiple 
     *         hashtags, may be even stronger evidence.
     */
    public static Map<String, Set<String>> guessFollowsGraph_smart(List<Tweet> tweets) {
    	Map<String, Set<String>> gfg = new HashMap<String, Set<String>>();
    	for(Tweet tweet:tweets) {								//遍历每条推特，为每个author及其follow的user集做map
    		if(!gfg.containsKey(tweet.getAuthor())) {			//对于新author，添加map的key
    			Set<String> newfollow = new HashSet<String>();
    			gfg.put(tweet.getAuthor(),newfollow);
    		}
    		Set<String> myfollow = gfg.get(tweet.getAuthor());
    		myfollow.addAll(Extract.getMentionedUsers(Arrays.asList(tweet)));		//取并集
    		//gfg.put(tweet.getAuthor(), myfollow);
    	}
    	
    	Map<String, Set<String>> tagmap = new HashMap<String, Set<String>>();
    	Set<String> tags = Extract.getMentionedTags(tweets);		//所有推特中的所有tag
    	for(Tweet tweet:tweets) {								//遍历每条推特，为每个author及其tag集做map
    		if(!tagmap.containsKey(tweet.getAuthor())) {			//对于新author，添加map的key
    			Set<String> authorfollow = new HashSet<String>();
    			tagmap.put(tweet.getAuthor(),authorfollow);
    		}
    		Set<String> mytag = tagmap.get(tweet.getAuthor());
    		mytag.addAll(Extract.getMentionedTags(Arrays.asList(tweet)));		//取并集
    		//tagmap.put(tweet.getAuthor(), mytag);
    	}
    	int tagmax = tags.size()/2;        //所有tag的数/2，作为拥有很多tag的判断
    	Set<String> mintag = new HashSet<String>();			//低热度tag的集
    	int coldtag = tagmap.size()/10;		//所有的人数/10，作为低热度tag的标准
    	for(String tag:tags) {
    		int number = 0;
    		for(Map.Entry<String, Set<String>> map:tagmap.entrySet()) {
    			if(map.getValue().contains(tag)) number++;
    		}
    		if (coldtag>= number) mintag.add(tag);
    	}
    	
    	for(Map.Entry<String, Set<String>> map1:tagmap.entrySet()) {			//两两判断
    		for(Map.Entry<String, Set<String>> map2:tagmap.entrySet()) {
    			if(map1 != map2) {
    				List<String> m1 = new ArrayList<String>(map1.getValue());
    				List<String> m2 = new ArrayList<String>(map2.getValue());
    				m1.retainAll(m2);			//取交集
    				int m1number = m1.size();
    				m1.retainAll(mintag);	//与低热度tag集取交集
    				if(m1number >= tagmax || m1.size()>0 ) {				//若大于一定数量，或者交集有热度很低的tag，则认为二人互相follow
    					Set<String> tagfollow = gfg.get(map1.getKey());
    					tagfollow.add(map2.getKey());
    					//gfg.put(map1.getKey(), tagfollow);
    				
    					Set<String> tagfollow1 = gfg.get(map2.getKey());
    					tagfollow1.add(map1.getKey());
    					//gfg.put(map2.getKey(), tagfollow1);
    				}
    			}
    		}
    	}
    	return gfg;
        //throw new RuntimeException("not implemented");
    }

    /**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     * 
     * @param followsGraph
     *            a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
    	List<String> inf = new ArrayList<>();
    	Map<String, Set<String>> followers = new HashMap<String, Set<String>>();
    	for(Map.Entry<String, Set<String>> map:followsGraph.entrySet()) {
    		String fan = map.getKey();							
    		Set<String> nobody = new HashSet<String>();
    		if(!followers.containsKey(fan)) followers.put(fan, nobody);//初始化
    		Set<String> fans = new HashSet<String>();   
    		fans.add(fan);									//follow的人
    		for(String star:map.getValue()) {				//被follow的人
    			if(followers.containsKey(star)) {
    				Set<String> starf = followers.get(star);				//被follow的人已有的粉丝
    				starf.add(fan);
    			}
    			else followers.put(star, fans);
    		}
    	}
    	while(!followers.isEmpty()) {			//排序
    		int max = -1;			
    		String fol = null;
    		for(Map.Entry<String, Set<String>> map:followers.entrySet()) {
    			int num = map.getValue().size();
    			if (num>max) {
    				fol = map.getKey();
    				max = num;
    			}
    		}
    		inf.add(fol);
    		followers.remove(fol);
    	}
    	return inf;
        //throw new RuntimeException("not implemented");
    }


}
