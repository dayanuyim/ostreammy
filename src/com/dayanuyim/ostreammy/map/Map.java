package com.dayanuyim.ostreammy.map;

/*
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.server.mvc.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;;


@Path("/map")
public class Map
{
	private static Logger logger = LoggerFactory.getLogger(Map.class);

	public Map(){
		System.out.println("init map");
	}
	
	@GET
    public String show(){
        return "Thanks to use the Map. Please specify a map to display";
    }   
 
	@POST
	@Path("/{map_id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Viewable showMap(@PathParam("map_id") String map_id,
    		@FormDataParam("file") InputStream file_istream,
    		@FormDataParam("file") FormDataContentDisposition file_disposition,
    		@Context HttpServletRequest request,
    		@Context HttpServletResponse response) throws Exception
    {
    	if(StringUtils.isBlank(map_id))
    		throw new IllegalArgumentException("no map specified");

    	//String gps_path = request.getParameter("file");
    	//if(StringUtils.isBlank(gps_path))
    	//	msg = "Please provide gps file";

    	if(file_istream == null)
    		throw new IllegalArgumentException("No file uploaded");
    	
    	String filename = file_disposition.getFileName();
    	logger.info("show tracks '{}' in map '{}'", filename, map_id);
    	
    	String msg = String.format("Show tracks[%s] in map[%s]", filename, map_id);
    	MapDescriptor desc = getMapDescriptor(map_id);
    	request.setAttribute("desc", desc);
    	request.setAttribute("msg", msg);
    	
    	return new Viewable("/map.jsp");
    	//return Response.status(Status.OK).entity(new Viewable("/map.jsp")).build();
    }
    
    private MapDescriptor getMapDescriptor(String id)
    {
    	MapDescriptor desc = new MapDescriptor();
    	desc.setMapId(id);
    	desc.setMapTitle("經建三版");
    	return desc;
    }

}
*/
