/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.magnum.mobilecloud.video;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.magnum.mobilecloud.repositories.Video;
import org.magnum.mobilecloud.repositories.VideoRepository;
import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

@Controller
public class VideoController {
	
	//This is collection of videos. and the long id.
	//	private static final AtomicLong currentId = new AtomicLong(0L);
	//private Map<Long,Video> videos = new HashMap<Long, Video>();

	@Autowired
	VideoRepository videoRepo;
	
	
	@RequestMapping(value=VideoSvcApi.VIDEO_SVC_PATH, method=RequestMethod.GET)
	public @ResponseBody Collection<Video> getVideoList()
	{
		return Lists.newArrayList(videoRepo.findAll());
	}
	
	@RequestMapping(value=VideoSvcApi.VIDEO_SVC_PATH, method=RequestMethod.POST)
	public @ResponseBody Video addVideo(@RequestBody Video v)
	{
		Video video = v;
		
		video.setLikes(0);
		videoRepo.save(video);
		
		return video; 
	}
	
	@RequestMapping(value=VideoSvcApi.VIDEO_SVC_PATH + "/{id}", method=RequestMethod.GET)
	public @ResponseBody Video getVideoById(@PathVariable(VideoSvcApi.ID_PARAMETER) long id, HttpServletResponse response)
	{
		Video video = null;
		
		video = videoRepo.findOne(id);
		
		if(video == null)
		{
			SendError(response, 404);
		}
		
		return video;
	}
	
	@RequestMapping(value=VideoSvcApi.VIDEO_SVC_PATH + "/{id}/like", method=RequestMethod.POST)
	public @ResponseBody void likeVideo(@PathVariable(VideoSvcApi.ID_PARAMETER) long id, HttpServletResponse response)
	{
		Video video = videoRepo.findOne(id);
		Set<String> likers;
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		
		if(video == null)
		{
			SendError(response, 404);
			return;
		}
		
		likers = video.getLikesUsernames();
		
		if(likers.contains(userName))
		{
			SendError(response, 400);
			return;
		}
		
		response.setStatus(HttpServletResponse.SC_OK);
		likers.add(userName);
		video.setLikesUsernames(likers);
		video.setLikes(likers.size());
		videoRepo.save(video);
		
		return;
	}
	
	@RequestMapping(value=VideoSvcApi.VIDEO_SVC_PATH + "/{id}/unlike", method=RequestMethod.POST)
	public @ResponseBody void unlikeVideo(@PathVariable(VideoSvcApi.ID_PARAMETER) long id, HttpServletResponse response)
	{
		Video video = videoRepo.findOne(id);
		Set<String> likers;
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		
		if(video == null)
		{
			SendError(response, 404);
			return;
		}
		
		likers = video.getLikesUsernames();
		
		if(likers.contains(userName))
		{
			response.setStatus(HttpServletResponse.SC_OK);
			likers.remove(userName);
			video.setLikesUsernames(likers);
			video.setLikes(likers.size());
			videoRepo.save(video);
			return;
		}
		
		SendError(response, 400);
		return;
	}
	
	@RequestMapping(value=VideoSvcApi.VIDEO_SVC_PATH + "/{id}/likedby", method=RequestMethod.GET)
	public @ResponseBody Collection<String> getUsersWhoLikedVideo(@PathVariable(VideoSvcApi.ID_PARAMETER) long id, HttpServletResponse response)
	{
		Video video = videoRepo.findOne(id);
		Set<String> likers;
		
		if(video == null)
		{
			SendError(response, 404);
			return null;
		}
		
		response.setStatus(HttpServletResponse.SC_OK);
		likers = video.getLikesUsernames();
		
		return likers;
	}
	
	@RequestMapping(value=VideoSvcApi.VIDEO_TITLE_SEARCH_PATH, method=RequestMethod.GET)
	public @ResponseBody Collection<Video> findByTitle(@RequestParam(VideoSvcApi.TITLE_PARAMETER) String title)
	{
		return videoRepo.findByName(title);
	}
	
	@RequestMapping(value=VideoSvcApi.VIDEO_DURATION_SEARCH_PATH, method=RequestMethod.GET)
	public @ResponseBody Collection<Video> findByDurationLessThan(@RequestParam(VideoSvcApi.DURATION_PARAMETER) long duration)
	{
		return videoRepo.findByDurationLessThan(duration);
	}
	
	
	private void SendError(HttpServletResponse response, int errorCode)
	{
		try {
			response.sendError(errorCode);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
